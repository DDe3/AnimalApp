package com.example.practica.presentacion.fragmentos

import android.os.Bundle
import android.widget.Toast
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.practica.R


class PreferenciasFragment : PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }


    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when(preference.key) {
            "avistamiento" ->
            {
                Toast.makeText(activity?.applicationContext,"Avistamiento",Toast.LENGTH_SHORT).show()
                return true
            }
            "traductor" ->
            {
                Toast.makeText(activity?.applicationContext,"Traductor",Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return false
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        TODO("Not yet implemented")
    }


}