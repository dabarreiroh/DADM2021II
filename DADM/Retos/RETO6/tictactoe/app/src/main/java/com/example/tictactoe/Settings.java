package com.example.tictactoe;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;

/**
 * Created by vr on 10/11/17.
 */

public class Settings extends PreferenceActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences( getBaseContext() );

        final ListPreference diffPref = (ListPreference) findPreference( "difficulty_level" );
        String diff = prefs.getString( "difficulty_level", getResources().getString( R.string.difficulty_expert ) );
        diffPref.setSummary( diff );
        diffPref.setOnPreferenceChangeListener( new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                diffPref.setSummary( o.toString() );
                /*SharedPreferences.Editor ed = prefs.edit();
                ed.putString( "difficulty_level", o.toString() );
                ed.commit();*/
                return true;
            }
        });

        final EditTextPreference victoryPref = (EditTextPreference) findPreference( "victory_message" );
        String victoryMessage = prefs.getString( "victory_message", getResources().getString( R.string.result_human_wins ) );
        victoryPref.setSummary( victoryMessage );
        victoryPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                victoryPref.setSummary( o.toString() );
                /*SharedPreferences.Editor ed = prefs.edit();
                ed.putString( "victory_message", o.toString() );
                ed.commit();*/
                return true;
            }
        });

    }

}
