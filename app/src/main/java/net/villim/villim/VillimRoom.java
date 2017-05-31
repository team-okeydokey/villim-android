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

    public static final String KEY_ROOM_ID = "room_id";
    public static final String KEY_ROOM_TITLE = "room_title";
    public static final String KEY_ROOM_PRICE = "room_price";
    public static final String KEY_ROOM_REVIEW_RATING = "room_review_rating";
    public static final String KEY_ROOM_REVIEW_COUNT = "room_review_count";

    public static final String KEY_HOST_NAME = "host_name";
    public static final String KEY_HOST_REVIEW_RATING = "host_review_rating";
    public static final String KEY_HOST_REVIEW_COUNT = "host_review_count";
    public static final String KEY_TAGS = "tags";
    public static final String KEY_NUM_GUESTS = "num_guests";
    public static final String KEY_NUM_ROOMS = "num_rooms";
    public static final String KEY_NUM_BEDS = "num_beds";
    public static final String KEY_NUM_BATHS = "num_baths";


    public int roomId;
    public String roomTitle;
    public int roomPrice;
    public float roomReviewRating;
    public int roomReviewCount;
    public String hostName;
    public float hostReviewRating;
    public int hostReviewCount;
    public String[] tags;
    public int numGuests;
    public int numRooms;
    public int numBeds;
    public int numBaths;

    public VillimRoom(JSONObject jsonObject) {
        try {
            roomId = jsonObject.getInt(KEY_ROOM_ID);
            roomTitle = jsonObject.getString(KEY_ROOM_TITLE);
            roomPrice = jsonObject.getInt(KEY_ROOM_PRICE);
            roomReviewRating = Float.valueOf(jsonObject.getString(KEY_ROOM_REVIEW_RATING));
            roomReviewCount = jsonObject.getInt(KEY_ROOM_REVIEW_COUNT);
            hostName = jsonObject.getString(KEY_HOST_NAME);
            hostReviewRating = Float.valueOf(jsonObject.getString(KEY_HOST_REVIEW_RATING));
            hostReviewCount = jsonObject.getInt(KEY_HOST_REVIEW_COUNT);
            tags = JSONtoArray(jsonObject.getJSONArray(KEY_TAGS));
            numGuests = jsonObject.getInt(KEY_NUM_GUESTS);
            numRooms = jsonObject.getInt(KEY_NUM_ROOMS);
            numBeds = jsonObject.getInt(KEY_NUM_BEDS);
            numBaths = jsonObject.getInt(KEY_NUM_BATHS);
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
