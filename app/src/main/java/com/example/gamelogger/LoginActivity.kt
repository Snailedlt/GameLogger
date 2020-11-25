package com.example.gamelogger

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_login_screen.*


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var pref: SharedPreferences
    private var form = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login_screen)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        supportActionBar?.hide()

        val loginbutton = findViewById<Button>(R.id.login_knapp)
        val signupButton = findViewById<Button>(R.id.signup_knapp)

        // Sjekker om remember me var krysset av
        rememberMeCheckboxCheck()

        loginbutton.setOnClickListener {
            clearAllErrors()
            usernameTextField.isVisible = false
            usernameText.isVisible = false
            buttonColorChange("#79878F", "#263238")


            if (emailTextField.editText?.text.toString()
                    .isNotEmpty() && passwordTextField.editText?.text.toString().isNotEmpty()
            ) {
                // Firebase Authentication
                logIn(
                    emailTextField.editText?.text.toString(),
                    passwordTextField.editText?.text.toString()
                )
                form = false
            } else if (form) {
                form = false
            } else {
                passwordTextField.isPasswordVisibilityToggleEnabled = false;
                emptyFormFieldsCheck()

            }
        }

        signupButton.setOnClickListener {
            clearAllErrors()
            usernameTextField.isVisible = true
            usernameText.isVisible = true
            buttonColorChange("#263238", "#79878F")

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
                passwordTextField.isPasswordVisibilityToggleEnabled = false;
                emptyFormFieldsCheck()

            }
        }
    }

    /**
     *   registrering funksjon som blir kalt ved sign in knappen og registrerer bruker i firebase og logger inn
     */
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
                        "username" to usernameText.editText?.text.toString()
                    )
                    db.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
                        .set(bruker)
                } else {
                    Log.e("Opprettelse av bruker", "Feilet" + task.exception)
                    formErrorMessages(task)
                }
    }
    }

    private fun formErrorMessages(task: Task<AuthResult?>) {
        //Hvis authentication feiler
        if (!task.isSuccessful) {
            when (task.exception?.localizedMessage!!) {
                "The email address is badly formatted." -> {
                    emailText.error = "The email address is badly formatted"
                }
                "The given password is invalid. [ Password should be at least 6 characters ]" -> {
                    passwordText.error = "Password should be at least 6 characters"
                }
                "There is no user record corresponding to this identifier. The user may have been deleted." -> {
                    passwordText.error = "Incorrect login credentials"
                }
                "The email address is already in use by another account." -> {
                    passwordText.error = "The email address is already in use"
                }
            }
        }
    }

    /**
     *   Innlogging funksjon som blir kalt ved log in knappen og sjekker firebase om autentisering
     */
    private fun logIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.e("Login var", "Suksess")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.e("Login har", "Feilet" + task.exception)
                    formErrorMessages(task)
                }
            }
    }
    /**
     *   Endrer knappene login og signup ved trykk for å enklere vise hvilken form man er på
     */
    private fun buttonColorChange(signUpColor: String, loginColor: String) {
        val loginbutton = findViewById<Button>(R.id.login_knapp)
        val signupButton = findViewById<Button>(R.id.signup_knapp)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            signupButton.background.setTint(Color.parseColor(signUpColor))
            loginbutton.background.setTint(Color.parseColor(loginColor))
        }
    }
    /**
     *   Sjekker om remember me var krysset av
     */
    private fun rememberMeCheckboxCheck() {
        pref = getSharedPreferences("dd", Context.MODE_PRIVATE)
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                pref.edit().putString("dd", "true").apply()
            } else {
                pref.edit().putString("dd", "false").apply()
            }
        }
    }

    private fun clearAllErrors() {
        emailText.error = null
        passwordText.error = null
        usernameTextField.error = null
        passwordTextField.isPasswordVisibilityToggleEnabled = true;
    }

    private fun emptyFormFieldsCheck() {
        if(passwordTextField.editText?.text.toString().isEmpty()) {
            passwordText.error = "You need to enter an password"
        }
        if(emailTextField.editText?.text.toString().isEmpty()) {
            emailText.error = "You need to enter an email"
        }
        if(usernameText.editText?.text.toString().isEmpty()) {
            usernameTextField.error = "You need to enter an username"
        }
    }

    /**
     *   Sjekker om bruker er logget inn allerede ved oppstart
     */
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
