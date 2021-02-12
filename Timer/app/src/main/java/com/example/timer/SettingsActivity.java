package com.example.timer;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    SettingsFragment settingsFragment;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        settingsFragment = new SettingsFragment();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        editor = sharedPreferences.edit();

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        final ListPreference fontSizePreference = settingsFragment.findPreference("fontSize");
//        assert fontSizePreference != null;
//
////        fontSizePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
////            @Override
////            public boolean onPreferenceChange(Preference preference, Object newValue) {
////                switch (fontSizePreference.findIndexOfValue(newValue.toString())) {
////                    case 0: {
////                        editor.putInt("font", -2);
////                        break;
////                    }
////                    case 1: {
////                        editor.putInt("font", 0);
////                        break;
////                    }
////                    case 2: {
////                        editor.putInt("font", 4);
////                        break;
////                    }
////                }
////                editor.apply();
////                finish();
////                startActivity(new Intent(SettingsActivity.this, SettingsActivity.this.getClass()));
////                return true;
////
////            }
////        });
//    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        ListPreference listPreference = settingsFragment.findPreference("fontSize");
        if (key.equals("appTheme")) {
            if (sharedPreferences.getBoolean("appTheme", true)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.this.getClass()));
        }

        if (key.equals("appLocale")) {
            if (sharedPreferences.getBoolean("appLocale", true)) {
                LocaleHelper.setLocale(getBaseContext(), "en");
            } else {
                LocaleHelper.setLocale(getBaseContext(), "ru");
            }
            finish();
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.this.getClass()));
        }

        if (key.equals("fontSize")) {
            String fontSize = sharedPreferences.getString("fontSize", "");
            if (fontSize.equals(getResources().getString(R.string.small))) {
                editor.putInt("font", -2);
            } else if (fontSize.equals(getResources().getString(R.string.medium))) {
                editor.putInt("font", 0);
            } else if (fontSize.equals(getResources().getString(R.string.large))) {
                editor.putInt("font", 4);
            }
            editor.apply();
            finish();
            startActivity(new Intent(SettingsActivity.this, SettingsActivity.this.getClass()));
        }
    }



    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference clearData = findPreference("clearData");
            clearData.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new AlertDialog.Builder(getContext())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(R.string.reset_dialog_title)
                            .setMessage(R.string.reset_dialog_message)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DbHelper dbHelper = new DbHelper(getContext());
                                    dbHelper.clearAll();
                                    dialog.dismiss();
                                }

                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                    return true;
                }
            });
        }
    }
}