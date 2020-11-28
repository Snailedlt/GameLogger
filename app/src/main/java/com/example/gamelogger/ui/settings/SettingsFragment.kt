package com.example.gamelogger.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.gamelogger.R
import com.example.gamelogger.services.changePassword
import com.example.gamelogger.services.changeUsername
import com.example.gamelogger.services.deleteAllSavedGames


class SettingsFragment : PreferenceFragmentCompat() {

    /**
     *   Funksjon som lager og fyller inn innstillingene
     */
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        // Setting where you can change username
        val usernamePref: Preference = findPreference("Change_username")!!
        usernamePref.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                changeUsername(newValue.toString())
                true

            }

        // Setting where you can change password
        val passwordPref: Preference = findPreference("Change_password")!!
        passwordPref.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                changePassword(newValue.toString())
                true

            }

        // Setting where you can delete all games current user has saved
        val clearSavedGamesButton: Preference = findPreference("clearSavedGames")!!
        clearSavedGamesButton.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                // Creates a popup
                AlertDialog.Builder(context)
                    // What popup contains
                    .setMessage("Do you really want to delete all your games?")
                    // Adds a yes button
                    .setPositiveButton(android.R.string.yes
                    ) { _, _ ->
                        // Function that deletes all of users games
                        deleteAllSavedGames()
                    }
                    // Adds a no button that sends user back
                    .setNegativeButton(android.R.string.no, null).show()
                true
            }


    }


}