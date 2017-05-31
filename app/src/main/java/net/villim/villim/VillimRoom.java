package net.villim.villim;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by seongmin on 5/30/17.
 */

public class VillimRoom {

    public static final String KEY_HOST_NAME = "host_name";
    public static final String KEY_HOST_REVIEW_RATING = "host_review_rating";
    public static final String KEY_HOST_REVIEW_COUNT = "host_review_count";
    public static final String KEY_TAGS = "tags";
    public static final String KEY_NUM_GUESTS = "num_guests";
    public static final String KEY_NUM_ROOMS = "num_rooms";
    public static final String KEY_NUM_BEDS = "num_beds";
    public static final String KEY_NUM_BATHS = "num_baths";


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
}
