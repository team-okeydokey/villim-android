package net.villim.villim;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by seongmin on 6/9/17.
 */

public class VillimSession {
    public final static String KEY_LOGGED_IN = "logged_in";
    public final static String KEY_NAME = "name";

    private SharedPreferences prefs;

    public VillimSession(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setLoggedIn(boolean loggedIn) {
        if (!loggedIn) {
            prefs.edit().clear().apply();
        }
        prefs.edit().putBoolean(KEY_LOGGED_IN, loggedIn);
    }

    public Boolean getLoggedIn() {
        return  prefs.getBoolean(KEY_LOGGED_IN, false);
    }

    public void setName(String usename) {
        prefs.edit().putString(KEY_NAME, usename).apply();
    }

    public String getName() {
        String usename = prefs.getString(KEY_NAME,"");
        return usename;
    }
}
