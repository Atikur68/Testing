package com.flarze.hashstash.data.instagram_login;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    public static final String APP_PREFERENCES_FILE_NAME = "userdata";
    public static final String USER_ID = "userID";
    public static final String TOKEN = "token";
    public static final String PROFILE_PIC = "profile_pic";
    public static final String USER_NAME = "username";
    public static final String NAME = "name";
    public static final String USER_PASSWORD = "password";
    public static final String USER_PHONE = "phone";
    public static final String USER_EMAIL = "email";
    public static final String USER_COUNTRY = "country";
    public static final String TABLE_ID = "id";
    public static final String HASHORSTASH = "hashOrStash";
    public static final String LOCATION_ID = "locationId";
    public static final String WELCOME_STATUS = "welcome_status";


    private SharedPreferences preferences;

    public AppPreferences(Context context) {
        this.preferences = context.getSharedPreferences(APP_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
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
