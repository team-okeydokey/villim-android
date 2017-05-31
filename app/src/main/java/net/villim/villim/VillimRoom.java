package net.villim.villim;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by seongmin on 5/30/17.
 */

public class VillimRoom implements Parcelable {

    public static final String KEY_HOUSE_ID = "house_id";
    public static final String KEY_HOUSE_NAME = "house_name";
    public static final String KEY_ADDR_FULL = "addr_full";
    public static final String KEY_ADDR_SUMMARY = "addr_summary";
    public static final String KEY_ADDR_DIRECTION = "addr_direction";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_NUM_GUEST = "num_guest";
    public static final String KEY_NUM_BEDROOM = "num_bedroom";
    public static final String KEY_NUM_BED = "num_bed";
    public static final String KEY_NUM_BATHROOM = "num_bathroom";
    public static final String KEY_PRICE = "price";
    public static final String KEY_LOCK_ADDR = "lock_addr";
    public static final String KEY_LOCK_PC = "lock_pw";
    public static final String KEY_HIT = "hit";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_STATUS = "status";
    public static final String KEY_CREATED = "created";
    public static final String KEY_MODIFIED = "modified";
    public static final String KEY_ROOM_POLICY = "room_policy";
    public static final String KEY_REFUND_POLICY = "refund_policy";

    public static final String KEY_HOST_ID = "host_id";
    public static final String KEY_HOST_NAME = "host_name";



    // Raw query from server.
    public int houseId;
    public String houseName;
    public String addrFull;
    public String addrSummary;
    public String addrDirection;
    public String description;
    public int numGuest;
    public int numBedRoom;
    public int numBed;
    public int numBathroom;
    public int housePrice;
    private int lockAddr;
    private int lockPc;
    public double latitude;
    public double longitude;
    public String roomPolicy;
    public String refundPolicy;
    public VillimUser host;
    String hostName;


    public VillimReview[] reviews;
    public int[] utilities;

    public VillimRoom(JSONObject jsonObject) {
        try {
            houseId = jsonObject.getInt(KEY_HOUSE_ID);
            houseName = jsonObject.getString(KEY_HOUSE_NAME);
            addrFull = jsonObject.getString(KEY_ADDR_FULL);
            addrSummary =  jsonObject.getString(KEY_ADDR_SUMMARY);
            addrDirection = jsonObject.getString(KEY_ADDR_DIRECTION);
            description = jsonObject.getString(KEY_DESCRIPTION);
            numGuest = jsonObject.getInt(KEY_NUM_GUEST);
            numBedRoom = jsonObject.getInt(KEY_NUM_BEDROOM);
            numBed = jsonObject.getInt(KEY_NUM_BED);
            numBathroom = jsonObject.getInt(KEY_NUM_BATHROOM);
            housePrice = jsonObject.getInt(KEY_PRICE);
            lockAddr = jsonObject.getInt(KEY_LOCK_ADDR);
            lockPc = jsonObject.getInt(KEY_LOCK_PC);
            latitude = jsonObject.getDouble(KEY_LATITUDE);
            longitude = jsonObject.getDouble(KEY_LONGITUDE);
            roomPolicy = jsonObject.getString(KEY_ROOM_POLICY);
            refundPolicy = jsonObject.getString(KEY_REFUND_POLICY);

            //host = VillimUser.getUserFromServer(jsonObject.getInt(KEY_HOST_ID));
            hostName = jsonObject.getString(KEY_HOST_NAME);

        } catch (JSONException e) {

        }
    }

    private String[] JSONtoArray(JSONArray array) {
        String[] stringsArray = new String[array.length()];
        try{
            for (int i = 0; i < array.length(); i++) {
                stringsArray[i] = array.getString(i);
            }
        } catch(JSONException e) {

        }
        return stringsArray;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
