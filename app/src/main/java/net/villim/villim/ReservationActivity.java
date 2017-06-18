package net.villim.villim;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static net.villim.villim.CalendarActivity.END_DATE;
import static net.villim.villim.CalendarActivity.START_DATE;
import static net.villim.villim.MainActivity.DATE_SELECTED;
import static net.villim.villim.ReservationSuccessActivity.RESERVATION;
import static net.villim.villim.VillimKeys.KEY_EMAIL;
import static net.villim.villim.VillimKeys.KEY_END_DATE;
import static net.villim.villim.VillimKeys.KEY_LOGIN_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_MESSAGE;
import static net.villim.villim.VillimKeys.KEY_PASSWORD;
import static net.villim.villim.VillimKeys.KEY_RESERVATION_INFO;
import static net.villim.villim.VillimKeys.KEY_RESERVATION_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_ROOM_ID;
import static net.villim.villim.VillimKeys.KEY_START_DATE;
import static net.villim.villim.VillimKeys.KEY_USER_INFO;
import static net.villim.villim.VillimKeys.RESERVE_URL;

public class ReservationActivity extends VillimActivity {

    private static final int CALENDAR = 0;

    private Toolbar toolbar;

    private TextView overViewTitle;
    private TextView overViewHouseName;
    private TextView overViewHouseInfo;

    private ImageView hostProfilePic;
    private TextView hostName;

    private RelativeLayout startEndDates;
    private TextView startDateText;
    private TextView endDateText;

    private TextView numberOfNightsText;
    private TextView priceText;
    private TextView cancellationPolicyText;

    private Button reserveButton;
    private AVLoadingIndicatorView loadingIndicator;
    private TextView errorMessage;


    private VillimSession session;
    private VillimHouse house;
    private boolean dateSelected;
    private Date startDate;
    private Date endDate;
    private int stayDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        session = new VillimSession(this);

        /* Extract info */
        house = getIntent().getParcelableExtra(getString(R.string.key_house));
        dateSelected = getIntent().getBooleanExtra(DATE_SELECTED, false);
        if (dateSelected) {
            startDate = (Date) getIntent().getSerializableExtra(START_DATE);
            endDate = (Date) getIntent().getSerializableExtra(END_DATE);
            stayDuration = VillimUtil.daysBetween(startDate, endDate);
        }


        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow_dark));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Overview */
        overViewTitle = (TextView) findViewById(R.id.overview_title);
        overViewHouseName = (TextView) findViewById(R.id.overview_house_name);
        overViewHouseInfo = (TextView) findViewById(R.id.overview_house_info);

        /* Host info */
        hostProfilePic = (ImageView) findViewById(R.id.host_profile_pic);
        hostName = (TextView) findViewById(R.id.host_name);

        /* Duration */
        startEndDates = (RelativeLayout) findViewById(R.id.start_end_dates);
        startDateText = (TextView) findViewById(R.id.start_date_text);
        endDateText = (TextView) findViewById(R.id.end_date_text);

        /* Everything else */
        numberOfNightsText = (TextView) findViewById(R.id.number_of_nights);
        priceText = (TextView) findViewById(R.id.price);
        cancellationPolicyText = (TextView) findViewById(R.id.cancellation_policy);

        /* Network operations */
        reserveButton = (Button) findViewById(R.id.reserve_button);
        loadingIndicator = (AVLoadingIndicatorView) findViewById(R.id.loading_indicator);
        errorMessage = (TextView) findViewById(R.id.error_message);
        errorMessage.setVisibility(View.INVISIBLE);

        populateView();
    }

    private void populateView() {
        /* Overview */
        String titleString;
        if (dateSelected) {
            titleString = String.format(getString(R.string.overview_title_format_hasdate), house.addrSummary, stayDuration);
            updateStayInfo(startDate, endDate);
        } else {
            titleString = String.format(getString(R.string.overview_title_format_nodate), house.addrSummary);
        }
        overViewTitle.setText(titleString);
        overViewHouseName.setText(house.houseName);
        String overViewHouseInfoString = String.format(getString(R.string.overview_info_format), house.numGuest, house.numBedroom, house.numBed, house.numBathroom);
        overViewHouseInfo.setText(overViewHouseInfoString);

        /* Host info */
        hostName.setText(house.hostName);
        Glide.with(this).load(house.hostProfilePicUrl).into(hostProfilePic);

        /* Duration */
        startEndDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dateFilterIntent = new Intent(ReservationActivity.this, CalendarActivity.class);
                dateFilterIntent.putExtra(CalendarActivity.START_DATE, startDate);
                dateFilterIntent.putExtra(CalendarActivity.END_DATE, endDate);
                ReservationActivity.this.startActivityForResult(dateFilterIntent, CALENDAR);
            }
        });

        /* Everything else */
        cancellationPolicyText.setText(house.cancellationPolicy);

        /* Network operations */
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (session.getLoggedIn()) {
                    if (dateSelected){
                        sendReservationRequest();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.must_select_date, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Intent loginIntent = new Intent(ReservationActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }
            }
        });
    }

    private void updateStayInfo(Date start, Date end) {
        dateSelected = true;
        reserveButton.setEnabled(true);
        stayDuration = VillimUtil.daysBetween(startDate, endDate);

         /* Set date strings. */
        String startDateString = String.format(getString(R.string.date_filter_date_text_format), startDate.getMonth() + 1, startDate.getDate())
                + "\n" + VillimUtil.getWeekday(this, startDate.getDay());
        startDateText.setText(startDateString);

        String endDateString = String.format(getString(R.string.date_filter_date_text_format), endDate.getMonth() + 1, endDate.getDate())
                + "\n" + VillimUtil.getWeekday(this, endDate.getDay());
        endDateText.setText(endDateString);

        /* Set number of nights and price texts */
        String numNightsString = String.format(getString(R.string.num_nights_format), stayDuration);
        numberOfNightsText.setText(numNightsString);

        String priceString = String.format(getString(R.string.won_symbol_format), stayDuration * house.ratePerNight);
        priceText.setText(priceString);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /* Requests to reservation activity */
        if (requestCode == CALENDAR) {
            if (resultCode == Activity.RESULT_OK) {
                startDate = (Date) data.getSerializableExtra(CalendarActivity.START_DATE);
                endDate = (Date) data.getSerializableExtra(CalendarActivity.END_DATE);
                updateStayInfo(startDate, endDate);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void sendReservationRequest() {
        startLoadingAnimation();
        hideErrorMessage();

        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();

        RequestBody requestBody = new FormBody.Builder()
                .add(KEY_ROOM_ID, Integer.toString(house.houseId))
                .add(KEY_START_DATE, VillimUtil.dateStringFromDate(this, startDate))
                .add(KEY_END_DATE, VillimUtil.dateStringFromDate(this, endDate))
                .build();

        Request request = new Request.Builder()
                .url(RESERVE_URL)
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
                    if (jsonObject.getBoolean(KEY_RESERVATION_SUCCESS)) {
                        VillimReservation reservation = VillimReservation.createReservationFromJSONObject((JSONObject) jsonObject.get(KEY_RESERVATION_INFO));
                        Intent intent = new Intent(ReservationActivity.this, ReservationSuccessActivity.class);
                        intent.putExtra(RESERVATION, reservation);
                        stopLoadingAnimation();
                        hideErrorMessage();
                        startActivity(intent);
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
