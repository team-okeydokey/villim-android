package net.villim.villim;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static net.villim.villim.VillimKeys.KEY_ADDR_DIRECTION;
import static net.villim.villim.VillimKeys.KEY_ADDR_FULL;
import static net.villim.villim.VillimKeys.KEY_ADDR_SUMMARY;
import static net.villim.villim.VillimKeys.KEY_AMENITY_IDS;
import static net.villim.villim.VillimKeys.KEY_CANCELLATION_POLICY;
import static net.villim.villim.VillimKeys.KEY_CLEANING_FEE;
import static net.villim.villim.VillimKeys.KEY_DESCRIPTION;
import static net.villim.villim.VillimKeys.KEY_END_DATE;
import static net.villim.villim.VillimKeys.KEY_HOST_ID;
import static net.villim.villim.VillimKeys.KEY_HOST_NAME;
import static net.villim.villim.VillimKeys.KEY_HOST_PROFILE_PIC_URL;
import static net.villim.villim.VillimKeys.KEY_HOST_RATING;
import static net.villim.villim.VillimKeys.KEY_HOST_REVIEW_COUNT;
import static net.villim.villim.VillimKeys.KEY_HOUSE_ID;
import static net.villim.villim.VillimKeys.KEY_HOUSE_NAME;
import static net.villim.villim.VillimKeys.KEY_HOUSE_PIC_URLS;
import static net.villim.villim.VillimKeys.KEY_HOUSE_POLICY;
import static net.villim.villim.VillimKeys.KEY_HOUSE_RATING;
import static net.villim.villim.VillimKeys.KEY_HOUSE_REVIEW_COUNT;
import static net.villim.villim.VillimKeys.KEY_HOUSE_THUMBNAIL_URL;
import static net.villim.villim.VillimKeys.KEY_LATITUDE;
import static net.villim.villim.VillimKeys.KEY_LOCK_ADDR;
import static net.villim.villim.VillimKeys.KEY_LOCK_PC;
import static net.villim.villim.VillimKeys.KEY_LONGITUDE;
import static net.villim.villim.VillimKeys.KEY_NUM_BATHROOM;
import static net.villim.villim.VillimKeys.KEY_NUM_BED;
import static net.villim.villim.VillimKeys.KEY_NUM_BEDROOM;
import static net.villim.villim.VillimKeys.KEY_NUM_GUEST;
import static net.villim.villim.VillimKeys.KEY_RATE_PER_MONTH;
import static net.villim.villim.VillimKeys.KEY_RATE_PER_NIGHT;
import static net.villim.villim.VillimKeys.KEY_RESERVATIONS;
import static net.villim.villim.VillimKeys.KEY_START_DATE;
import static net.villim.villim.VillimKeys.KEY_UTILITY_FEE;
import static net.villim.villim.VillimKeys.KEY_HOUSE_TYPE;

/**
 * Created by seongmin on 5/30/17.
 */

public class VillimHouse implements Parcelable {

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
    public int ratePerMonth;
    public int ratePerNight;
    public int utilityFee;
    public int cleaningFee;
    private int lockAddr;
    private int lockPc;
    public double latitude;
    public double longitude;
    public String housePolicy;
    public String cancellationPolicy;
    public VillimUser host;
    public int hostId;
    public String hostName;
    public float hostRating;
    public int hostReviewCount;
    public String hostProfilePicUrl;
    public float houseRating;
    public int houseReviewCount;
    public int[] amenityIds;
    public String houseThumbnailUrl;
    public String[] housePicUrls;
    public int houseType;
    public VillimReservation[] reservations;
    //public VillimReview[] reviews;
    //public int[] utilities;

    public VillimHouse() {

    }

    private String[] JSONtoArray(JSONArray array) {
        String[] stringsArray = new String[array.length()];
        try {
            for (int i = 0; i < array.length(); i++) {
                stringsArray[i] = array.getString(i);
            }
        } catch (JSONException e) {

        }
        return stringsArray;
    }

