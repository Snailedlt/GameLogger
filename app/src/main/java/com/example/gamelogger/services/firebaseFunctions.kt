package com.example.gamelogger.services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Legger til spill i Firestore databasen
 * addSavedGame(Spillid, spillstate)
 * */
fun addSavedGame(spillid: String, spillstate: String, spillPlat: String) {

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
                    .set(nestedData)
                Log.d("add", "Added more game to firebase?")
            }
        }
        .addOnFailureListener { exception ->
            Log.d("add", "get failed with ", exception)
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
}

/**
 * Henter Spill som er lagret på brukeren sin database
 * */
fun getUserGames(myCallback: (MutableList<String>) -> Unit) {

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
                    list.add(state)
                    list.add(id)
                }
                myCallback(list)
            } else {
                Log.e("MError: ", "Error getting games from firebase")
            }
        }
}

/**
 *   getUserGameState {
 *       var backlog: Float = it[1]
 *       var playing: Float = it[0]
 *       var planning: Float = it[2]
 *   }
 */
fun getUserGameState(myCallback: (FloatArray) -> Unit) {

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
}

fun getUserGamePlatform(myCallback: (FloatArray) -> Unit) {

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
                        "PS4" -> {
                            ps4++
                        }
                        "XB1" -> {
                            xb1++
                        }
                        "PC" -> {
                            pc++
                        }
                        "Switch" -> {
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
                platArray[2] = switch
                platArray[2] = android
                myCallback(platArray)
            } else {
                Log.e("MError: ", "Error getting game platforms from firebase")
            }
        }
}
fun changeDatabaseGameState(spillid: String, spillstate: String) {

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
}



