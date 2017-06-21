package net.villim.villim;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static net.villim.villim.VillimKeys.KEY_HOST_ID;
import static net.villim.villim.VillimKeys.KEY_HOUSE_ID;
import static net.villim.villim.VillimKeys.KEY_MESSAGE;
import static net.villim.villim.VillimKeys.KEY_QUERY_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_RATING_ACCURACY;
import static net.villim.villim.VillimKeys.KEY_RATING_CHECKIN;
import static net.villim.villim.VillimKeys.KEY_RATING_CLEANLINESS;
import static net.villim.villim.VillimKeys.KEY_RATING_COMMUNICATION;
import static net.villim.villim.VillimKeys.KEY_RATING_LOCATION;
import static net.villim.villim.VillimKeys.KEY_RATING_OVERALL;
import static net.villim.villim.VillimKeys.KEY_RATING_VALUE;
import static net.villim.villim.VillimKeys.KEY_RESERVATION_ID;
import static net.villim.villim.VillimKeys.KEY_REVIEWER_ID;
import static net.villim.villim.VillimKeys.KEY_REVIEWER_NAME;
import static net.villim.villim.VillimKeys.KEY_REVIEWER_PROFILE_PIC_URL;
import static net.villim.villim.VillimKeys.KEY_REVIEWS;
import static net.villim.villim.VillimKeys.KEY_REVIEW_CONTENT;
import static net.villim.villim.VillimKeys.KEY_REVIEW_DATE;

public class VillimReview implements Parcelable {
    private static VillimReview[] reviewArrayFromServer;

    /* Id는 db속의 id. Username 아님. */
    public int houseId;
    public int hostId;
    public int reviewerId;
    public int reservationId;
    public String reviewerName;
    public String reviewContent;
    public String reviewerProfilePicUrl;
    public Date reviewDate;
    public float overAllRating;
    public float accuracyRating;
    public float communicationRating;
    public float cleanlinessRating;
    public float locationRating;
    public float checkinRating;
    public float valueRating;


    public VillimReview() {

    }

    public VillimReview(int houseId, int hostId, int reviewerId, int reservationId, String reviewerName, String review, float rating) {
        this.houseId = houseId;
        this.hostId = hostId;
        this.reviewerId = reviewerId;
        this.reservationId = reservationId;
        this.reviewerName = reviewerName;
        this.reviewContent = review;
        this.overAllRating = rating;
    }

    protected VillimReview(Parcel in) {
        houseId = in.readInt();
        hostId = in.readInt();
        reviewerId = in.readInt();
        reservationId = in.readInt();
        reviewerName = in.readString();
        reviewContent = in.readString();
        reviewerProfilePicUrl = in.readString();
        reviewDate = (Date) in.readSerializable();
        overAllRating = in.readFloat();
        accuracyRating = in.readFloat();
        communicationRating = in.readFloat();
        cleanlinessRating = in.readFloat();
        locationRating = in.readFloat();
        checkinRating = in.readFloat();
        valueRating = in.readFloat();
    }

    public static final Creator<VillimReview> CREATOR = new Creator<VillimReview>() {
        @Override
        public VillimReview createFromParcel(Parcel in) {
            return new VillimReview(in);
        }

        @Override
        public VillimReview[] newArray(int size) {
            return new VillimReview[size];
        }
    };


    public static VillimReview createReviewFromJSONObject(JSONObject reviewInfo) {
        /* Create user instance */
        VillimReview review = new VillimReview();

         /* No need to null check here because if we dont set it, it's going to be null anyway */
        try {
            review.houseId = reviewInfo.getInt(KEY_HOUSE_ID);
            review.hostId = reviewInfo.getInt(KEY_HOST_ID);
            review.reviewerId = reviewInfo.getInt(KEY_REVIEWER_ID);
            review.reservationId = reviewInfo.getInt(KEY_RESERVATION_ID);
            review.reviewerName = reviewInfo.getString(KEY_REVIEWER_NAME);
            review.reviewContent = reviewInfo.getString(KEY_REVIEW_CONTENT);
            boolean isReviewerProfilePicUrlNull = reviewInfo.isNull(KEY_REVIEWER_PROFILE_PIC_URL);
            review.reviewerProfilePicUrl = isReviewerProfilePicUrlNull ? null : reviewInfo.getString(KEY_REVIEWER_PROFILE_PIC_URL);
            review.reviewDate = VillimUtil.dateFromDateString(reviewInfo.getString(KEY_REVIEW_DATE));
            review.overAllRating = (float) reviewInfo.getDouble(KEY_RATING_OVERALL);
            review.accuracyRating = (float) reviewInfo.getDouble(KEY_RATING_ACCURACY);
            review.communicationRating = (float) reviewInfo.getDouble(KEY_RATING_COMMUNICATION);
            review.cleanlinessRating = (float) reviewInfo.getDouble(KEY_RATING_CLEANLINESS);
            review.locationRating = (float) reviewInfo.getDouble(KEY_RATING_LOCATION);
            review.checkinRating = (float) reviewInfo.getDouble(KEY_RATING_CHECKIN);
            review.valueRating = (float) reviewInfo.getDouble(KEY_RATING_VALUE);
        } catch (JSONException e) {

        }
        return review;
    }

    public static VillimReview[] reviewArrayFromJsonArray(JSONArray jsonArray) {

        VillimReview[] reviews = new VillimReview[jsonArray.length()];

        try {
            for (int i = 0; i < jsonArray.length(); ++i) {
                VillimReview review = createReviewFromJSONObject(jsonArray.getJSONObject(i));
                reviews[i] = review;
            }

        } catch (JSONException e) {

        }
        return reviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(houseId);
        dest.writeInt(hostId);
        dest.writeInt(reviewerId);
        dest.writeInt(reservationId);
        dest.writeString(reviewerName);
        dest.writeString(reviewContent);
        dest.writeString(reviewerProfilePicUrl);
        dest.writeSerializable(reviewDate);
        dest.writeFloat(overAllRating);
        dest.writeFloat(accuracyRating);
        dest.writeFloat(communicationRating);
        dest.writeFloat(cleanlinessRating);
        dest.writeFloat(locationRating);
        dest.writeFloat(checkinRating);
        dest.writeFloat(valueRating);
    }
}
