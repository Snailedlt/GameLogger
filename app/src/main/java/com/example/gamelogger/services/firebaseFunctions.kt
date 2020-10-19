package com.example.gamelogger.services

import android.content.Intent
import android.util.Log
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import com.example.gamelogger.LoginActivity
import com.example.gamelogger.MainActivity
import com.example.gamelogger.R
import com.example.gamelogger.ui.profile.ProfileFragment.Companion.TAG
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_login_screen.*

fun addSavedGame(spillid: String, spillstate: String) {

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    val uid = auth.currentUser!!.uid

    val docRef = db.collection("savedGames").document(uid)

    val nestedData = hashMapOf(
        "spill id" to spillid,
        "spill state" to spillstate
    )

    nestedData[spillid] = nestedData.toString()

    docRef.get()
        .addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                db.collection("savedGames").document(uid)
                    .update(nestedData as Map<String, Any>)
                Log.d(TAG, "Added game to firebase?")
            } else {
                db.collection("savedGames").document(uid)
                    .set(nestedData as Map<String, Any>)
                Log.d(TAG, "Added more game to firebase?")
            }
        }
        .addOnFailureListener { exception ->
            Log.d(TAG, "get failed with ", exception)
        }
}
