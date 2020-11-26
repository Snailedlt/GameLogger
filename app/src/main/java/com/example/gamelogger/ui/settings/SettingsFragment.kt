package com.example.gamelogger.ui.settings

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.gamelogger.R
import com.example.gamelogger.services.changePassword
import com.example.gamelogger.services.changeUsername
import com.example.gamelogger.services.deleteAllSavedGames


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val usernamePref: Preference = findPreference("Change_username")!!

        usernamePref.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                changeUsername(newValue.toString())
                true

            }
        val passwordPref: Preference = findPreference("Change_password")!!

        passwordPref.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, newValue ->
                changePassword(newValue.toString())
                true

            }

        val clearSavedGamesButton: Preference = findPreference("clearSavedGames")!!
        clearSavedGamesButton.onPreferenceClickListener =
            Preference.OnPreferenceClickListener {
                AlertDialog.Builder(context)
                    .setMessage("Do you really want to delete all your games?")
                    .setPositiveButton(android.R.string.yes
                    ) { _, _ ->
                        deleteAllSavedGames()
                    }
                    .setNegativeButton(android.R.string.no, null).show()

                true
            }


    }


}