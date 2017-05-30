package net.villim.villim;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class SearchActivity extends AppCompatActivity {

    private final static String BUTTON_CODE = "button_code";
    private final static String HAS_DATE = "has_date";
    private final static String YEAR = "year";
    private final static String MONTH = "month";
    private final static String DAY = "day";
    private final static int BUTTON_START = 0;
    private final static int BUTTON_END = 1;

    private Toolbar toolbar;
    private RelativeLayout searchContainer;
    private ListViewCompat popularLocationsListView;
    private VillimLocation[] popularLocations;
    private TextView selectDateStart;
    private TextView selectDateEnd;
    private boolean startDateSet = false;
    private boolean endDateSet = false;
    private Date startDate;
    private Date endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /* Parse current date */
        final Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH);
        int curDay = c.get(Calendar.DAY_OF_MONTH);
        startDate = new Date(curYear, curMonth, curDay);
        endDate = new Date(curYear, curMonth, curDay);

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Relative layout that contains the search area */
        searchContainer = (RelativeLayout) findViewById(R.id.search_container);

        /* Popular locations list */
        popularLocationsListView = (ListViewCompat) findViewById(R.id.search_popular_locations_listview);
        TextView textView = new TextView(getApplicationContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15.0f);
        textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.search_suggestion_title));
        textView.setText(getString(R.string.search_popular_locations));
        popularLocationsListView.addHeaderView(textView);
        popularLocations = new VillimLocation[] { new VillimLocation("강남구", "서울특별시 강남구"),
                new VillimLocation("강남역", "서초구 강남"),
                new VillimLocation("대한민국", "서울특별시 강남대로 서초구"),
                new VillimLocation("강남 고속버스 터미널", "서초구 강남 고속버스 터미널")};
        popularLocationsListView.setAdapter(new SearchSuggestionListViewAdapter(getApplicationContext(), popularLocations));

        /* Select date buttons */
        selectDateStart = (TextView) findViewById(R.id.select_date_start);
        selectDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt(BUTTON_CODE, BUTTON_START);
                args.putBoolean(HAS_DATE, startDateSet);
                args.putInt(YEAR, startDate.getYear());
                args.putInt(MONTH, startDate.getMonth());
                args.putInt(DAY, startDate.getDate());
                DialogFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setArguments(args);
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        selectDateEnd = (TextView) findViewById(R.id.select_date_end);
        selectDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt(BUTTON_CODE, BUTTON_END);
                args.putBoolean(HAS_DATE, endDateSet);
                args.putInt(YEAR, endDate.getYear());
                args.putInt(MONTH, endDate.getMonth());
                args.putInt(DAY, endDate.getDate());
                DialogFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setArguments(args);
                datePickerFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class SearchSuggestionListViewAdapter extends ArrayAdapter<VillimLocation> {
        private VillimLocation[] locations;

        public SearchSuggestionListViewAdapter(Context context, VillimLocation[] popularLocations) {
            super(context, R.layout.search_suggestion_list_item, popularLocations);
            this.locations = popularLocations;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_suggestion_list_item, parent, false);
            }

            VillimLocation location = locations[position];

            /* Location Name */
            TextView locationName = (TextView) convertView.findViewById(R.id.location_name);
            locationName.setText(location.name);

            /* Location detail */
            TextView locationDetail = (TextView) convertView.findViewById(R.id.location_detail);
            locationDetail.setText(location.detail);
            return convertView;
        }
    }

    private static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private int buttonCode;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            Bundle bundle = getArguments();

            int year = bundle.getInt(YEAR);
            int month = bundle.getInt(MONTH);
            int day = bundle.getInt(DAY);

            buttonCode = bundle.getInt(BUTTON_CODE);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            ((SearchActivity)getActivity()).setDateText(buttonCode, year, month, day);
        }
    }

    private void setDateText(int buttonCode, int year, int month, int day) {
        Date newDate = new Date(year, month, day);
        boolean dateValid;

        /* Verify if valid date. */
        if (endDateSet && buttonCode == BUTTON_START) {
            dateValid = !newDate.after(endDate);
        } else if (startDateSet && buttonCode == BUTTON_END){
            dateValid = !newDate.before(startDate);
        } else {
            dateValid = true;
        }

        /* Set selected. */
        if (dateValid) {
            String dateTextTemplate = getString(R.string.search_date_format);
            String dateText = String.format(dateTextTemplate, year, month + 1, day);
            if (buttonCode == BUTTON_START) {
                startDateSet = true;
                selectDateStart.setText(dateText);
                startDate.setYear(year);
                startDate.setMonth(month);
                startDate.setDate(day);
            } else {
                endDateSet = true;
                selectDateEnd.setText(dateText);
                endDate.setYear(year);
                endDate.setMonth(month);
                endDate.setDate(day);
            }
        } else {
            Snackbar snackbar = Snackbar
                    .make(searchContainer, getString(R.string.invalid_date_selected), Snackbar.LENGTH_SHORT);
            View snackbarView = snackbar.getView();
            /* Adjust snackbar height */
            ViewGroup.LayoutParams params = snackbarView.getLayoutParams();
            params.height = findViewById(R.id.date_select_bar).getHeight();
            snackbarView.setLayoutParams(params);
            /* Center text */
            TextView snackbarTextView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            snackbarTextView.setGravity(Gravity.CENTER);
            snackbar.show();
        }
    }

}
