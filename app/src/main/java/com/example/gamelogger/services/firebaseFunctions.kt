package com.example.gamelogger.services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Legger til spill i Firestore databasen
 * addSavedGame(Spillid, spillstate)
 * */
fun addSavedGame(spillid: String, spillstate: String) {

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    val uid = auth.currentUser!!.uid

    val docRef = db.collection("savedGames").document(uid).collection("Games").document(spillid)

    val nestedData = hashMapOf(
        "spill id" to spillid,
        "spill state" to spillstate
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
fun getUserGames(myCallback: (List<String>) -> Unit) {

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    val uid = auth.currentUser!!.uid

    db.collection("savedGames").document(uid).collection("Games").get()
        .addOnSuccessListener { result ->

            val spill = result!!.map { snapshot ->
                snapshot["spill id"].toString()
            }
            Log.d("get", "Spill som brukeren har lagret i sin database: $spill")
            myCallback(spill)

        }
        .addOnFailureListener { exception ->
            Log.d("get", "get failed with ", exception)
        }
}



