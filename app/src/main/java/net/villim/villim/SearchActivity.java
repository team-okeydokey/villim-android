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

    private Toolbar toolbar;
    private RelativeLayout searchContainer;
    private ListViewCompat popularLocationsListView;
    private VillimLocation[] popularLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow_dark));
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


}
