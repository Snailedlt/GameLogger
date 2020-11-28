package com.example.gamelogger.services

import android.util.Log
import com.example.gamelogger.classes.Game
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.ArrayList

/**
 *   Legger til ett spill i Firestore databasen
 **/
fun addSavedGame(game: Game) {
    val spillid = game.id.toString()
    val spillstate = game.state.toString()
    val spillPlat = game.chosenPlatform
    val spillDatoAdded = game.dateAdded
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser!!.uid

        val docRef = db.collection("savedGames").document(uid).collection("Games").document(spillid)

        val nestedData = hashMapOf(
            "spill id" to spillid,
            "spill state" to spillstate,
            "spill platform" to spillPlat,
            "spill lagt til" to spillDatoAdded
        )

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    db.collection("savedGames").document(uid).collection("Games").document(spillid)
                        .update(nestedData as Map<String, Any>)
                } else {
                    db.collection("savedGames").document(uid).collection("Games").document(spillid)
                        .set(nestedData as Map<String, Any>)
                }
            }
            .addOnFailureListener { exception ->
                Log.d("add", "get failed with ", exception)
            }
    } catch (e: Exception) {
        Log.d("update", "addSavedGame failed with ", e)
    }
}

/**
 *   Får tak i pålogget bruker og sender brukernavn i callback
 * */
fun getUser(myCallback: (String) -> Unit) {
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser!!.uid

        val docRef = db.collection("users").document(uid)

        docRef.get()
            .addOnSuccessListener { document ->
                val brukernavn: String = document.get("username") as String
                myCallback(brukernavn)
            }
            .addOnFailureListener { exception ->
                Log.d("get", "get failed with ", exception)
            }
    } catch (e: Exception) {
        Log.d("get", "getUser failed with ", e)
    }
}

/**
 *   Henter Spill som er lagret på brukeren sin database, legger dem i en string med
 *   all informasjon som trengs og sender den i en callback
 * */
fun getUserGames(myCallback: (MutableList<String>) -> Unit) {
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser!!.uid

        db.collection("savedGames").document(uid).collection("Games").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = ArrayList<String>()
                    for (document in task.result!!) {
                        val id = document.data["spill id"].toString()
                        val state = document.data["spill state"].toString()
                        val platform = document.data["spill platform"].toString()
                        val dateAdded = document.data["spill lagt til"].toString()
                        list.add(state)
                        list.add(platform)
                        list.add(dateAdded)
                        list.add(id)
                    }
                    myCallback(list)
                } else {
                    Log.e("MError: ", "Error getting games from firebase")
                }
            }
    } catch (e: Exception) {
        Log.d("get", "getUserGames failed with ", e)
    }

}

/**
 *   Henter og sorterer hvilken statuser spillene brukeren har lagret i databasen, legger dem i en
 *   floatarray og sender dem i en callback
 */
fun getUserGameState(myCallback: (FloatArray) -> Unit) {
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser!!.uid
        val stateArray = FloatArray(3)

        db.collection("savedGames").document(uid).collection("Games").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var backlog = 0F
                    var done = 0F
                    var playing = 0F
                    for (document in task.result!!) {
                        when (document.data["spill state"].toString()) {
                            "BACKLOG" -> {
                                backlog++
                            }
                            "PLAYING" -> {
                                playing++
                            }
                            "DONE" -> {
                                done++
                            }
                        }
                    }
                    stateArray[1] = backlog
                    stateArray[2] = done
                    stateArray[0] = playing
                    myCallback(stateArray)
                } else {
                    Log.e("MError: ", "Error getting game states from firebase")
                }
            }
    } catch (e: Exception) {
        Log.d("get", "getUserGameState failed with ", e)
    }
}

/**
 *   Henter hvilken plattformer spillene brukeren har lagret i databasen
 */
