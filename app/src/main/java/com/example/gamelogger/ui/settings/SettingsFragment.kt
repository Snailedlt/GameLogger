package com.example.gamelogger.ui.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.gamelogger.R
import com.example.gamelogger.services.changePassword
import com.example.gamelogger.services.changeUsername


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

    }


}