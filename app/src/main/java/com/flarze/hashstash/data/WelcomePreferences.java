package com.flarze.hashstash.data;

import android.content.Context;
import android.content.SharedPreferences;

public class WelcomePreferences {
    public static final String WELCOME_PREFERENCES_FILE_NAME = "WELCOME_userdata";
    public static final String WELCOME_STATUS = "welcome_status";



    private SharedPreferences preferences;

    public WelcomePreferences(Context context) {
        this.preferences = context.getSharedPreferences(WELCOME_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    public String getString(String key) {
        return preferences.getString(key, null);
    }

    public void putString(String key, String value)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public void putBoolean(String key, boolean value)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }


    public void clear()
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
