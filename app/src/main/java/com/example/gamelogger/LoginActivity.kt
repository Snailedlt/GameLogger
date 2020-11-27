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
    // Variabel som brukes til å sjekke hvilken form(login eller registrering) brukeren er på
    private var form = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login_screen)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        // Fjerner bar i toppen
        supportActionBar?.hide()

        // Henter login og registrering knappene og legger dem i variabel
        val loginbutton = findViewById<Button>(R.id.login_knapp)
        val signupButton = findViewById<Button>(R.id.signup_knapp)

        // Sjekker om remember me checkbox var krysset av
        rememberMeCheckboxCheck()

        // Dette skjer om log in knappen trykkes
        loginbutton.setOnClickListener {

            // Fjerner alle gamle feilmeldinger
            clearAllErrors()
            // Felt for brukernavn fjernes siden det ikke er nødvendig ved innlogging
            usernameTextField.isVisible = false
            usernameText.isVisible = false
            // Funksjon som endrer farger på knappene
            buttonColorChange("#79878F", "#263238")

            // Hvis login feltene ikke er tomme
            if (emailTextField.editText?.text.toString()
                    .isNotEmpty() && passwordTextField.editText?.text.toString().isNotEmpty()
            ) {
                // Login funksjon
                logIn(
                    emailTextField.editText?.text.toString(),
                    passwordTextField.editText?.text.toString()
                )
                form = false
            } else if (form) {
                form = false
            } else {
                //
                passwordTextField.isPasswordVisibilityToggleEnabled = false;
                emptyFormFieldsCheck()

            }
        }

        // Dette skjer om sign up knappen trykkes
        signupButton.setOnClickListener {

            // Fjerner alle gamle feilmeldinger
            clearAllErrors()
            // Felt for brukernavn dukker opp
            usernameTextField.isVisible = true
            usernameText.isVisible = true
            // Funksjon som endrer farger på knappene
            buttonColorChange("#263238", "#79878F")

            // Hvis alle feltene ikke er tomme
            if (passwordTextField.editText?.text.toString()
                    .isNotEmpty() && emailTextField.editText?.text.toString()
                    .isNotEmpty() && usernameText.editText?.text.toString().isNotEmpty()
            ) {
                // funksjon som lager bruker
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
     *   registrering funksjon som blir kalt ved sign in knappen, registrerer bruker i firebase og logger inn
     */
    private fun createUser(email: String, password: String, username: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.e("Opprettelse av bruker", "Suksess")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                    // Lagrer ekstra data i Firestore database
                    val bruker = hashMapOf(
                        "email" to emailTextField.editText?.text.toString(),
                        "username" to usernameText.editText?.text.toString()
                    )
                    db.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid)
                        .set(bruker)
                } else {
                    Log.e("Opprettelse av bruker", "Feilet" + task.exception)
                    // Viser feilmelding i UI
                    formErrorMessages(task)
                }
    }
    }

    /**
     *   Error meldinger hvis bruker skriver feil ved innlogging og registrering
     */
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
     *   Innlogging funksjon som blir kalt ved log in knappen, sjekker firebase om autentisering og logger inn
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
     *   Endrer knappene login og signup ved trykk for å enklere vise om man er på log in eller sign in
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

    /**
     *   Funksjon som fjerner alle error meldinger på LoginActivity
     */
    private fun clearAllErrors() {
        emailText.error = null
        passwordText.error = null
        usernameTextField.error = null
        passwordTextField.isPasswordVisibilityToggleEnabled = true;
    }

    /**
     *   Funksjon som sjekker om alle registrering/login feltene er fylt inn
     */
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
     *   Sjekker om bruker er logget inn ved oppstart
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