fun getUserGamePlatform(myCallback: (HashMap<String, Float>) -> Unit) {
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser!!.uid

        db.collection("savedGames").document(uid).collection("Games").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var hMap: HashMap<String, Float> = hashMapOf<String, Float>()
                    for (document in task.result!!) {
                        var platformName = document.data["spill platform"].toString()
                        if (!hMap.containsKey(platformName)) {
                            hMap[platformName] = 1F
                        } else {
                            hMap.put(platformName, hMap.get(platformName)!! + 1)
                        }

                    }
                    myCallback(hMap)
                } else {
                    Log.e("MError: ", "Error getting game platforms from firebase")
                }
            }
    } catch (e: Exception) {
        Log.d("get", "getUserGamePlatform failed with ", e)
    }
}

/**
 *   Endrer statusen til ett spill i firestore databasen
 */
fun changeDatabaseGameState(spillid: String, spillstate: String) {
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser!!.uid

        val docRef = db.collection("savedGames").document(uid).collection("Games").document(spillid)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    db.collection("savedGames").document(uid).collection("Games").document(spillid)
                        .update("spill state", spillstate)
                } else {
                    db.collection("savedGames").document(uid).collection("Games").document(spillid)
                        .set("spill state")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("add", "get failed with ", exception)
            }
    } catch (e: Exception) {
        Log.d("update", "changeDatabaseGameState failed with ", e)
    }
}

/**
 *   Bytter brukerens brukernavn i firestore databasen
 */
fun changeUsername(username: String) {
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser!!.uid
        val docRef = db.collection("users").document(uid)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    db.collection("users").document(uid)
                        .update("username", username)
                } else {
                    db.collection("users").document(uid)
                        .set("username")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("add", "get failed with ", exception)
            }
    } catch (e: Exception) {
        Log.d("update", "changeUsername failed with ", e)
    }
}

/**
 *   Endrer brukerens passord i firebase authentication
 */
fun changePassword(newPassword: String) {
    try {
        val user = FirebaseAuth.getInstance().currentUser
        val credential = EmailAuthProvider
            .getCredential(" ", " ")
        user?.reauthenticate(credential)?.addOnCompleteListener {
            user.updatePassword(newPassword)
        }
    } catch (e: Exception) {
        Log.d("update", "changePassword failed with ", e)
    }

}

/**
 *   Henter ett spill i brukerens database som henter lagret state(BACKLOG, DONE, PLAYING)
 */
fun getOneGameSavedPlatforms(spillid: String, myCallback: (String) -> Unit) {
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser!!.uid

        db.collection("savedGames").document(uid).collection("Games").document(spillid).get()
            .addOnSuccessListener { document ->
                val savedPlatforms: String = document.get("spill platform") as String
                myCallback(savedPlatforms)
            }
    } catch (e: Exception) {
        Log.d("get", "getOneGameSavedPlatforms failed with ", e)
    }
}

/**
 *   Sletter alle spill lagret på en bruker i firestore
 */
fun deleteAllSavedGames() {
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser!!.uid

        db.collection("savedGames").document(uid).collection("Games").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val id = document.data["spill id"].toString()
                        db.collection("savedGames").document(uid).collection("Games")
                            .document(id).delete()
                    }
                } else {
                    Log.e("MError: ", "Could not delete games from firestore")
                }
            }.addOnFailureListener { exception ->
                Log.d("delete", "delete failed with ", exception)
            }
    } catch (e: Exception) {
        Log.d("delete", "deleteAllSavedGames failed with ", e)
    }
}

/**
 *   Tar imot en spill id og sletter spillet i firestore databasen hvis den finner det
 */
fun deleteSavedGame(spillid: String) {
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser!!.uid

        val docRef = db.collection("savedGames").document(uid).collection("Games").document(spillid)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    db.collection("savedGames").document(uid).collection("Games")
                        .document(spillid).delete()
                }
            }
            .addOnFailureListener { exception ->
                Log.d("delete", "delete failed with ", exception)
            }
    } catch (e: Exception) {
        Log.d("delete", "deleteSavedGame failed with ", e)
    }
}



