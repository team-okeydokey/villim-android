package net.villim.villim;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static net.villim.villim.VillimKeys.HOUSE_REVIEW_URL;
import static net.villim.villim.VillimKeys.KEY_HOUSE_ID;
import static net.villim.villim.VillimKeys.KEY_MESSAGE;
import static net.villim.villim.VillimKeys.KEY_QUERY_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_RATING_ACCURACY;
import static net.villim.villim.VillimKeys.KEY_RATING_CHECKIN;
import static net.villim.villim.VillimKeys.KEY_RATING_CLEANLINESS;
import static net.villim.villim.VillimKeys.KEY_RATING_COMMUNICATION;
import static net.villim.villim.VillimKeys.KEY_RATING_LOCATION;
import static net.villim.villim.VillimKeys.KEY_RATING_OVERALL;
import static net.villim.villim.VillimKeys.KEY_RATING_VALUE;
import static net.villim.villim.VillimKeys.KEY_REVIEWS;
import static net.villim.villim.VillimKeys.KEY_REVIEW_COUNT;
import static net.villim.villim.VillimKeys.SERVER_HOST;
import static net.villim.villim.VillimKeys.SERVER_SCHEME;

public class ViewReviewActivity extends VillimActivity {

    private Toolbar toolbar;
    private Button closeButton;

    private TextView reviewCount;

    private RatingBar overAllRatingBar;
    private RatingBar accuracyRatingBar;
    private RatingBar communicationRatingBar;
    private RatingBar cleanlinessRatingBar;
    private RatingBar locationRatingBar;
    private RatingBar checkinRatingBar;
    private RatingBar valueRatingBar;

    private ListView reviewListView;

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
        accuracyRatingBar = (RatingBar) findViewById(R.id.accuracy_ratingbar);
        communicationRatingBar = (RatingBar) findViewById(R.id.communication_ratingbar);
        cleanlinessRatingBar = (RatingBar) findViewById(R.id.cleanliness_ratingbar);
        locationRatingBar = (RatingBar) findViewById(R.id.location_ratingbar);
        checkinRatingBar = (RatingBar) findViewById(R.id.checkin_ratingbar);
        valueRatingBar = (RatingBar) findViewById(R.id.value_ratingbar);

        /* Review list */
        reviewListView = (ListView) findViewById(R.id.review_listview);

        /* Loading indicator */
        loadingIndicator = (AVLoadingIndicatorView) findViewById(R.id.loading_indicator);

        sendGetHouseReviewRequest();
    }

    private void sendGetHouseReviewRequest() {
        startLoadingAnimation();
        hideErrorMessage();

        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();


//        URL url = new HttpUrl.Builder()
//                .scheme(SERVER_SCHEME)
//                .host(SERVER_HOST)
//                .addPathSegments("a/host-info")
//                .addQueryParameter(KEY_HOST_ID, getIntent().getStringExtra(KEY_HOST_ID))
//                .build().url();

        URL url = new HttpUrl.Builder()
                .scheme(SERVER_SCHEME)
                .host(SERVER_HOST)
                .addPathSegments(HOUSE_REVIEW_URL)
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

            /* Add ratings */
            overAllRatingBar.setRating((float) jsonObject.getDouble(KEY_RATING_OVERALL));
            accuracyRatingBar.setRating((float) jsonObject.getDouble(KEY_RATING_ACCURACY));
            communicationRatingBar.setRating((float) jsonObject.getDouble(KEY_RATING_COMMUNICATION));
            cleanlinessRatingBar.setRating((float) jsonObject.getDouble(KEY_RATING_CLEANLINESS));
            locationRatingBar.setRating((float) jsonObject.getDouble(KEY_RATING_LOCATION));
            checkinRatingBar.setRating((float) jsonObject.getDouble(KEY_RATING_CHECKIN));
            valueRatingBar.setRating((float) jsonObject.getDouble(KEY_RATING_VALUE));


            JSONArray reviewsArray = jsonObject.getJSONArray(KEY_REVIEWS);

            /* Indicate review count in title. */
            reviewCount.setText(String.format(getString(R.string.review_count_format), reviewsArray.length()));

            VillimReview[] reviews = VillimReview.reviewArrayFromJsonArray(reviewsArray);
            ReviewAdapter adapter = new ReviewAdapter(this, reviews);
            reviewListView.setAdapter(adapter);

        } catch (JSONException e) {

        }
    }


    private class ReviewAdapter extends BaseAdapter {

        private Context context;
        private VillimReview[] reviews;

        public ReviewAdapter(Context ctx, VillimReview[] items) {
            context = ctx;
            reviews = items;
        }

        @Override
        public int getCount() {
            return reviews.length;
        }

        @Override
        public Object getItem(int position) {
            return reviews[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.review_list_item, parent, false);
            }

            VillimReview review = (VillimReview) getItem(position);

            ImageView reviewerProfilePic = (ImageView) view.findViewById(R.id.reviewer_profile_pic);
            TextView reviewerName = (TextView) view.findViewById(R.id.reviewer_name);
            TextView reviewDate = (TextView) view.findViewById(R.id.review_date);
            TextView reviewContent = (TextView) view.findViewById(R.id.review_content);

            Glide.with(getApplicationContext()).load(review.reviewerProfilePicUrl).into(reviewerProfilePic);
            reviewerName.setText(review.reviewerName);
            String reviewDateString = String.format(getString(R.string.review_date_format),
                    review.reviewDate.getYear()+1900,
                    review.reviewDate.getMonth() + 1);
            reviewDate.setText(reviewDateString);
            reviewContent.setText(review.reviewContent);

            return view;
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
//                errorMessage.setText(message);
//                errorMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideErrorMessage() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                errorMessage.setVisibility(View.INVISIBLE);
//            }
//        });
    }
}
