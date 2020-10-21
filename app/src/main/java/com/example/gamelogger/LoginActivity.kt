package com.example.gamelogger

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_login_screen.*
import kotlinx.android.synthetic.main.fragment_profile.*


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login_screen)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        supportActionBar?.hide()
        var form = false

        val loginbutton = findViewById<Button>(R.id.login_knapp)
        val signupButton = findViewById<Button>(R.id.signup_knapp)

        pref = getSharedPreferences("dd", Context.MODE_PRIVATE)

        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                pref.edit().putString("dd", "true").apply()
            } else {
                pref.edit().putString("dd", "false").apply()
            }
        }

        loginbutton.setOnClickListener {
            usernameTextField.isVisible = false
            usernameText.isVisible = false

            if (emailTextField.editText?.text.toString()
                    .isNotEmpty() && passwordTextField.editText?.text.toString().isNotEmpty()
            ) {
                // Firebase Authentication
                logIn(
                    emailTextField.editText?.text.toString(),
                    passwordTextField.editText?.text.toString()
                )
                form = false
                /*ViewCompat.setBackgroundTintList(
                    signupButton,
                    ColorStateList.valueOf(Color.RED));*/
            } else if (form) {

                form = false
            } else {
                Toast.makeText(this, "Please fill in all textboxes", Toast.LENGTH_LONG).show()
            }
        }

        signupButton.setOnClickListener {
            usernameTextField.isVisible = true
            usernameText.isVisible = true


            if (passwordTextField.editText?.text.toString()
                    .isNotEmpty() && emailTextField.editText?.text.toString()
                    .isNotEmpty() && usernameText.editText?.text.toString().isNotEmpty()
            ) {

                // Firebase Authentication
                createUser(
                    emailTextField.editText?.text.toString(),
                    passwordTextField.editText?.text.toString(),
                    usernameText.editText?.text.toString()
                )
                form = true
            } else if (!form) {

                form = true
            } else {
                Toast.makeText(this, "Please fill in all textboxes", Toast.LENGTH_LONG).show()
            }

        }


    }


    private fun createUser(email: String, password: String, username: String) {

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
                    db.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
                        .set(
                            bruker
                        )

                } else {
                    Log.e("Opprettelse av bruker", "Feilet" + task.exception)
                }
            }
    }

    private fun logIn(email: String, password: String) {
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
        val test = pref.getString("dd", "false")
        if (bruker != null && test == "true") {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)


        }
    }


}
