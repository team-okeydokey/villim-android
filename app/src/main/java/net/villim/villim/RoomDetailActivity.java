package net.villim.villim;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static net.villim.villim.HouseDescriptionActivity.KEY_BASIC_DESCRIPTION;


public class RoomDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final static int MAX_AMENITY_ICONS = 6;

    private VillimRoom house;

    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView toolbarImage;

    private ImageView hostProfilePic;
    private TextView hostName;
    private RatingBar hostRating;
    private TextView hostReviewCount;
    private Button contactHostButton;

    private TextView houseName;
    private TextView houseAddress;

    private TextView numGuest;
    private TextView numBedroom;
    private TextView numBed;
    private TextView numBathroom;

    private TextView description;
    private Button descriptionSeeMore;

    private LinearLayoutCompat amenityIcons;

    private RelativeLayout reviewBody;
    private ImageView reviewerProfilePic;
    private TextView reviewerName;
    private RatingBar reviewerRating;
    private TextView reviewContent;
    private TextView seeMoreReviews;
    private RatingBar houseRating;

    FrameLayout mapContainer;
    MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        toolbarImage = (ImageView) findViewById(R.id.toolbar_image);

        /* Toolbar. */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow_light));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Change back button color on collapse. */
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == -appBarLayout.getTotalScrollRange()) { // Completely collapsed.
                    collapsingToolbarLayout.setTitle(getString(R.string.room_detail_title));
                    toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.arrow_dark), PorterDuff.Mode.SRC_ATOP);
                } else {
                    collapsingToolbarLayout.setTitle(" ");
                    toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.arrow_light), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        /* View elements containing room info. */

        /* Host Info */
        hostProfilePic = (ImageView) findViewById(R.id.host_profile_pic);
        hostName = (TextView) findViewById(R.id.host_name);
        hostRating = (RatingBar) findViewById(R.id.host_review_rating);
        hostReviewCount = (TextView) findViewById(R.id.host_review_count);
        contactHostButton = (Button) findViewById(R.id.contact_host_button);

        /* House Name and address */
        houseName = (TextView) findViewById(R.id.house_name);
        houseAddress = (TextView) findViewById(R.id.house_address);

        /* Icons with house info */
        numGuest = (TextView) findViewById(R.id.num_guest);
        numBedroom = (TextView) findViewById(R.id.num_bedroom);
        numBed = (TextView) findViewById(R.id.num_bed);
        numBathroom = (TextView) findViewById(R.id.num_bathroom);

        /* House description */
        description = (TextView) findViewById(R.id.description);
        descriptionSeeMore = (Button) findViewById(R.id.description_see_more);

        /* Amenities */
        amenityIcons = (LinearLayoutCompat) findViewById(R.id.amenity_icons);

        /* Review */
        reviewBody = (RelativeLayout) findViewById(R.id.review_body);
        reviewerProfilePic = (ImageView) findViewById(R.id.reviewer_profile_pic);
        reviewerName = (TextView) findViewById(R.id.reviewer_name);
        reviewerRating = (RatingBar) findViewById(R.id.reviewer_rating);
        reviewContent = (TextView) findViewById(R.id.review_content);
        seeMoreReviews = (TextView) findViewById(R.id.see_more_reviews);
        houseRating = (RatingBar) findViewById(R.id.house_rating);

        /* Map */
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /* Extract room info and fill view elements with data */
        house = extractRoomInfo();

        populateView();
    }

    // Extract room info.
    private VillimRoom extractRoomInfo() {
        Bundle args = getIntent().getExtras();
        house = args.getParcelable(getString(R.string.key_house));
        house.reviews = VillimReview.getHouseReviewsFromServer(house.houseId);
        return house;
    }


    // Make this async.
    private void populateView() {
        /* Room picture */
        Glide.with(this)
                .load(R.drawable.prugio_thumbnail)
                .into(toolbarImage);

        /* Host profile pic */
        Glide.with(this)
                .load(R.drawable.prugio_thumbnail)
                .into(hostProfilePic);

        /* Host name and rating */
        hostName.setText(house.hostName);
        hostRating.setRating(house.hostRating);
        String countText = String.format(getString(R.string.review_count_text), house.hostReviewCount);
        hostReviewCount.setText(countText);

        /* House Name and address */
        houseName.setText(house.houseName);
        houseAddress.setText(house.addrSummary);

        /* Icons with house info */
        String numGuestText = String.format(getString(R.string.num_guest_format, house.numGuest));
        numGuest.setText(numGuestText);
        String numBedroomText = String.format(getString(R.string.num_bedroom_format, house.numBedroom));
        numBedroom.setText(numBedroomText);
        String numBedText = String.format(getString(R.string.num_bed_format, house.numBed));
        numBed.setText(numBedText);
        String numBathroomText = String.format(getString(R.string.num_bathroom_format, house.numBathroom));
        numBathroom.setText(numBathroomText);

        /* Description */
        description.setText(house.description);
        description.post(new Runnable() {
            @Override
            public void run() {
                if (description.getLineCount() > description.getMaxLines()) {
                    descriptionSeeMore.setVisibility(View.VISIBLE);
                } else {
                    descriptionSeeMore.setVisibility(View.GONE);
                }

            }
        });
        descriptionSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomDetailActivity.this, HouseDescriptionActivity.class);
                intent.putExtra(KEY_BASIC_DESCRIPTION, house.description);
                startActivity(intent);
            }
        });

        /* Amenities */
        populateAmenityIcons();

        /* Reviews */
        populateReviews();

        /* Map */


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(house.latitude, house.longitude)).title("Marker"));
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(house.latitude, house.longitude)));
        map.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
    }

    private class PopulateHouseDetailTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            populateView();
            return null;
        }

        protected void onProgressUpdate(Void... voids) {

        }

        protected void onPostExecute() {

        }
    }

    private void populateAmenityIcons() {
        int[] amenityIds = house.amenityIds;

        /* Displayed icons should be one less than MAX_AMENITY_ICONS if there are more amenities
           than MAX_AMENITY_ICONS, to make room for the "see more" button. */
        int numIcons = amenityIds.length > MAX_AMENITY_ICONS ? MAX_AMENITY_ICONS - 1 : amenityIds.length;

        /* Add icons. */
        for (int i = 0; i < numIcons; ++i) {
            ImageView iconView = new ImageView(this);
            iconView.setImageResource(VillimAmenity.getAmenityDrawableResourceId(amenityIds[i]));
            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(
                    (int) getResources().getDimension(R.dimen.amenity_icon_width),
                    (int) getResources().getDimension(R.dimen.amenity_icon_height),
                    1.0f
            );
            iconView.setLayoutParams(params);
            amenityIcons.addView(iconView);
        }

        /* Add "see more" button, if applicable. */
        if (amenityIds.length > MAX_AMENITY_ICONS) {
            TextView seeMoreTextView = new TextView(this);
            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(
                    (int) getResources().getDimension(R.dimen.amenity_icon_width),
                    (int) getResources().getDimension(R.dimen.amenity_icon_height),
                    1.0f
            );
            seeMoreTextView.setLayoutParams(params);
            String seeMoreText = String.format(getString(R.string.amenities_see_more_format), amenityIds.length - numIcons);
            seeMoreTextView.setText(seeMoreText);
            seeMoreTextView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            seeMoreTextView.setGravity(Gravity.CENTER_VERTICAL);
            amenityIcons.addView(seeMoreTextView);
        }

    }

    private void populateReviews() {
        VillimReview[] reviews = house.reviews;

        if (reviews == null || reviews.length == 0) {
            /* Remove the whole review section andreplace it with "No review" textview. */
            LinearLayoutCompat parent = (LinearLayoutCompat) reviewBody.getParent();
            int index = parent.indexOfChild(reviewBody);
            parent.removeView(reviewBody);

            TextView noReviewTextView = new TextView(this);
            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            noReviewTextView.setLayoutParams(params);
            noReviewTextView.setText(getString(R.string.reviews_no_review));
            noReviewTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            noReviewTextView.setGravity(Gravity.CENTER_VERTICAL);
            noReviewTextView.setPadding(0, 0, 0, (int) getResources().getDimension(R.dimen.room_detail_activity_padding));
            parent.addView(noReviewTextView, index);

        } else {

            /* There is at least 1 review. Load reviewer info. */
            VillimReview firstReview = reviews[0];
            Glide.with(this)
                    .load(R.drawable.prugio_thumbnail)
                    .into(reviewerProfilePic);
            reviewerName.setText(firstReview.reviewerName);
            reviewerRating.setRating(firstReview.rating);
            reviewContent.setText(firstReview.review);
            houseRating.setRating(house.houseRating);

            if (reviews.length == 1) {
                /* Be away with "see more" if there is only one review. Also, shift house rating bar to the left. */
                seeMoreReviews.setVisibility(View.GONE);
                RelativeLayout.LayoutParams houseRatingParams = (RelativeLayout.LayoutParams) houseRating.getLayoutParams();
                houseRatingParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                houseRating.setLayoutParams(houseRatingParams);
            } else {
                /* Activate "see more" button with appropriate text. */
                String seeMoreReviewsText = String.format(getString(R.string.reviews_see_more_format), reviews.length - 1);
                seeMoreReviews.setText(seeMoreReviewsText);
            }
        }
    }

}
