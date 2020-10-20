package com.example.gamelogger

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.api.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_login_screen.*


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login_screen)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        supportActionBar?.hide()
        var form = false

        val loginbutton = findViewById<Button>(R.id.login_knapp)
        val signupButton = findViewById<Button>(R.id.signup_knapp)



        loginbutton.setOnClickListener {
            usernameTextField.isVisible = false
            usernameText.isVisible = false
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            /*
            if (emailTextField.editText?.text.toString().isNotEmpty() && passwordTextField.editText?.text.toString().isNotEmpty() ) {
                // Firebase Authentication
                logIn(emailTextField.editText?.text.toString(), passwordTextField.editText?.text.toString())
                form = false
            }else if (form) {

                form = false
            }  else {
                Toast.makeText(this, "Please fill in all textboxes", Toast.LENGTH_LONG).show()
            }*/
        }

        signupButton.setOnClickListener {
            usernameTextField.isVisible = true
            usernameText.isVisible = true
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
/*
            if ( passwordTextField.editText?.text.toString().isNotEmpty() && emailTextField.editText?.text.toString().isNotEmpty() && usernameText.editText?.text.toString().isNotEmpty()) {

                // Firebase Authentication
                createUser(emailTextField.editText?.text.toString(), passwordTextField.editText?.text.toString(), usernameText.editText?.text.toString())
                form = true
            } else if (!form) {

                form = true
            } else {
                Toast.makeText(this, "Please fill in all textboxes", Toast.LENGTH_LONG).show()
            }

        }*/


        }


/*
    private fun createUser(email:String, password:String, username:String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.e("Opprettelse av bruker", "Suksess")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                    // Firestore database
                    val bruker = hashMapOf(

                        "email" to emailTextField.editText?.text.toString(),
                        "password" to passwordTextField.editText?.text.toString(),
                        "username" to usernameText.editText?.text.toString()
                    )
                    db.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid).set(bruker as Map<String, Any>)

                } else {
                    Log.e("Opprettelse av bruker", "Feilet" + task.exception)
                }
            }
    }
    private fun logIn(email:String, password:String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.e("Login var", "Suksess")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.e("Login har", "Feilet" + task.exception)
                }
            }
    }



    override fun onStart() {
        super.onStart()
        val bruker = auth.currentUser

        if (bruker != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


*/
    }
}