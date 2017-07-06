package net.villim.villim;

import android.app.Activity;
import android.app.DialogFragment;
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
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
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

import static net.villim.villim.CalendarActivity.END_DATE;
import static net.villim.villim.CalendarActivity.START_DATE;
import static net.villim.villim.MainActivity.DATE_SELECTED;
import static net.villim.villim.VillimKeys.CANCEL_VISIT_URL;
import static net.villim.villim.VillimKeys.KEY_ADDR_DIRECTION;
import static net.villim.villim.VillimKeys.KEY_ADDR_SUMMARY;
import static net.villim.villim.VillimKeys.KEY_CANCEL_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_HOUSE_ID;
import static net.villim.villim.VillimKeys.KEY_HOUSE_INFO;
import static net.villim.villim.VillimKeys.KEY_HOUSE_NAME;
import static net.villim.villim.VillimKeys.KEY_HOUSE_THUMBNAIL_URL;
import static net.villim.villim.VillimKeys.KEY_LATITUDE;
import static net.villim.villim.VillimKeys.KEY_LONGITUDE;
import static net.villim.villim.VillimKeys.KEY_MESSAGE;
import static net.villim.villim.VillimKeys.KEY_QUERY_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_RESERVATION_ACTIVE;
import static net.villim.villim.VillimKeys.KEY_VISIT_ID;
import static net.villim.villim.VillimKeys.KEY_VISIT_INFO;
import static net.villim.villim.VillimKeys.SERVER_HOST;
import static net.villim.villim.VillimKeys.SERVER_SCHEME;
import static net.villim.villim.VillimKeys.VISIT_INFO_URL;

public class VisitDetailActivity extends VillimActivity implements CancelVisitDialog.CancelVisitDialogListener{

    private Toolbar toolbar;

    private ImageView houseThumbnail;
    private TextView houseNameText;
    private TextView visitTimeText;
    private TextView errorMessage;
    private Button bottomButton;

    private Button locationButton;
    private Button cancelVisitButton;

    private AVLoadingIndicatorView loadingIndicator;

    private VillimSession session;

    private VillimVisit visit;

    private VillimHouse house;

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

        /* Top buttons */
        locationButton = (Button) findViewById(R.id.location_button);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisitDetailActivity.this, FullScreenMapActivity.class);
                intent.putExtra(KEY_LATITUDE, house.latitude);
                intent.putExtra(KEY_LONGITUDE, house.longitude);
                intent.putExtra(KEY_ADDR_DIRECTION, house.addrDirection);
                startActivity(intent);
            }
        });
        cancelVisitButton = (Button) findViewById(R.id.cancel_visit_button);
        cancelVisitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment cancelConfirmDialog = new CancelVisitDialog();
                cancelConfirmDialog.show(getFragmentManager(), "");
            }
        });

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

        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();


        URL url = new HttpUrl.Builder()
                .scheme(SERVER_SCHEME)
                .host(SERVER_HOST)
                .addPathSegments(VISIT_INFO_URL)
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

                        JSONObject houseInfo = jsonObject.getJSONObject(KEY_HOUSE_INFO);
                        house = VillimHouse.createHouseFromJSONObject(getApplicationContext(), houseInfo);
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

    private void sendCancelVisitRequest() {
        startLoadingAnimation();
        hideErrorMessage();

        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();

        URL url = new HttpUrl.Builder()
                .scheme(SERVER_SCHEME)
                .host(SERVER_HOST)
                .addPathSegments(CANCEL_VISIT_URL)
                .build().url();

        RequestBody requestBody = new FormBody.Builder()
                .add(KEY_VISIT_ID, Integer.toString(visit.visitId))
                .build();

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
                    if (jsonObject.getBoolean(KEY_CANCEL_SUCCESS)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onBackPressed();
                            }
                        });
                    } else {
                        showErrorMessage(jsonObject.getString(KEY_MESSAGE));
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
        /* Top buttons */
        locationButton.setEnabled(true);
        locationButton.setClickable(true);
        locationButton.setTextColor(getResources().getColorStateList(R.color.red_text_button));
        cancelVisitButton.setEnabled(true);
        cancelVisitButton.setClickable(true);
        cancelVisitButton.setTextColor(getResources().getColorStateList(R.color.red_text_button));

        /* Set up slide button */
        bottomButton.setClickable(true);
        bottomButton.setEnabled(true);
        bottomButton.setText(getString(R.string.book));
        bottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VisitDetailActivity.this, ReservationActivity.class);
                intent.putExtra(getString(R.string.key_house), house);
                intent.putExtra(DATE_SELECTED, false);
                startActivity(intent);
            }
        });

        /* House Name */
        houseNameText.setText(house.houseName);

        /* Visit time */
        visitTimeText.setText(visit.visitTime);

        /* House thumbnail */
        Glide.with(this)
                .load(house.houseThumbnailUrl)
                .into(houseThumbnail);
    }

    public void displayNoRoom() {
         /* Top buttons */
        locationButton.setEnabled(false);
        locationButton.setClickable(false);
        locationButton.setTextColor(getResources().getColorStateList(R.color.text_button_inactive));
        cancelVisitButton.setEnabled(false);
        cancelVisitButton.setClickable(false);
        cancelVisitButton.setTextColor(getResources().getColorStateList(R.color.text_button_inactive));

        /* Slide button */
        bottomButton.setClickable(false);
        bottomButton.setEnabled(false);

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

    /* Cancel Visit Dialog */
    public void onCancelConfirm(DialogFragment dialog) {
        dialog.dismiss();
        sendCancelVisitRequest();
    }

}
