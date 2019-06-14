package com.example.flarzehashstash.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

public class SharedPref {

    private static SharedPreferences preferences;
    public static final String KEY_BOARD_HEIGHT = "keyboard_height";

    private SharedPref() {
    }

    public static void init(Context context) {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
        }
    }

    public static boolean write(String key, Object value) {
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        editor.putString(key, gson.toJson(value));
        return editor.commit();
    }

    public static boolean write(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(key, value);

        return editor.commit();
    }

    public static boolean write(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(key, value);

        return editor.commit();
    }

    public static boolean write(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(key, value);

        return editor.commit();
    }

    public static boolean write(String key, long value) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putLong(key, value);

        return editor.commit();
    }

    public static String read(String key) {
        return preferences.getString(key, "");
    }

    public static long readLong(String key) {
        return preferences.getLong(key, 0);
    }

    public static int readInt(String key) {
        return preferences.getInt(key, 0);
    }
    public static boolean readBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public static boolean readBooleanDefaultTrue(String key){
        return preferences.getBoolean(key, true);
    }

    public static <GenericClass> GenericClass readObject(String key, Class<GenericClass> classType) {
        if (preferences.contains(key)) {
            Gson gson = new Gson();
            return gson.fromJson(preferences.getString(key, ""), classType);
        }
        return null;
    }

    public static boolean contains(String key) {
        return preferences.contains(key);
    }

}