package net.villim.villim;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static net.villim.villim.VillimKeys.KEY_CURRENCY_PREFERENCE;
import static net.villim.villim.VillimKeys.KEY_EMAIL;
import static net.villim.villim.VillimKeys.KEY_FIRSTNAME;
import static net.villim.villim.VillimKeys.KEY_FULLNAME;
import static net.villim.villim.VillimKeys.KEY_ID;
import static net.villim.villim.VillimKeys.KEY_LANGUAGE_PREFERENCE;
import static net.villim.villim.VillimKeys.KEY_LASTNAME;
import static net.villim.villim.VillimKeys.KEY_PROFILE_PIC_URL;
import static net.villim.villim.VillimKeys.KEY_PUSH_PREFERENCE;
import static net.villim.villim.VillimKeys.KEY_ROOM_ID;
import static net.villim.villim.VillimKeys.KEY_USER_STATUS;

/**
 * Created by seongmin on 6/9/17.
 */

public class VillimSession {
    public final static String KEY_LOGGED_IN = "logged_in";

    private SharedPreferences prefs;

    public VillimSession(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /* Logged in */
    public void setLoggedIn(boolean loggedIn) {
        if (!loggedIn) {
            prefs.edit().clear().apply();
        }
        prefs.edit().putBoolean(KEY_LOGGED_IN, loggedIn).apply();
    }

    public boolean getLoggedIn() {
        return prefs.getBoolean(KEY_LOGGED_IN, false);
    }

    /* Id */
    public void setId(int id) {
        prefs.edit().putInt(KEY_ID, id).apply();
    }

    public int getId() {
        int id = prefs.getInt(KEY_ID,0);
        return id;
    }


    /* Fullname */
    public void setFullName(String fullname) {
        prefs.edit().putString(KEY_FULLNAME, fullname).apply();
    }

    public String getFullName() {
        String fullname = prefs.getString(KEY_FULLNAME,"");
        return fullname;
    }

    /* Firstname */
    public void setFirstName(String fullname) {
        prefs.edit().putString(KEY_FIRSTNAME, fullname).apply();
    }

    public String getFirstName() {
        String firstname = prefs.getString(KEY_FIRSTNAME,"");
        return firstname;
    }

    /* Lastname */
    public void setLastName(String fullname) {
        prefs.edit().putString(KEY_LASTNAME, fullname).apply();
    }

    public String getLastName() {
        String lastname = prefs.getString(KEY_LASTNAME,"");
        return lastname;
    }

    /* Email */
    public void setEmail(String email) {
        prefs.edit().putString(KEY_EMAIL, email).apply();
    }

    public String getEmail() {
        String email = prefs.getString(KEY_EMAIL,"");
        return email;
    }

    /* Profile pic url */
    public void setProfilePicUrl(String url) {
        prefs.edit().putString(KEY_PROFILE_PIC_URL, url).apply();
    }

    public String getProfilePicUrl() {
        String url = prefs.getString(KEY_PROFILE_PIC_URL,"");
        return url;
    }

    /* Rent status */
    public void setStatus(int status) {
        prefs.edit().putInt(KEY_USER_STATUS, status).apply();
    }

    public int getStatus() {
        int status = prefs.getInt(KEY_USER_STATUS,0);
        return status;
    }

    /* Room id */
    public void setRoomId(int roomId) {
        prefs.edit().putInt(KEY_ROOM_ID, roomId).apply();
    }

    public int getRoomId() {
        int roomId = prefs.getInt(KEY_ROOM_ID,0);
        return roomId;
    }

    /* Push Notifications */
    public void setPushPref(boolean pushPref) {
        prefs.edit().putBoolean(KEY_PUSH_PREFERENCE, pushPref).apply();
    }

    public boolean getPushPref() {
        boolean pushPref = prefs.getBoolean(KEY_PUSH_PREFERENCE, true);
        return pushPref;
    }

    /* Currency */
    public void setCurrencyPref(int currencyPref) {
        prefs.edit().putInt(KEY_CURRENCY_PREFERENCE, currencyPref).apply();
    }

    public int getCurrencyPref() {
        int currencyPref = prefs.getInt(KEY_CURRENCY_PREFERENCE,0);
        return currencyPref;
    }

    /* Language */
    public void setLanguagePref(int languagePref) {
        prefs.edit().putInt(KEY_LANGUAGE_PREFERENCE, languagePref).apply();
    }

    public int getLanguagePref() {
        int languagePref = prefs.getInt(KEY_LANGUAGE_PREFERENCE,0);
        return languagePref;
    }


    public void updateUserSession(VillimUser user) {

    }
}
