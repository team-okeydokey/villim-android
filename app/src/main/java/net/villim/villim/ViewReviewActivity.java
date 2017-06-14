package net.villim.villim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static net.villim.villim.VillimKeys.KEY_HOUSE_ID;
import static net.villim.villim.VillimKeys.KEY_MESSAGE;
import static net.villim.villim.VillimKeys.KEY_QUERY_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_RATING_OVERALL;
import static net.villim.villim.VillimKeys.KEY_REVIEWS;
import static net.villim.villim.VillimKeys.KEY_REVIEW_COUNT;

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

    private AVLoadingIndicatorView loadingIndicator;

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

        /* Loading indicator */
        loadingIndicator = (AVLoadingIndicatorView) findViewById(R.id.loading_indicator);

        sendGetHouseReviewRequest();
    }

    private void sendGetHouseReviewRequest() {
        startLoadingAnimation();
        hideErrorMessage();

        OkHttpClient client = new OkHttpClient();

//        URL url = new HttpUrl.Builder()
//                .scheme(SERVER_SCHEME)
//                .host(SERVER_HOST)
//                .addPathSegments("a/host-info")
//                .addQueryParameter(KEY_HOST_ID, getIntent().getStringExtra(KEY_HOST_ID))
//                .build().url();

        URL url = new HttpUrl.Builder()
                .scheme("http")
                .host("www.mocky.io")
                .addPathSegments("v2/594175a00f0000770ec63237")
                .addQueryParameter(KEY_HOUSE_ID, Integer.toString(getIntent().getIntExtra(KEY_HOUSE_ID, 0)))
                .build().url();

        Request request = new Request.Builder()
                .url(url)
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
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.getBoolean(KEY_QUERY_SUCCESS)) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateView(jsonObject);
                            }
                        });

                    } else {
                        showErrorMessage(jsonObject.getString(KEY_MESSAGE));
                    }
                    stopLoadingAnimation();
                } catch (JSONException e) {
                    showErrorMessage(getString(R.string.server_error));
                    stopLoadingAnimation();
                }
            }
        });
    }

    private void populateView(JSONObject jsonObject) {

        try {

            /* Indicate review count in title. */
            reviewCount.setText(String.format(getString(R.string.review_count_format), jsonObject.getInt(KEY_REVIEW_COUNT)));

            /* Add ratings */
            overAllRatingBar.setRating((float)jsonObject.getDouble(KEY_RATING_OVERALL));
            JSONArray reviewsArray = jsonObject.getJSONArray(KEY_REVIEWS);
            

        } catch (JSONException e) {

        }


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
