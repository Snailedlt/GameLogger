package com.example.gamelogger.firebase

import android.util.Log
import com.example.gamelogger.classes.Game
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.ArrayList

/**
 *   Adds a new game to the firestore database
 **/
fun addSavedGame(game: Game) {
    try {
        val spillid = game.id.toString()
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser!!.uid // Gets the user id

        // Creates a document reference to the database
        val docRef = db.collection("savedGames").document(uid).collection("Games").document(spillid)

        // Creates a hashmap of data to send to database
        val nestedData = hashMapOf(
            "spill id" to game.id.toString(),
            "spill state" to game.state.toString(),
            "spill platform" to game.chosenPlatform,
            "spill lagt til" to game.dateAdded
        )

        // Gets document reference and updates or sets the data into the database
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    db.collection("savedGames").document(uid).collection("Games").document(spillid)
                        .update(nestedData as Map<String, String?>)
                } else {
                    db.collection("savedGames").document(uid).collection("Games").document(spillid)
                        .set(nestedData as Map<String, String?>)
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
 *   Gets the current users username and sends it back in a callback
 * */
fun getUser(myCallback: (String) -> Unit) {
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser!!.uid // Gets the user id

        // Creates a document reference to the database
        val docRef = db.collection("users").document(uid)

        // Gets document reference and updates or sets the data into the database
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
 *   Gets all games in a users database, puts them in a string with all information needed
 *   and then sends it back in a callback
 * */
fun getUserGames(myCallback: (MutableList<String>) -> Unit) {
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser!!.uid // Gets the user id

        // Gets document reference and updates or sets the data into the database
        db.collection("savedGames").document(uid).collection("Games").get()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    // Initialises the list
                    val list = ArrayList<String>()
                    for (document in task.result!!) {
                        // add content to the list
                        list.add(document.data["spill state"].toString())
                        list.add(document.data["spill platform"].toString())
                        list.add(document.data["spill lagt til"].toString())
                        list.add(document.data["spill id"].toString())
                    }
                    // Sends list back
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
 *   Gets ands sorts what states the users games have stored in the database,
 *   adds them to a floatarray and sends them back
 */
fun getUserGameState(myCallback: (FloatArray) -> Unit) {
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser!!.uid // Gets the user id
        val stateArray = FloatArray(3)

        db.collection("savedGames").document(uid).collection("Games").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var backlog = 0F
                    var done = 0F
                    var playing = 0F
                    // Loops through all the users game documents
                    for (document in task.result!!) {
                        // Checks what state game is in and adds it
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
 *   Gets what platforms the users games have been stored as in the database and counts the quantity
 */
fun getUserGamePlatform(myCallback: (HashMap<String, Float>) -> Unit) {
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser!!.uid // Gets the user id

        db.collection("savedGames").document(uid).collection("Games").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val hMap: HashMap<String, Float> = hashMapOf<String, Float>()
                    // Loops through all the users game documents
                    for (document in task.result!!) {
                        val platformName = document.data["spill platform"].toString()
                        // if the hashmap doesn't contain platform then put it in with value of 1
                        if (!hMap.containsKey(platformName)) {
                            hMap[platformName] = 1F
                        } else { // if the hashmap does contain platform then add 1 value
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
 *   Changes the state of one game in the database
 */
fun changeDatabaseGameState(spillid: String, spillstate: String) {
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser!!.uid // Gets the user id

        val docRef = db.collection("savedGames").document(uid).collection("Games").document(spillid)

        // Gets document reference and updates or sets the data into the database
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
 *   Changes the current users username in the firestore database
 */
fun changeUsername(username: String) {
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser!!.uid // Gets the user id
        val docRef = db.collection("users").document(uid)

        // Gets document reference and updates or sets the data into the database
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
 *   Changes the current users password in the firestore database
 */
fun changePassword(newPassword: String) {
    try {
        val user = FirebaseAuth.getInstance().currentUser // Gets the current user
        val credential = EmailAuthProvider
            .getCredential(" ", " ")
        // Updates the password in Firebase Authentication
        user?.reauthenticate(credential)?.addOnCompleteListener {
            user.updatePassword(newPassword)
        }
    } catch (e: Exception) {
        Log.d("update", "changePassword failed with ", e)
    }

}

/**
 *   Deletes all games stored on a user in firestore database
 */
fun deleteAllSavedGames() {
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser!!.uid // Gets the user id

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
 *   Deletes one game
 */
fun deleteSavedGame(spillid: String) {
    try {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val uid = auth.currentUser!!.uid // Gets the user id

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



