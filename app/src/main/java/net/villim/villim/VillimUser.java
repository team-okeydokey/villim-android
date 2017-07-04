package net.villim.villim;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import static net.villim.villim.VillimKeys.KEY_ABOUT;
import static net.villim.villim.VillimKeys.KEY_CITY_OF_RESIDENCE;
import static net.villim.villim.VillimKeys.KEY_CURRENCY_PREFERENCE;
import static net.villim.villim.VillimKeys.KEY_EMAIL;
import static net.villim.villim.VillimKeys.KEY_FIRSTNAME;
import static net.villim.villim.VillimKeys.KEY_FULLNAME;
import static net.villim.villim.VillimKeys.KEY_HOUSE_ID_CONFIRMED;
import static net.villim.villim.VillimKeys.KEY_HOUSE_ID_DONE;
import static net.villim.villim.VillimKeys.KEY_HOUSE_ID_STAYING;
import static net.villim.villim.VillimKeys.KEY_LANGUAGE_PREFERENCE;
import static net.villim.villim.VillimKeys.KEY_LASTNAME;
import static net.villim.villim.VillimKeys.KEY_PHONE_NUMBER;
import static net.villim.villim.VillimKeys.KEY_PROFILE_PIC_URL;
import static net.villim.villim.VillimKeys.KEY_PUSH_NOTIFICATIONS;
import static net.villim.villim.VillimKeys.KEY_SEX;
import static net.villim.villim.VillimKeys.KEY_USER_ID;
import static net.villim.villim.VillimKeys.KEY_VISIT_HOUSE_ID_CONFIRMED;
import static net.villim.villim.VillimKeys.KEY_VISIT_HOUSE_ID_DONE;
import static net.villim.villim.VillimKeys.KEY_VISIT_HOUSE_ID_PENDING;

/**
 * Created by seongmin on 5/31/17.
 */

public class VillimUser implements Parcelable {
    public int userId;
    public String fullname;
    public String firstname;
    public String lastname;
    public String email;
    public String profilePicUrl;
    public String about;

    public int sex;
    public String phoneNumber;
    public String cityOfResidence;
    public boolean pushNotifications;
    public int currencyPref;
    public int languagePref;

    public int[] houseIdConfirmed;
    public int houseIdStaying;
    public int[] houseIdDone;

    public int[] visitHouseIdPending;
    public int[] visitHouseIdConfirmed;
    public int[] visitHouseIdDone;

    protected VillimUser(Parcel in) {
        userId = in.readInt();
        fullname = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        email = in.readString();
        profilePicUrl = in.readString();
        about = in.readString();
        sex = in.readInt();
        phoneNumber = in.readString();
        cityOfResidence = in.readString();
        pushNotifications = in.readByte() != 0;
        currencyPref = in.readInt();
        languagePref = in.readInt();

        houseIdConfirmed = in.createIntArray();
        houseIdStaying = in.readInt();
        houseIdDone = in.createIntArray();

        visitHouseIdPending = in.createIntArray();
        visitHouseIdConfirmed = in.createIntArray();
        visitHouseIdDone = in.createIntArray();
    }

    public static final Creator<VillimUser> CREATOR = new Creator<VillimUser>() {
        @Override
        public VillimUser createFromParcel(Parcel in) {
            return new VillimUser(in);
        }

        @Override
        public VillimUser[] newArray(int size) {
            return new VillimUser[size];
        }
    };

    public VillimUser() {

    }

    public static VillimUser getUserFromServer(int id) {
        VillimUser user = new VillimUser();
        user.fullname = "Kim Woo Bin";
        return user;
    }

    public static VillimUser createUserFromJSONObject(JSONObject userInfo) {
        /* Create user instance */
        VillimUser user = new VillimUser();

         /* No need to null check here because if we dont set it, it's going to be null anyway */
        user.userId = userInfo.optInt(KEY_USER_ID);
        user.fullname = userInfo.opt(KEY_FULLNAME).toString();
        user.firstname = userInfo.opt(KEY_FIRSTNAME).toString();
        user.lastname = userInfo.opt(KEY_LASTNAME).toString();
        user.email = userInfo.opt(KEY_EMAIL).toString();
        boolean isProfilePicUrlNull = userInfo.isNull(KEY_PROFILE_PIC_URL);
        user.profilePicUrl = isProfilePicUrlNull ? null : userInfo.opt(KEY_PROFILE_PIC_URL).toString();
        user.about = userInfo.optString(KEY_ABOUT);
        user.sex = userInfo.optInt(KEY_SEX);
        user.phoneNumber = userInfo.optString(KEY_PHONE_NUMBER);
        user.cityOfResidence = userInfo.optString(KEY_CITY_OF_RESIDENCE);
        user.pushNotifications = userInfo.optBoolean(KEY_PUSH_NOTIFICATIONS);
        user.currencyPref = userInfo.optInt(KEY_CURRENCY_PREFERENCE);
        user.languagePref = userInfo.optInt(KEY_LANGUAGE_PREFERENCE);
        user.houseIdConfirmed = net.villim.villim.VillimUtils.JSONArrayToIntArray(userInfo.optJSONArray(KEY_HOUSE_ID_CONFIRMED));
        user.houseIdStaying = userInfo.optInt(KEY_HOUSE_ID_STAYING);
        user.houseIdDone = net.villim.villim.VillimUtils.JSONArrayToIntArray(userInfo.optJSONArray(KEY_HOUSE_ID_DONE));
        user.visitHouseIdPending = net.villim.villim.VillimUtils.JSONArrayToIntArray(userInfo.optJSONArray(KEY_VISIT_HOUSE_ID_PENDING));
        user.visitHouseIdConfirmed = net.villim.villim.VillimUtils.JSONArrayToIntArray(userInfo.optJSONArray(KEY_VISIT_HOUSE_ID_CONFIRMED));
        user.visitHouseIdDone = net.villim.villim.VillimUtils.JSONArrayToIntArray(userInfo.optJSONArray(KEY_VISIT_HOUSE_ID_DONE));
        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(fullname);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(email);
        dest.writeString(profilePicUrl);
        dest.writeString(about);
        dest.writeInt(sex);
        dest.writeString(phoneNumber);
        dest.writeString(cityOfResidence);
        dest.writeByte((byte) (pushNotifications ? 1 : 0));
        dest.writeInt(currencyPref);
        dest.writeInt(languagePref);
        dest.writeIntArray(houseIdConfirmed);
        dest.writeInt(houseIdStaying);
        dest.writeIntArray(houseIdDone);
        dest.writeIntArray(visitHouseIdPending);
        dest.writeIntArray(visitHouseIdConfirmed);
        dest.writeIntArray(visitHouseIdDone);
    }
}
