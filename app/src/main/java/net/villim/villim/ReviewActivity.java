package net.villim.villim;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static net.villim.villim.RateActivity.ACCURACY;
import static net.villim.villim.RateActivity.CHECKIN;
import static net.villim.villim.RateActivity.CLEANLINESS;
import static net.villim.villim.RateActivity.COMMUNICATION;
import static net.villim.villim.RateActivity.LOCATION;
import static net.villim.villim.RateActivity.RATE;
import static net.villim.villim.RateActivity.VALUE;
import static net.villim.villim.VillimKeys.KEY_HOUSE_ID;
import static net.villim.villim.VillimKeys.KEY_MESSAGE;
import static net.villim.villim.VillimKeys.KEY_RATING_ACCURACY;
import static net.villim.villim.VillimKeys.KEY_RATING_CHECKIN;
import static net.villim.villim.VillimKeys.KEY_RATING_CLEANLINESS;
import static net.villim.villim.VillimKeys.KEY_RATING_COMMUNICATION;
import static net.villim.villim.VillimKeys.KEY_RATING_LOCATION;
import static net.villim.villim.VillimKeys.KEY_RATING_VALUE;
import static net.villim.villim.VillimKeys.KEY_REVIEW_CONTENT;
import static net.villim.villim.VillimKeys.KEY_ROOM_ID;
import static net.villim.villim.VillimKeys.POST_REVIEW_URL;
import static net.villim.villim.VillimKeys.SERVER_HOST;
import static net.villim.villim.VillimKeys.SERVER_SCHEME;
import static net.villim.villim.VillimKeys.KEY_POST_SUCCESS;

public class ReviewActivity extends VillimActivity {

    private Toolbar toolbar;
    private RelativeLayout reviewRatingView;
    private RatingBar overallRatingBar;
    private EditText reviewContent;
    private Button submitButton;

    private TextView errorMessage;
    private AVLoadingIndicatorView loadingIndicator;

    private int houseId;
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

        houseId = getIntent().getIntExtra(KEY_HOUSE_ID, -1);

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
                ratingIntent.putExtra(ACCURACY, accuracyRating);
                ratingIntent.putExtra(LOCATION, locationRating);
                ratingIntent.putExtra(COMMUNICATION, communicationRating);
                ratingIntent.putExtra(CHECKIN, checkinRating);
                ratingIntent.putExtra(CLEANLINESS, cleanlinessRating);
                ratingIntent.putExtra(VALUE, valueRating);
                startActivityForResult(ratingIntent, RATE);
            }
        });

        /* Review content */
        reviewContent = (EditText) findViewById(R.id.review_content_field);

         /* Error Message */
        errorMessage = (TextView) findViewById(R.id.error_message);
        errorMessage.setVisibility(View.INVISIBLE);

         /* Loading indicator */
        loadingIndicator = (AVLoadingIndicatorView) findViewById(R.id.loading_indicator);

        /* Button */
        submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPostReviewRequest();
            }
        });

    }

    private void sendPostReviewRequest() {
        startLoadingAnimation();
        hideErrorMessage();

        CookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

        OkHttpClient client = new OkHttpClient.Builder().cookieJar(cookieJar).build();

        RequestBody requestBody = new FormBody.Builder()
                .add(KEY_HOUSE_ID, Integer.toString(houseId))
                .add(KEY_REVIEW_CONTENT, reviewContent.getText().toString().trim())
                .add(KEY_RATING_ACCURACY, Float.toString(accuracyRating))
                .add(KEY_RATING_LOCATION, Float.toString(locationRating))
                .add(KEY_RATING_COMMUNICATION, Float.toString(communicationRating))
                .add(KEY_RATING_CHECKIN, Float.toString(checkinRating))
                .add(KEY_RATING_CLEANLINESS, Float.toString(cleanlinessRating))
                .add(KEY_RATING_VALUE, Float.toString(valueRating))
                .build();

        URL url = new HttpUrl.Builder()
                .scheme(SERVER_SCHEME)
                .host(SERVER_HOST)
                .addPathSegments(POST_REVIEW_URL)
                .build().url();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Something went wrong.
                showErrorMessage(getString(R.string.cant_connect_to_server));
                stopLoadingAnimation();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    showErrorMessage(getString(R.string.server_error));
                    stopLoadingAnimation();
                    throw new IOException("Response not successful   " + response);
                }
                /* Request success. */
                try {
                    /* 주의: response.body().string()은 한 번 부를 수 있음 */
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.getBoolean(KEY_POST_SUCCESS)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        getString(R.string.review_posted),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                        finish();
                    } else {
                        showErrorMessage(jsonObject.getString(KEY_MESSAGE));
                        stopLoadingAnimation();
                    }
                } catch (JSONException e) {
                    showErrorMessage(getString(R.string.server_error));
                    stopLoadingAnimation();
                }
            }
        });
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

    public void startLoadingAnimation() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingIndicator.smoothToShow();
            }
        });
    }

    public void stopLoadingAnimation() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingIndicator.smoothToHide();
            }
        });
    }

    public void showErrorMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                errorMessage.setText(message);
                errorMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideErrorMessage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                errorMessage.setVisibility(View.INVISIBLE);
            }
        });
    }

}
