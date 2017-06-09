package net.villim.villim;


import android.os.Parcel;
import android.os.Parcelable;

public class VillimReview implements Parcelable {
    /* Id는 db속의 id. Username 아님. */
    public int houseId;
    public int hostId;
    public int reviewerId;
    public int reservationId;
    public String reviewerName;
    public String review;
    public float rating;

    public VillimReview() {

    }

    public VillimReview(int houseId, int hostId, int reviewerId, int reservationId, String reviewerName, String review, float rating) {
        this.houseId = houseId;
        this.hostId = hostId;
        this.reviewerId = reviewerId;
        this.reservationId = reservationId;
        this.reviewerName = reviewerName;
        this.review = review;
        this.rating = rating;
    }

    protected VillimReview(Parcel in) {
        houseId = in.readInt();
        hostId = in.readInt();
        reviewerId = in.readInt();
        reservationId = in.readInt();
        reviewerName = in.readString();
        review = in.readString();
        rating = in.readFloat();
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

    public static VillimReview getReviewFromServer(int reviewId) {
        VillimReview review = new VillimReview();
        review.review = "리뷰리뷰리뷰리뷰";
        review.rating = 3.5f;
        return review;
    }

    public static VillimReview[] gerRoomReviewsFromServer(int houseId) {
        VillimReview review = new VillimReview(0,0,0,0, "리뷰어 이름","첫인상으로 많은 것이 결정되고 마케팅이 신뢰를 잃어가는 요즘과 같은 시대에서는, " +
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
        dest.writeString(review);
        dest.writeFloat(rating);
    }
}
