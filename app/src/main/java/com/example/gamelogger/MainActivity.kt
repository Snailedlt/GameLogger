package com.example.gamelogger

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration2: AppBarConfiguration

    // Initialiserer Firebase autentisering
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Lager autentiseringsvariabel til Firebase
        auth = FirebaseAuth.getInstance()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_gameslist, R.id.navigation_profile, R.id.navigation_gameslist))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController2 = navHostFragment.navController
        appBarConfiguration2 = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController2, appBarConfiguration2)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.overflow_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //return super.onOptionsItemSelected(item)
        // Handle item selection
        return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration2)
                || super.onSupportNavigateUp()
    }

    fun onSignOut(item: MenuItem) {
        val bruker = auth.currentUser
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}