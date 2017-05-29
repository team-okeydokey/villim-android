package net.villim.villim;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class SearchActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListViewCompat popularLocationsListView;
    private VillimLocation[] popularLocations;
    private TextView selectDateStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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

        /* Select data button */
        selectDateStart = (TextView) findViewById(R.id.select_date_end);
        selectDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragment = new DatePickerFragment();
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

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user.
        }
    }

}
