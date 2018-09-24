package com.etrack.feedback;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by Husain on 10/1/17.
 * OCS infotech Ltd
 */

public class SharedPreferenceHelper {
    private final static String PREF_FILE = "PREFS";
    public final static String PREF_APP_COMP_NAME = "com_name";
    public final static String PREF_APP_LANG = "pref_app_lang";

    /**
     * Set a string shared preference
     *
     * @param key   - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setSharedPreferenceString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Set a integer shared preference
     *
     * @param key   - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setSharedPreferenceInt(Context context, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * Set a Boolean shared preference
     *
     * @param key   - Key to set shared preference
     * @param value - Value for the key
     */
    public static void setSharedPreferenceBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * Get a string shared preference
     *
     * @param key      - Key to look up in shared preferences.
     * @param defValue - Default value to be returned if shared preference isn't found.
     * @return value - String containing value of the shared preference if found.
     */
    public static String getSharedPreferenceString(Context context, String key, String defValue) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        return settings.getString(key, defValue);
    }

    public static boolean clearSharedExptToken(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREF_FILE, 0);
        Map<String, ?> prefs = settings.getAll();
        for (Map.Entry<String, ?> prefToReset : prefs.entrySet()) {
            if (prefToReset.getKey().equals("token") || prefToReset.getKey().equals("tokenadded")) {
            } else {
                settings.edit().remove(prefToReset.getKey()).commit();
            }
        }
        return true;
    }

}