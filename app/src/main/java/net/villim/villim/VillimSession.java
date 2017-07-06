package net.villim.villim;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.File;

import static net.villim.villim.VillimKeys.KEY_CITY_OF_RESIDENCE;
import static net.villim.villim.VillimKeys.KEY_LOCAL_STORE_PROFILE_PICTURE;
import static net.villim.villim.VillimKeys.KEY_PREFERENCE_CURRENCY;
import static net.villim.villim.VillimKeys.KEY_EMAIL;
import static net.villim.villim.VillimKeys.KEY_FIRSTNAME;
import static net.villim.villim.VillimKeys.KEY_FULLNAME;
import static net.villim.villim.VillimKeys.KEY_PREFERENCE_LANGUAGE;
import static net.villim.villim.VillimKeys.KEY_LASTNAME;
import static net.villim.villim.VillimKeys.KEY_PHONE_NUMBER;
import static net.villim.villim.VillimKeys.KEY_PROFILE_PIC_URL;
import static net.villim.villim.VillimKeys.KEY_PUSH_NOTIFICATIONS;
import static net.villim.villim.VillimKeys.KEY_SEX;
import static net.villim.villim.VillimKeys.KEY_USER_ID;
import static net.villim.villim.VillimKeys.KEY_USER_STATUS;
import static net.villim.villim.VillimKeys.KEY_HOUSE_ID_CONFIRMED;
import static net.villim.villim.VillimKeys.KEY_HOUSE_ID_STAYING;
import static net.villim.villim.VillimKeys.KEY_HOUSE_ID_DONE;
import static net.villim.villim.VillimKeys.KEY_VISIT_HOUSE_ID_PENDING;
import static net.villim.villim.VillimKeys.KEY_VISIT_HOUSE_ID_CONFIRMED;
import static net.villim.villim.VillimKeys.KEY_VISIT_HOUSE_ID_DONE;

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
    public void setUserId(int id) {
        prefs.edit().putInt(KEY_USER_ID, id).apply();
    }

    public int getUserId() {
        int id = prefs.getInt(KEY_USER_ID, 0);
        return id;
    }


    /* Fullname */
    public void setFullName(String fullname) {
        prefs.edit().putString(KEY_FULLNAME, fullname).apply();
    }

    public String getFullName() {
        String fullname = prefs.getString(KEY_FULLNAME, "");
        return fullname;
    }

    /* Firstname */
    public void setFirstName(String fullname) {
        prefs.edit().putString(KEY_FIRSTNAME, fullname).apply();
    }

    public String getFirstName() {
        String firstname = prefs.getString(KEY_FIRSTNAME, "");
        return firstname;
    }

    /* Lastname */
    public void setLastName(String fullname) {
        prefs.edit().putString(KEY_LASTNAME, fullname).apply();
    }

    public String getLastName() {
        String lastname = prefs.getString(KEY_LASTNAME, "");
        return lastname;
    }

    /* Email */
    public void setEmail(String email) {
        prefs.edit().putString(KEY_EMAIL, email).apply();
    }

    public String getEmail() {
        String email = prefs.getString(KEY_EMAIL, "");
        return email;
    }

    /* Profile pic url */
    public void setProfilePicUrl(String url) {
        prefs.edit().putString(KEY_PROFILE_PIC_URL, url).apply();
    }

    public String getProfilePicUrl() {
        String url = prefs.getString(KEY_PROFILE_PIC_URL, "");
        return url;
    }

    /* Rent status */
    public void setStatus(int status) {
        prefs.edit().putInt(KEY_USER_STATUS, status).apply();
    }

    public int getStatus() {
        int status = prefs.getInt(KEY_USER_STATUS, 0);
        return status;
    }


    /* Push Notifications */
    public void setPushPref(boolean pushPref) {
        prefs.edit().putBoolean(KEY_PUSH_NOTIFICATIONS, pushPref).apply();
    }

    public boolean getPushPref() {
        boolean pushPref = prefs.getBoolean(KEY_PUSH_NOTIFICATIONS, true);
        return pushPref;
    }

    /* Currency */
    public void setCurrencyPref(int currencyPref) {
        prefs.edit().putInt(KEY_PREFERENCE_CURRENCY, currencyPref).apply();
    }

    public int getCurrencyPref() {
        int currencyPref = prefs.getInt(KEY_PREFERENCE_CURRENCY, 0);
        return currencyPref;
    }

    /* Language */
    public void setLanguagePref(int languagePref) {
        prefs.edit().putInt(KEY_PREFERENCE_LANGUAGE, languagePref).apply();
    }

    public int getLanguagePref() {
        int languagePref = prefs.getInt(KEY_PREFERENCE_LANGUAGE, 0);
        return languagePref;
    }

    /* Sex */
    public void setSex(int sex) {
        prefs.edit().putInt(KEY_SEX, sex).apply();
    }

    public int getSex() {
        int sex = prefs.getInt(KEY_SEX, 0);
        return sex;
    }

    /* Phone number */
    public void setPhoneNumber(String phoneNumber) {
        prefs.edit().putString(KEY_PHONE_NUMBER, phoneNumber).apply();
    }

    public String getPhoneNumber() {
        String phoneNumber = prefs.getString(KEY_PHONE_NUMBER, "");
        return phoneNumber;
    }

    /* City of residence */
    public void setCityOfResidence(String city) {
        prefs.edit().putString(KEY_CITY_OF_RESIDENCE, city).apply();
    }

    public String getCityOfResidence() {
        String city = prefs.getString(KEY_CITY_OF_RESIDENCE, "");
        return city;
    }

    /* House id confirmed */
    public void setHouseIdConfirmed(int[] houseIdArray) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < houseIdArray.length; i++) {
            str.append(houseIdArray[i]).append(",");
        }
        prefs.edit().putString(KEY_HOUSE_ID_CONFIRMED, str.toString()).apply();
    }

    public int[] getHosueIdConfirmed() {
        String rawString = prefs.getString(KEY_HOUSE_ID_CONFIRMED, "");
        if (rawString.isEmpty()) {
            return new int[0];
        } else {
            String[] stringArray = rawString.split(",");
            int[] houseIdArray = new int[stringArray.length];

            for (int i = 0; i < houseIdArray.length; i++) {
                houseIdArray[i] = Integer.parseInt(stringArray[i]);
            }
            return houseIdArray;
        }
    }

    /* House id staying */
    public void setHouseIdStaying(int id) {
        prefs.edit().putInt(KEY_HOUSE_ID_STAYING, id).apply();
    }

    public int getHouseIdStaying() {
        int id = prefs.getInt(KEY_HOUSE_ID_STAYING, 0);
        return id;
    }

    /* House id done */
    public void setHouseIdDone(int[] houseIdArray) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < houseIdArray.length; i++) {
            str.append(houseIdArray[i]).append(",");
        }
        prefs.edit().putString(KEY_HOUSE_ID_DONE, str.toString()).apply();
    }

    public int[] getHosueIdDone() {
        String rawString = prefs.getString(KEY_HOUSE_ID_DONE, "");
        if (rawString.isEmpty()) {
            return new int[0];
        } else {
            String[] stringArray = rawString.split(",");
            int[] houseIdArray = new int[stringArray.length];

            for (int i = 0; i < houseIdArray.length; i++) {
                houseIdArray[i] = Integer.parseInt(stringArray[i]);
            }
            return houseIdArray;
        }
    }

    /* Visit house id pending */
    public void setVisitHouseIdPending(int[] houseIdArray) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < houseIdArray.length; i++) {
            str.append(houseIdArray[i]).append(",");
        }
        prefs.edit().putString(KEY_VISIT_HOUSE_ID_PENDING, str.toString()).apply();
    }

    public int[] getVisitHosueIdPending() {
        String rawString = prefs.getString(KEY_VISIT_HOUSE_ID_PENDING, "");
        if (rawString.isEmpty()) {
            return new int[0];
        } else {
            String[] stringArray = rawString.split(",");
            int[] houseIdArray = new int[stringArray.length];

            for (int i = 0; i < houseIdArray.length; i++) {
                houseIdArray[i] = Integer.parseInt(stringArray[i]);
            }
            return houseIdArray;
        }
    }

    /* Visit house id confirmed */
    public void setVisitHouseIdConfirmed(int[] houseIdArray) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < houseIdArray.length; i++) {
            str.append(houseIdArray[i]).append(",");
        }
        prefs.edit().putString(KEY_VISIT_HOUSE_ID_CONFIRMED, str.toString()).apply();
    }

    public int[] getVisitHosueIdConfirmed() {
        String rawString = prefs.getString(KEY_VISIT_HOUSE_ID_CONFIRMED, "");
        if (rawString.isEmpty()) {
            return new int[0];
        } else {
            String[] stringArray = rawString.split(",");
            int[] houseIdArray = new int[stringArray.length];

            for (int i = 0; i < houseIdArray.length; i++) {
                houseIdArray[i] = Integer.parseInt(stringArray[i]);
            }
            return houseIdArray;
        }
    }

    /* Visit House id done */
    public void setVisitHouseIdDone(int[] houseIdArray) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < houseIdArray.length; i++) {
            str.append(houseIdArray[i]).append(",");
        }
        prefs.edit().putString(KEY_VISIT_HOUSE_ID_DONE, str.toString()).apply();
    }

    public int[] getVisitHouseIdDone() {
        String rawString = prefs.getString(KEY_VISIT_HOUSE_ID_DONE, "");
        if (rawString.isEmpty()) {
            return new int[0];
        } else {
            String[] stringArray = rawString.split(",");
            int[] houseIdArray = new int[stringArray.length];

            for (int i = 0; i < houseIdArray.length; i++) {
                houseIdArray[i] = Integer.parseInt(stringArray[i]);
            }
            return houseIdArray;
        }
    }

    /* City of residence */
    public void setLocalStoreProfilePicturePath(String path) {
        prefs.edit().putString(KEY_LOCAL_STORE_PROFILE_PICTURE, path).apply();
    }

    public String getLocalStoreProfilePicturePath() {
        String path = prefs.getString(KEY_LOCAL_STORE_PROFILE_PICTURE, null);
        return path;
    }

    public File getLocalStoreProfilePictureFile() {
        String path = prefs.getString(KEY_LOCAL_STORE_PROFILE_PICTURE, null);

        if (path == null) { return null; };

        File imageFile = null;

        if (path != null) {
            imageFile = new File(path);
            if (imageFile != null && imageFile.exists()) {
                return imageFile;
            }
        }

        return null;
    }


    public void updateUserSession(VillimUser user) {
        setUserId(user.userId);
        setFullName(user.fullname);
        setFirstName(user.firstname);
        setLastName(user.lastname);
        setEmail(user.email);
        setProfilePicUrl(user.profilePicUrl);
        setPushPref(user.pushNotifications);
        setCurrencyPref(user.currencyPref);
        setLanguagePref(user.languagePref);
        setSex(user.sex);
        setPhoneNumber(user.phoneNumber);
        setCityOfResidence(user.cityOfResidence);
        setHouseIdConfirmed(user.houseIdConfirmed);
        setHouseIdStaying(user.houseIdStaying);
        setHouseIdDone(user.houseIdDone);
        setVisitHouseIdPending(user.visitHouseIdPending);
        setVisitHouseIdConfirmed(user.visitHouseIdConfirmed);
        setVisitHouseIdDone(user.visitHouseIdDone);
    }
}
