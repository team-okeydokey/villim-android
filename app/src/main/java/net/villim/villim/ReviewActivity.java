package net.villim.villim;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import static net.villim.villim.RateActivity.ACCURACY;
import static net.villim.villim.RateActivity.CHECKIN;
import static net.villim.villim.RateActivity.CLEANLINESS;
import static net.villim.villim.RateActivity.COMMUNICATION;
import static net.villim.villim.RateActivity.LOCATION;
import static net.villim.villim.RateActivity.RATE;
import static net.villim.villim.RateActivity.VALUE;

public class ReviewActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RelativeLayout reviewRatingView;
    private RatingBar overallRatingBar;
    private Button submitButton;

    private float overAllRating;
    private float accuracyRating;
    private float locationRating;
    private float communicationRating;
    private float checkinRating;
    private float cleanlinessRating;
    private float valueRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow_dark));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Rating view */
        reviewRatingView = (RelativeLayout) findViewById(R.id.review_rating_view);
        overallRatingBar = (RatingBar) findViewById(R.id.overall_ratingbar);

        reviewRatingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ratingIntent = new Intent(ReviewActivity.this, RateActivity.class);
                startActivityForResult(ratingIntent, RATE);
            }
        });

        /* Button */
        submitButton = (Button) findViewById(R.id.submit_button);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /* Requests to reservation activity */
        if (requestCode == RATE) {
            if (resultCode == Activity.RESULT_OK) {
                // Extract info.
                accuracyRating = data.getFloatExtra(ACCURACY, -1);
                locationRating = data.getFloatExtra(LOCATION, -1);
                communicationRating = data.getFloatExtra(COMMUNICATION, -1);
                checkinRating = data.getFloatExtra(CHECKIN, -1);
                cleanlinessRating = data.getFloatExtra(CLEANLINESS, -1);
                valueRating = data.getFloatExtra(VALUE, -1);

                updateRating();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void updateRating() {
        overAllRating = getAverageRating();
        overallRatingBar.setRating(overAllRating);
    }

    private float getAverageRating() {
        float average = (accuracyRating + locationRating + communicationRating + checkinRating + cleanlinessRating + valueRating) / 6;
        return average;
    }

}
