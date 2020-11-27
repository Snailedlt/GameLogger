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

        // Innstilling der man kan endre brukernavn
        val usernamePref: Preference = findPreference("Change_username")!!
        usernamePref.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                changeUsername(newValue.toString())
                true

            }

        // Innstilling der man kan endre passord
        val passwordPref: Preference = findPreference("Change_password")!!
        passwordPref.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                changePassword(newValue.toString())
                true

            }

        // Innstilling der man kan slette alle spill brukeren har lagret
        val clearSavedGamesButton: Preference = findPreference("clearSavedGames")!!
        clearSavedGamesButton.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                // Lager en popup
                AlertDialog.Builder(context)
                    // Hva popup skal inneholde
                    .setMessage("Do you really want to delete all your games?")
                    // Setter inn knapp ja
                    .setPositiveButton(android.R.string.yes
                    ) { _, _ ->
                        // Funksjon som sletter alle brukerens spill ved ja
                        deleteAllSavedGames()
                    }
                    // Setter inn knapp Nei som sender bruker tilbake
                    .setNegativeButton(android.R.string.no, null).show()
                true
            }


    }


}