package net.villim.villim;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rojoxpress.slidebutton.SlideButton;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static net.villim.villim.VillimKeys.KEY_HOUSE_NAME;
import static net.villim.villim.VillimKeys.KEY_HOUSE_THUMBNAIL_URL;
import static net.villim.villim.VillimKeys.KEY_ID;
import static net.villim.villim.VillimKeys.KEY_MESSAGE;
import static net.villim.villim.VillimKeys.KEY_QUERY_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_RESERVATION_ACTIVE;
import static net.villim.villim.VillimKeys.KEY_ROOM_ID;
import static net.villim.villim.VillimKeys.KEY_VISIT_ID;
import static net.villim.villim.VillimKeys.KEY_VISIT_INFO;

public class VisitDetailActivity extends AppCompatActivity {

    private static final String MY_ROOM_URL = "http://www.mocky.io/v2/593df3e0110000f727722b11";

    private Toolbar toolbar;

    private ImageView houseThumbnail;
    private TextView houseNameText;
    private TextView visitTimeText;
    private TextView errorMessage;
    private Button bottomButton;

    private AVLoadingIndicatorView loadingIndicator;

    private VillimSession session;

    private VillimVisit visit;
    private String houseName;
    private String houseThumbnailUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_detail);

         /* Retrieve session. */
        session = new VillimSession(getApplicationContext());

        visit = getIntent().getParcelableExtra(getString(R.string.key_visit));

         /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow_dark));
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        /* House name & dates */
        houseNameText = (TextView) findViewById(R.id.house_name);
        visitTimeText = (TextView) findViewById(R.id.visit_time);

        /* House thumbnail */
        houseThumbnail = (ImageView) findViewById(R.id.house_thumbnail);

        /* Bottom Button */
        bottomButton = (Button) findViewById(R.id.bottom_button);
        bottomButton.setClickable(false);
        bottomButton.setEnabled(false);

        /* Error Message */
        errorMessage = (TextView) findViewById(R.id.error_message);
        errorMessage.setVisibility(View.INVISIBLE);

        /* Loading indicator */
        loadingIndicator = (AVLoadingIndicatorView) findViewById(R.id.loading_indicator);

        displayNoRoom();

        sendVisitInfoRequest();
    }

    private void sendVisitInfoRequest() {
        startLoadingAnimation();
        hideErrorMessage();

        OkHttpClient client = new OkHttpClient();

        URL url = new HttpUrl.Builder()
                .scheme("http")
                .host("www.mocky.io")
                .addPathSegments("v2/59442d6c1200002f0ffcb5a5")
                .addQueryParameter(KEY_VISIT_ID, Integer.toString(visit.visitId))
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
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.getBoolean(KEY_QUERY_SUCCESS)) {
                        JSONObject visitInfo = jsonObject.getJSONObject(KEY_VISIT_INFO);
                        visit = VillimVisit.createVisitFromJSONObject(visitInfo);
                        houseName = visitInfo.getString(KEY_HOUSE_NAME);
                        houseThumbnailUrl = visitInfo.getString(KEY_HOUSE_THUMBNAIL_URL);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                displayRoomInfo();
                            }
                        });
                    } else {
                        displayNoRoom();
                    }
                    stopLoadingAnimation();
                } catch (JSONException e) {
                    showErrorMessage(getString(R.string.open_room_error));
                    stopLoadingAnimation();
                }
            }
        });
    }

    public void displayRoomInfo() {
        /* Set up slide button */
        bottomButton.setText(getString(R.string.villim_request));
        bottomButton.setOnClickListener(null);

        /* House Name */
        houseNameText.setText(houseName);

        /* Visit time */
        visitTimeText.setText(visit.visitTime);

        /* House thumbnail */
        Glide.with(this)
                .load(houseThumbnailUrl)
                .into(houseThumbnail);
    }

    public void displayNoRoom() {

        /* House thumbnail */
        Glide.with(this)
                .load(R.drawable.img_default)
                .into(houseThumbnail);
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

    @Override
    public boolean onSupportNavigateUp() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
        return true;
    }


}
