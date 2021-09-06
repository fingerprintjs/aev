package com.fingerprintjs.android.pro.playgroundpro.settings


import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.fingerprintjs.android.pro.playgroundpro.R


class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
    }
}