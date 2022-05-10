package com.example.testingappproject.settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.testingappproject.R;

public class FragmentSettings extends PreferenceFragmentCompat {
    private SharedPreferences preferences;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        Preference preference = findPreference("dark_theme");
        preference.setOnPreferenceChangeListener((preference1, newValue) -> {
            if (newValue.equals(true)) {
                //set dark theme
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                //set light theme
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            return true;
        });
    }
}