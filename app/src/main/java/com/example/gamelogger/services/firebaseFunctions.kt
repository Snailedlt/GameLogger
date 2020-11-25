package com.example.gamelogger.services

import android.util.Log
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Legger til spill i Firestore databasen
 * addSavedGame(Spillid, spillstate)
 * */
fun addSavedGame(spillid: String, spillstate: String, spillPlat: String) {
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser!!.uid

        val docRef = db.collection("savedGames").document(uid).collection("Games").document(spillid)

        val nestedData = hashMapOf(
            "spill id" to spillid,
            "spill state" to spillstate,
            "spill platform" to spillPlat
        )

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    db.collection("savedGames").document(uid).collection("Games").document(spillid)
                        .update(nestedData as Map<String, Any>)
                    Log.d("add", "Added game to firebase?")
                } else {
                    db.collection("savedGames").document(uid).collection("Games").document(spillid)
                        .set(nestedData as Map<String, Any>)
                    Log.d("add", "Added more game to firebase?")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("add", "get failed with ", exception)
            }
    } catch (e: Exception) {
        Log.d("addSavedGame", "addSavedGame failed with ", e)
    }
}

/**
 * Fanger opp pålogget bruker sitt username, brukes på denne måten
 * getUser() {
 *    val brukernavn = it
 *    Kode som trenger brukernavn
 * }
 * */
fun getUser(myCallback: (String) -> Unit) {
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser!!.uid

        val docRef = db.collection("users").document(uid)

        docRef.get()
            .addOnSuccessListener { document ->
                var brukernavn: String = document.get("username") as String
                Log.d("get", "Palogget bruker: $brukernavn")

                myCallback(brukernavn)

            }
            .addOnFailureListener { exception ->
                Log.d("get", "get failed with ", exception)
            }
    } catch (e: Exception) {
        Log.d("getUser", "getUser failed with ", e)
    }
}

/**
 * Henter Spill som er lagret på brukeren sin database
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
                        list.add(state)
                        list.add(platform)
                        list.add(id)

                    }
                    myCallback(list)
                } else {
                    Log.e("MError: ", "Error getting games from firebase")
                }
            }
    } catch (e: Exception) {
        Log.d("getUserGames", "getUserGames failed with ", e)
    }

}

/**
 *   Henter hvilken statuser spillene brukeren har lagret i databasen
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
        Log.d("getUserGameState", "getUserGameState failed with ", e)
    }
}

/**
 *   Henter hvilken plattformer spillene brukeren har lagret i databasen
 */
fun getUserGamePlatform(myCallback: (FloatArray) -> Unit) {
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser!!.uid
        val platArray = FloatArray(5)

        db.collection("savedGames").document(uid).collection("Games").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var ps4 = 0F
                    var xb1 = 0F
                    var pc = 0F
                    var switch = 0F
                    var android = 0F
                    for (document in task.result!!) {
                        when (document.data["spill platform"].toString()) {
                            "PlayStation 4" -> {
                                ps4++
                            }
                            "Xbox One" -> {
                                xb1++
                            }
                            "PC" -> {
                                pc++
                            }
                            "Nintendo Switch" -> {
                                switch++
                            }
                            "Android" -> {
                                android++
                            }
                        }
                    }
                    platArray[0] = ps4
                    platArray[1] = xb1
                    platArray[2] = pc
                    platArray[3] = switch
                    platArray[4] = android
                    myCallback(platArray)
                } else {
                    Log.e("MError: ", "Error getting game platforms from firebase")
                }
            }
    } catch (e: Exception) {
        Log.d("getUserGamePlatform", "getUserGamePlatform failed with ", e)
    }
}

/**
 *   Endrer statusen til ett spill i databasen
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
                    Log.d("add", "Added game to firebase?")
                } else {
                    db.collection("savedGames").document(uid).collection("Games").document(spillid)
                        .set("spill state")
                    Log.d("add", "Added more game to firebase?")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("add", "get failed with ", exception)
            }
    } catch (e: Exception) {
        Log.d("changeDatabaseGameState", "changeDatabaseGameState failed with ", e)
    }
}

/**
 *   Bytter brukernavn i databasen
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
                    Log.d("add", "Changed username")
                } else {
                    db.collection("users").document(uid)
                        .set("username")
                    Log.d("add", "Added username")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("add", "get failed with ", exception)
            }
    } catch (e: Exception) {
        Log.d("changeUsername", "changeUsername failed with ", e)
    }
}

/**
 *   Endrer passord i firebase authentication
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
        Log.d("changePassword", "changePassword failed with ", e)
    }

}

/**
 *   getOneGameSavedPlatforms(game.id.toString()){
 *      Log.i("Spill lagret her: ", it.toString())
 *   }
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
        Log.d("getOneGamSaPlats", "getOneGameSavedPlatforms failed with ", e)
    }
}

fun deleteAllSavedGames(myCallback: (String) -> Unit) {
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser!!.uid

        db.collection("savedGames").document(uid).collection("Games").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        val id = document.data["spill id"].toString()
                        val docRef = db.collection("savedGames").document(uid).collection("Games")
                            .document(id)
                        docRef.delete()
                    }
                } else {
                    Log.e("MError: ", "Could not delete games from firestore")
                }
            }.addOnFailureListener { exception ->
                Log.d("delete", "delete failed with ", exception)
            }
    } catch (e: Exception) {
        Log.d("deleteAllSavedGames", "deleteAllSavedGames failed with ", e)
    }
}

fun deleteSavedGame(spillid: String) {
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser!!.uid

        val docRef = db.collection("savedGames").document(uid).collection("Games").document(spillid)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val docRef = db.collection("savedGames").document(uid).collection("Games")
                        .document(spillid)
                    docRef.delete()
                }
            }
            .addOnFailureListener { exception ->
                Log.d("delete", "delete failed with ", exception)
            }
    } catch (e: Exception) {
        Log.d("deleteSavedGame", "deleteSavedGame failed with ", e)
    }
}



