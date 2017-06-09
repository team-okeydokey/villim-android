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
    public static final String KEY_HOST_RATING = "host_rating";
    public static final String KEY_HOST_REVIEW_COUNT = "host_review_count";
    public static final String KEY_HOUSE_RATING = "house_rating";
    public static final String KEY_HOUSE_REVIEW_COUNT = "house_review_count";

    public static final String KEY_AMENITY_IDS = "amenity_ids";


    // Raw query from server.
    public int houseId;
    public String houseName;
    public String addrFull;
    public String addrSummary;
    public String addrDirection;
    public String description;
    public int numGuest;
    public int numBedroom;
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
    int hostId;
    String hostName;
    float hostRating;
    int hostReviewCount;
    float houseRating;
    int houseReviewCount;
    int[] amenityIds;
    VillimReview[] reviews;


    //public VillimReview[] reviews;
    //public int[] utilities;

    public VillimRoom(JSONObject jsonObject) {
        try {
            houseId = jsonObject.getInt(KEY_HOUSE_ID);
            houseName = jsonObject.getString(KEY_HOUSE_NAME);
            addrFull = jsonObject.getString(KEY_ADDR_FULL);
            addrSummary =  jsonObject.getString(KEY_ADDR_SUMMARY);
            addrDirection = jsonObject.getString(KEY_ADDR_DIRECTION);
            description = jsonObject.getString(KEY_DESCRIPTION);
            numGuest = jsonObject.getInt(KEY_NUM_GUEST);
            numBedroom = jsonObject.getInt(KEY_NUM_BEDROOM);
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
            hostId = jsonObject.getInt(KEY_HOST_ID);
            hostName = jsonObject.getString(KEY_HOST_NAME);
            hostRating = (float) jsonObject.getDouble(KEY_HOST_RATING);
            hostReviewCount = jsonObject.getInt(KEY_HOST_REVIEW_COUNT);
            houseRating = (float) jsonObject.getDouble(KEY_HOUSE_RATING);
            houseReviewCount = jsonObject.getInt(KEY_HOUSE_REVIEW_COUNT);

            amenityIds = VillimUtil.JSONArrayToIntArray(jsonObject.getJSONArray(KEY_AMENITY_IDS));
//            reviews = VillimReview.getHouseReviewsFromServer(houseId);
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
        dest.writeInt(houseId);
        dest.writeString(houseName);
        dest.writeString(addrFull);
        dest.writeString(addrSummary);
        dest.writeString(addrDirection);
        dest.writeString(description);
        dest.writeInt(numGuest);
        dest.writeInt(numBedroom);
        dest.writeInt(numBed);
        dest.writeInt(numBathroom);
        dest.writeInt(lockAddr);
        dest.writeInt(lockPc);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(roomPolicy);
        dest.writeString(refundPolicy);
        dest.writeInt(hostId);
        dest.writeString(hostName);
        dest.writeFloat(hostRating);
        dest.writeInt(hostReviewCount);
        dest.writeFloat(houseRating);
        dest.writeInt(houseReviewCount);
        dest.writeIntArray(amenityIds);
//        dest.writeParcelableArray(reviews, 0);
    }

    protected VillimRoom(Parcel in) {
        houseId = in.readInt();
        houseName = in.readString();
        addrFull = in.readString();
        addrSummary = in.readString();
        addrDirection = in.readString();
        description = in.readString();
        numGuest = in.readInt();
        numBedroom = in.readInt();
        numBed = in.readInt();
        numBathroom = in.readInt();
        lockAddr = in.readInt();
        lockPc = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        roomPolicy = in.readString();
        refundPolicy = in.readString();
        hostId = in.readInt();
        hostName = in.readString();
        hostRating = in.readFloat();
        hostReviewCount = in.readInt();
        houseRating = in.readFloat();
        houseReviewCount = in.readInt();
        amenityIds = in.createIntArray();
//        reviews = in.createTypedArray(VillimReview.CREATOR);
    }

    public static final Creator<VillimRoom> CREATOR = new Creator<VillimRoom>() {
        @Override
        public VillimRoom createFromParcel(Parcel in) {
            return new VillimRoom(in);
        }

        @Override
        public VillimRoom[] newArray(int size) {
            return new VillimRoom[size];
        }
    };
}
