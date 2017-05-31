package net.villim.villim;


public class VillimReview {
    /* Id는 db속의 id. Username 아님. */
    public int roomId;
    public int hostId;
    public int reviewerId;
    public String review;
    public float rating;

    public static VillimReview getReviewFromServer(int id) {
        VillimReview review = new VillimReview();
        review.review = "리뷰리뷰리뷰리뷰";
        review.rating = 3.5f;
        return review;
    }
}
