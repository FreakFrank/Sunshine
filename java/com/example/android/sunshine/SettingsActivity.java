package com.example.android.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;

import java.security.AccessController;
import java.util.prefs.PreferenceChangeListener;

import static com.example.android.sunshine.R.string.show_map;
import static com.example.android.sunshine.R.string.show_map_key;

/**
 * A {@link PreferenceActivity} that presents a set of application settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity
        implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
    Preference mapPref = findPreference("@string/show_map_key");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add 'general' preferences, defined in the XML file
        addPreferencesFromResource(R.xml.pref_general);
        Preference mapPref = findPreference(getString(R.string.show_map_key));
        if(mapPref == null){
            Log.d("Empty"," Map Preference");
        }
        mapPref.setOnPreferenceClickListener(this);
       // mapPref.setOnPreferenceClickListener(this);
        // For all preferences, attach an OnPreferenceChangeListener so the UI summary can be
        // updated when the preference changes.
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_location_key)));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.temp_key)));
      //  bindPreferenceSummaryToValue(findPreference(""));

    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);
        //preference.setOnPreferenceClickListener(this);
        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        String stringValue = value.toString();
        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Log.d("preference ",preference.getKey()+" is clicked");
        if(preference.getKey().equals(getString(R.string.show_map_key))){
            String location = "geo:0,0?q="+findPreference(getString(R.string.pref_location_key)).toString();
            Log.d("preference ",location+" is preferred");
            Intent mapLocation = new Intent(Intent.ACTION_VIEW);
            mapLocation.setData(Uri.parse(location));
            if (mapLocation.resolveActivity(getPackageManager()) != null) {
                Log.d("preference ",location+" will be opoened now");

                startActivity(mapLocation);
            }
        }
        return true;
    }
}