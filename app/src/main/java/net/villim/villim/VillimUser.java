package net.villim.villim;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import static net.villim.villim.VillimKeys.KEY_EMAIL;
import static net.villim.villim.VillimKeys.KEY_FIRSTNAME;
import static net.villim.villim.VillimKeys.KEY_FULLNAME;
import static net.villim.villim.VillimKeys.KEY_ID;
import static net.villim.villim.VillimKeys.KEY_LASTNAME;
import static net.villim.villim.VillimKeys.KEY_PROFILE_PIC_URL;
import static net.villim.villim.VillimKeys.KEY_ROOM_ID;
import static net.villim.villim.VillimKeys.KEY_STATUS;

/**
 * Created by seongmin on 5/31/17.
 */

public class VillimUser implements Parcelable {
    public int id;
    public String fullname;
    public String firstname;
    public String lastname;
    public String email;
    public String profilePicUrl;
    public int status;
    public int roomId;

    protected VillimUser(Parcel in) {
        id = in.readInt();
        fullname = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        email = in.readString();
        profilePicUrl = in.readString();
        status = in.readInt();
        roomId = in.readInt();
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
        try {
            user.id = userInfo.getInt(KEY_ID);
            user.fullname = userInfo.get(KEY_FULLNAME).toString();
            user.firstname = userInfo.get(KEY_FIRSTNAME).toString();
            user.lastname = userInfo.get(KEY_LASTNAME).toString();
            user.email = userInfo.get(KEY_EMAIL).toString();
            boolean isProfilePicUrlNull = userInfo.isNull(KEY_PROFILE_PIC_URL);
            user.profilePicUrl = isProfilePicUrlNull ? null : userInfo.get(KEY_PROFILE_PIC_URL).toString();
            user.status = userInfo.getInt(KEY_STATUS);
            boolean isRoomIdNull = userInfo.isNull(KEY_ROOM_ID);
            user.roomId = isRoomIdNull ? -1 : userInfo.getInt(KEY_ROOM_ID);
        } catch (JSONException e) {

        }
        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(fullname);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(email);
        dest.writeString(profilePicUrl);
        dest.writeInt(status);
        dest.writeInt(roomId);
    }
}
