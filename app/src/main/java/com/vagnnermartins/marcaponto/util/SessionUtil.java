package com.vagnnermartins.marcaponto.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionUtil {

    private final static String PREFERENCES = "map";

    public static void addValue(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCES, 0);
        settings.edit().putBoolean(key, value).commit();
    }

    public static boolean getValue(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES, 0);
        return preferences.getBoolean(key, true);
    }
}