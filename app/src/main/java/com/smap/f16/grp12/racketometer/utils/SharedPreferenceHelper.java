package com.smap.f16.grp12.racketometer.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.smap.f16.grp12.racketometer.BaseActivity;

/**
 * Shared preferences helper to handle {@link SharedPreferences} for the application.
 */
public class SharedPreferenceHelper {
    private final static String SHARED_PREFERENCES_ID
            = "com.smap.f16.grp12.racketometer.BaseActivity.SHARED_PREFERENCES_ID";
    private final static String SHARED_PREFERENCES_NAME
            = "com.smap.f16.grp12.racketometer.BaseActivity.SHARED_PREFERENCES_NAME";
    private final static String SHARED_PREFERENCES_CLUB
            = "com.smap.f16.grp12.racketometer.BaseActivity.SHARED_PREFERENCES_CLUB";

    private final SharedPreferences preferences;

    public SharedPreferenceHelper(BaseActivity context) {
        preferences = context.getSharedPreferences(SHARED_PREFERENCES_ID, Context.MODE_PRIVATE);
    }

    /**
     * Get saved name.
     * @return The name or empty string.
     */
    public String getName() {
        return preferences.getString(SHARED_PREFERENCES_NAME, "");
    }

    /**
     * Get saved club.
     * @return The club or empty string.
     */
    public String getClub() {
        return preferences.getString(SHARED_PREFERENCES_CLUB, "");
    }

    /**
     * Set saved name.
     * @param name The name to save.
     */
    public void setName(String name) {
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        preferenceEditor.putString(SHARED_PREFERENCES_NAME, name);
        preferenceEditor.apply();
    }

    /**
     * Set saved club.
     * @param club The club to save.
     */
    public void setClub(String club) {
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        preferenceEditor.putString(SHARED_PREFERENCES_CLUB, club);
        preferenceEditor.apply();
    }
}
