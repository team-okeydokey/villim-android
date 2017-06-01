package net.villim.villim;

import android.graphics.PorterDuff;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class RoomDetailActivity extends AppCompatActivity {

    private VillimRoom house;

    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView toolbarImage;

    private ImageView hostProfilePic;
    private TextView hostName;
    private RatingBar hostRating;
    private TextView hostReviewCount;

    private TextView houseName;
    private TextView houseAddress;

    private TextView numGuest;
    private TextView numBedroom;
    private TextView numBed;
    private TextView numBathroom;

    private TextView description;
    private Button descriptionSeeMore;

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
                if (verticalOffset == -appBarLayout.getTotalScrollRange()) {
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

        /* Review */

        /* Map */

        /* Extract room info and fill view elements with data */
        house = extractRoomInfo();
        populateView();

    }

    // Extract room info.
    private VillimRoom extractRoomInfo() {
        Bundle args = getIntent().getExtras();
        house = args.getParcelable(getString(R.string.key_house));
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

        /* Amenities */

        /* Review */

        /* Map */

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
