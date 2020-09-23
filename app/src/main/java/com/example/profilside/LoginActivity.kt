package com.example.profilside

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.fragment_login_screen.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_login_screen)
        supportActionBar?.hide()

        val button = findViewById<Button>(R.id.login_knapp)
        val signinButton = findViewById<Button>(R.id.signin_knapp)

        button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)

            startActivity(intent)
        }
        var test = 12
        signinButton.setOnClickListener {

            if (test == 14) {
                val intent = Intent(this, MainActivity::class.java)

                startActivity(intent)
            }
            test = 14

            username.isVisible=true
            email.hint = "Email"
            login_knapp.setBackgroundColor(Color.parseColor("#6200EE"))
            signin_knapp.setBackgroundColor(Color.parseColor("#3700B3"))


        }


    }

}