    public static VillimHouse createHouseFromJSONObject(JSONObject jsonObject) {

        /* Create user instance */
        VillimHouse house = new VillimHouse();

         /* No need to null check here because if we dont set it, it's going to be null anyway */
        house.houseId = jsonObject.optInt(KEY_HOUSE_ID);
        house.houseName = jsonObject.optString(KEY_HOUSE_NAME);
        house.addrFull = jsonObject.optString(KEY_ADDR_FULL);
        house.addrSummary = jsonObject.optString(KEY_ADDR_SUMMARY);
        house.addrDirection = jsonObject.optString(KEY_ADDR_DIRECTION);
        house.description = jsonObject.optString(KEY_DESCRIPTION);
        house.houseType = jsonObject.optInt(KEY_HOUSE_TYPE);
        house.numGuest = jsonObject.optInt(KEY_NUM_GUEST);
        house.numBedroom = jsonObject.optInt(KEY_NUM_BEDROOM);
        house.numBed = jsonObject.optInt(KEY_NUM_BED);
        house.numBathroom = jsonObject.optInt(KEY_NUM_BATHROOM);
        house.ratePerMonth = jsonObject.optInt(KEY_RATE_PER_MONTH);
        house.ratePerNight = jsonObject.optInt(KEY_RATE_PER_NIGHT);
        house.utilityFee = jsonObject.optInt(KEY_UTILITY_FEE);
        house.cleaningFee = jsonObject.optInt(KEY_CLEANING_FEE);
        house.lockAddr = jsonObject.optInt(KEY_LOCK_ADDR);
        house.lockPc = jsonObject.optInt(KEY_LOCK_PC);
        house.latitude = jsonObject.optDouble(KEY_LATITUDE);
        house.longitude = jsonObject.optDouble(KEY_LONGITUDE);
        house.housePolicy = jsonObject.optString(KEY_HOUSE_POLICY);
        house.cancellationPolicy = jsonObject.optString(KEY_CANCELLATION_POLICY);

        //host = VillimUser.getUserFromServer(jsonObject.getInt(KEY_HOST_ID));
        house.hostId = jsonObject.optInt(KEY_HOST_ID);
        house.hostName = jsonObject.optString(KEY_HOST_NAME);
        house.hostRating = (float) jsonObject.optDouble(KEY_HOST_RATING, 0);
        house.hostReviewCount = jsonObject.optInt(KEY_HOST_REVIEW_COUNT);
        house.hostProfilePicUrl = jsonObject.optString(KEY_HOST_PROFILE_PIC_URL);
        house.houseRating = (float) jsonObject.optDouble(KEY_HOUSE_RATING, 0);
        house.houseReviewCount = jsonObject.optInt(KEY_HOUSE_REVIEW_COUNT);

        house.houseThumbnailUrl = jsonObject.optString(KEY_HOUSE_THUMBNAIL_URL);
        house.amenityIds = net.villim.villim.VillimUtils.JSONArrayToIntArray(jsonObject.optJSONArray(KEY_AMENITY_IDS));
        house.housePicUrls = net.villim.villim.VillimUtils.JSONArrayToStringArray(jsonObject.optJSONArray(KEY_HOUSE_PIC_URLS));

        JSONArray reservationArray = jsonObject.optJSONArray(KEY_RESERVATIONS);
        if (reservationArray == null) {
            house.reservations = new VillimReservation[0];
        } else {
            house.reservations = new VillimReservation[reservationArray.length()];
            for (int i = 0; i < reservationArray.length(); ++i) {
                JSONObject reservationInfo = (JSONObject) reservationArray.optJSONObject(i);

                VillimReservation reservation = new VillimReservation();
                reservation.startDate = net.villim.villim.VillimUtils.dateFromDateString(reservationInfo.optString(KEY_START_DATE));
                reservation.endDate = net.villim.villim.VillimUtils.dateFromDateString(reservationInfo.optString(KEY_END_DATE));

                house.reservations[i] = reservation;
            }
        }

        return house;
    }

    public static VillimHouse[] houseArrayFromJsonArray(JSONArray jsonArray) {

        if (jsonArray == null) {
            return new VillimHouse[0];
        }

        VillimHouse[] houses = new VillimHouse[jsonArray.length()];

        try {
            for (int i = 0; i < jsonArray.length(); ++i) {
                VillimHouse house = createHouseFromJSONObject(jsonArray.getJSONObject(i));
                houses[i] = house;
            }

        } catch (JSONException e) {

        }
        return houses;
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
        dest.writeInt(houseType);
        dest.writeInt(numGuest);
        dest.writeInt(numBedroom);
        dest.writeInt(numBed);
        dest.writeInt(numBathroom);
        dest.writeInt(ratePerMonth);
        dest.writeInt(ratePerNight);
        dest.writeInt(utilityFee);
        dest.writeInt(cleaningFee);
        dest.writeInt(lockAddr);
        dest.writeInt(lockPc);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(housePolicy);
        dest.writeString(cancellationPolicy);
        dest.writeInt(hostId);
        dest.writeString(hostName);
        dest.writeFloat(hostRating);
        dest.writeInt(hostReviewCount);
        dest.writeString(hostProfilePicUrl);
        dest.writeFloat(houseRating);
        dest.writeInt(houseReviewCount);
        dest.writeString(houseThumbnailUrl);
        dest.writeIntArray(amenityIds);
        dest.writeStringArray(housePicUrls);
//        dest.writeTypedArray(reviews, 0);
        dest.writeTypedArray(reservations, 0);
    }

    protected VillimHouse(Parcel in) {
        houseId = in.readInt();
        houseName = in.readString();
        addrFull = in.readString();
        addrSummary = in.readString();
        addrDirection = in.readString();
        description = in.readString();
        houseType = in.readInt();
        numGuest = in.readInt();
        numBedroom = in.readInt();
        numBed = in.readInt();
        numBathroom = in.readInt();
        ratePerMonth = in.readInt();
        ratePerNight = in.readInt();
        utilityFee = in.readInt();
        cleaningFee = in.readInt();
        lockAddr = in.readInt();
        lockPc = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        housePolicy = in.readString();
        cancellationPolicy = in.readString();
        hostId = in.readInt();
        hostName = in.readString();
        hostRating = in.readFloat();
        hostReviewCount = in.readInt();
        hostProfilePicUrl = in.readString();
        houseRating = in.readFloat();
        houseReviewCount = in.readInt();
        houseThumbnailUrl = in.readString();
        amenityIds = in.createIntArray();
        housePicUrls = in.createStringArray();
//        reviews = in.createTypedArray(VillimReview.CREATOR);
        reservations = in.createTypedArray(VillimReservation.CREATOR);
    }

    public static final Creator<VillimHouse> CREATOR = new Creator<VillimHouse>() {
        @Override
        public VillimHouse createFromParcel(Parcel in) {
            return new VillimHouse(in);
        }

        @Override
        public VillimHouse[] newArray(int size) {
            return new VillimHouse[size];
        }
    };

    public Date[] getInvalidDates() {

        List<Date> dates = new ArrayList<>();

        for (int i = 0; i < reservations.length; ++i) {
            VillimReservation reservation = reservations[i];
            List<Date> datesBetween = net.villim.villim.VillimUtils.datesBetween(reservation.startDate, reservation.endDate, true);
            dates.addAll(datesBetween);
        }

        return dates.toArray(new Date[0]);
    }
}
