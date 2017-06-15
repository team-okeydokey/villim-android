package net.villim.villim;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Date;

import static net.villim.villim.CalendarActivity.END_DATE;
import static net.villim.villim.CalendarActivity.START_DATE;
import static net.villim.villim.MainActivity.DATE_SELECTED;

public class ReservationActivity extends AppCompatActivity {

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

    private VillimHouse house;
    private boolean dateSelected;
    private Date startDate;
    private Date endDate;
    private int stayDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

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

    }

    private void updateStayInfo(Date start, Date end) {
        dateSelected = true;
        stayDuration = VillimUtil.daysBetween(startDate, endDate);

         /* Set date strings. */
        String startDateString = String.format(getString(R.string.date_filter_date_text_format), startDate.getMonth(), startDate.getDate())
                + "\n" + VillimUtil.getWeekday(this, startDate.getDay());
        startDateText.setText(startDateString);

        String endDateString = String.format(getString(R.string.date_filter_date_text_format), endDate.getMonth(), endDate.getDate())
                + "\n" + VillimUtil.getWeekday(this, endDate.getDay());
        endDateText.setText(endDateString);

        /* Set number of nights and price texts */
        String numNightsString = String.format(getString(R.string.num_nights_format), stayDuration);
        numberOfNightsText.setText(numNightsString);

        String priceString = String.format(getString(R.string.won_symbol_format), stayDuration*house.ratePerNight);
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
}
