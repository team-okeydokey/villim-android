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

public class RateActivity extends AppCompatActivity {
    public static final int RATE = 0;
    public static final String ACCURACY = "accuracy";
    public static final String LOCATION = "location";
    public static final String COMMUNICATION = "communication";
    public static final String CHECKIN = "checkin";
    public static final String CLEANLINESS = "cleanliness";
    public static final String VALUE = "value";

    private Toolbar toolbar;
    private RatingBar overAllRatingBar;
    private RatingBar accuracyRatingBar;
    private RatingBar communicationRatingBar;
    private RatingBar cleanlinessRatingBar;
    private RatingBar locationRatingBar;
    private RatingBar checkinRatingBar;
    private RatingBar valueRatingBar;
    private Button applyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow_dark));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Rating bars */
        overAllRatingBar = (RatingBar) findViewById(R.id.overall_ratingbar);
        accuracyRatingBar = (RatingBar) findViewById(R.id.accuracy_ratingbar);
        communicationRatingBar = (RatingBar) findViewById(R.id.communication_ratingbar);
        cleanlinessRatingBar = (RatingBar) findViewById(R.id.cleanliness_ratingbar);
        locationRatingBar = (RatingBar) findViewById(R.id.location_ratingbar);
        checkinRatingBar = (RatingBar) findViewById(R.id.checkin_ratingbar);
        valueRatingBar = (RatingBar) findViewById(R.id.value_ratingbar);

        /* Bottom button */
        applyButton = (Button) findViewById(R.id.apply_button);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(ACCURACY, accuracyRatingBar.getRating());
                returnIntent.putExtra(LOCATION, locationRatingBar.getRating());
                returnIntent.putExtra(COMMUNICATION, communicationRatingBar.getRating());
                returnIntent.putExtra(CHECKIN, checkinRatingBar.getRating());
                returnIntent.putExtra(CLEANLINESS, cleanlinessRatingBar.getRating());
                returnIntent.putExtra(VALUE, valueRatingBar.getRating());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
        return true;
    }
}
