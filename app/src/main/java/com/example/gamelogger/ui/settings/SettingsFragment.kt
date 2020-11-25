package com.example.gamelogger.ui.settings

import android.os.Bundle
import android.util.Log
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
                deleteAllSavedGames {
                    Log.e("Delete", "All user games deleted")
                }
                true
            }

    }


}