package net.villim.villim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class ViewReviewActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button closeButton;

    private TextView reviewCount;

    private RatingBar overAllRatingBar;
    private RatingBar accuracyRatingbar;
    private RatingBar communicationRatingBar;
    private RatingBar cleanlinessRatingBar;
    private RatingBar locationRatingBar;
    private RatingBar checkinRatingBar;
    private RatingBar valueRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_review);

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Back button */
        closeButton = (Button) findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /* Title */
        reviewCount = (TextView) findViewById(R.id.review_count);

        /* Rating bars */
        overAllRatingBar = (RatingBar) findViewById(R.id.overall_ratingbar);
        accuracyRatingbar = (RatingBar) findViewById(R.id.accuracy_ratingbar);
        communicationRatingBar = (RatingBar) findViewById(R.id.communication_ratingbar);
        cleanlinessRatingBar = (RatingBar) findViewById(R.id.cleanliness_ratingbar);
        locationRatingBar = (RatingBar) findViewById(R.id.location_ratingbar);
        checkinRatingBar = (RatingBar) findViewById(R.id.checkin_ratingbar);
        valueRatingBar = (RatingBar) findViewById(R.id.value_ratingbar);

        sendGetHouseReviewRequest();
    }

    private void sendGetHouseReviewRequest() {
        
    }
}
