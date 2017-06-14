package net.villim.villim;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import static net.villim.villim.VillimKeys.KEY_HOST_ID;
import static net.villim.villim.VillimKeys.KEY_HOUSE_ID;
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
import static net.villim.villim.VillimKeys.KEY_REVIEWER_PROFILE_URL;
import static net.villim.villim.VillimKeys.KEY_REVIEW_CONTENT;

public class VillimReview implements Parcelable {
    /* Id는 db속의 id. Username 아님. */
    public int houseId;
    public int hostId;
    public int reviewerId;
    public int reservationId;
    public String reviewerName;
    public String reviewContent;
    public String reviewerProfilePicUrl;
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
            boolean isReviewerProfilePicUrlNull = reviewInfo.isNull(KEY_REVIEWER_PROFILE_URL);
            review.reviewerProfilePicUrl = isReviewerProfilePicUrlNull ? null : reviewInfo.getString(KEY_REVIEWER_PROFILE_URL);
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

    public static VillimReview[] getHouseReviewsFromServer(int houseId) {
        VillimReview review = new VillimReview(0, 0, 0, 0, "리뷰어 이름", "첫인상으로 많은 것이 결정되고 마케팅이 신뢰를 잃어가는 요즘과 같은 시대에서는, " +
                "앱 마켓의 별점과 리뷰가 앱 다운로드 여부를 결정하는 중요한 요인입니다. " +
                "최근 Apptentive에서 실시한 유저 관련 조사에 따르면, 유저 92%가 새로운 앱을 다운로드할 때 별점을 고려한다는 결과가 나왔습니다. " +
                "뿐만 아니라 42%는 주변 지인의 추천보다도 별점을 더 신뢰한다고 하는데요. 높은 별점과 리뷰는 유저들에게 해당 앱이 좋은 앱이라는 인상을 심어주기 때문에 소비자들로부터 거부감을 줄", 2.3f);
        VillimReview[] reviews = new VillimReview[]{review, review, review, review, review, review, review, review, review};
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
        dest.writeFloat(overAllRating);
        dest.writeFloat(accuracyRating);
        dest.writeFloat(communicationRating);
        dest.writeFloat(cleanlinessRating);
        dest.writeFloat(locationRating);
        dest.writeFloat(checkinRating);
        dest.writeFloat(valueRating);
    }
}
