package net.villim.villim;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_FILTER = 0;
    private static final int DATE_FILTER = 1;

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
//    private TextView toolbarTextView;
    private ImageView toolbarLogo;
    private String[] tabItems;
    private int[] tabIcons;
    private CharSequence toolBarTitle;
    private TabLayout tabLayout;
    private UnSwipeableViewpager viewPager;
    private Button searchButton;
    private RelativeLayout searchFilters;
    private TextView searchFilterLocation;
    private TextView searchFilterDate;

    private boolean appBarOpen;

    private Date startDate;
    private Date endDate;

    private int toolBarCollpasedColor;
    private int toolBarOpenColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appBarOpen = false;

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbarTextView = (TextView) findViewById(R.id.toolbar_title);
        toolbarLogo = (ImageView) findViewById(R.id.toolbar_logo);
        toolBarTitle = getString(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setTitle(toolBarTitle);

        toolBarOpenColor = getResources().getColor(R.color.search_filter_open);
        toolBarCollpasedColor = getResources().getColor(android.R.color.white);

        /* Search Filters */
        searchFilters = (RelativeLayout) findViewById(R.id.search_filters);
        searchFilterLocation = (TextView) findViewById(R.id.search_filter_location);
        searchFilterDate = (TextView) findViewById(R.id.search_filter_date);
        /* Set search filter icons */
        int markerSize = getResources().getDimensionPixelSize(R.dimen.marker_icon_size);
        int calendarWidth = getResources().getDimensionPixelSize(R.dimen.calendar_icon_width);;
        int calendarHeight = getResources().getDimensionPixelSize(R.dimen.calendar_icon_height);;
        Drawable markerIcon = getResources().getDrawable(R.drawable.icon_marker);
        Drawable calendarIcon = getResources().getDrawable(R.drawable.icon_calendar);
        markerIcon.setBounds(0, 0, markerSize, markerSize);
        calendarIcon.setBounds(0, 0, calendarWidth,calendarHeight);
        searchFilterLocation.setCompoundDrawables(markerIcon, null, null, null);
        searchFilterDate.setCompoundDrawables(calendarIcon, null, null, null);
        /* Launch filter activities */
        searchFilterLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent locationFilterIntent = new Intent(MainActivity.this, LocationFilterActivity.class);
                MainActivity.this.startActivityForResult(locationFilterIntent, LOCATION_FILTER);

            }
        });
        searchFilterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dateFilterIntent = new Intent(MainActivity.this, DateFilterActivity.class);
                dateFilterIntent.putExtra(DateFilterActivity.START_DATE, startDate);
                dateFilterIntent.putExtra(DateFilterActivity.END_DATE, endDate);
                MainActivity.this.startActivityForResult(dateFilterIntent, DATE_FILTER);
            }
        });

        /* Scroll behaviors */
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) { // Completely open.
//                    toolbarTextView.setTextColor(getResources().getColor(android.R.color.white));
                    toolbarLogo.setImageResource(R.drawable.logo_horizontal_white);
                    toolbar.setBackgroundColor(getResources().getColor(R.color.search_filter_open));
                    searchButton.setBackground(getResources().getDrawable(R.drawable.btn_up_arrow));
                    appBarOpen = true;
                } else if (verticalOffset == -appBarLayout.getTotalScrollRange()) { // Completely collapsed.
//                    toolbarTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                    toolbarLogo.setImageResource(R.drawable.logo_horizontal_red);
                    toolbar.setBackgroundColor(getResources().getColor(android.R.color.white));
                    searchButton.setBackground(getResources().getDrawable(R.drawable.btn_search));
                    appBarOpen = false;
                } else {
                    int toolBarColor = getToolbarColorFromOffset(
                            verticalOffset, -appBarLayout.getTotalScrollRange(), 0,
                            toolBarCollpasedColor, toolBarOpenColor);
//                    toolbarTextView.setTextColor(getResources().getColor(android.R.color.white));
                    int toolBarLogoColor = getToolbarColorFromOffset(
                            verticalOffset, -appBarLayout.getTotalScrollRange(), 0,
                            toolBarOpenColor, toolBarCollpasedColor);
                    toolbarLogo.setImageResource(R.drawable.logo_horizontal_white);
                    toolbarLogo.setColorFilter(toolBarLogoColor);
                    toolbar.setBackgroundColor(toolBarColor);
                    searchButton.setBackground(getResources().getDrawable(R.drawable.btn_up_arrow));
                }
            }
        });

        appBarLayout.setExpanded(false);

        /* Bototm tab */
        viewPager = (UnSwipeableViewpager) findViewById(R.id.view_pager);
        tabItems = getResources().getStringArray(R.array.tab_items);
        tabIcons = getResources().getIntArray(R.array.tab_icons);
        // Set default screen to 방 찾기.
        final TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setPagingEnabled(false);
        viewPager.setAdapter(tabAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                toolBarTitle = tabItems[position];
                //setTitle(toolBarTitle);
                // Fold search.
                appBarLayout.setExpanded(false);
                if (position != 0) { // Hide search button if not in discovery fragment.
                    searchButton.setVisibility(View.INVISIBLE);
                } else {
                    searchButton.setVisibility(View.VISIBLE);
                }
            }

        });
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(R.layout.tab);
            tab.setIcon(getTabIcon(i));
        }

        /* Search Button */
        searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (appBarOpen) {
//                    toolbar.setBackgroundColor(getResources().getColor(android.R.color.white));
//                } else {
//                    toolbar.setBackgroundColor(getResources().getColor(R.color.search_filter_open));
//                }
                appBarOpen = !appBarOpen;
                appBarLayout.setExpanded(appBarOpen);
            }
        });

//        Glide.with(this)
//                .load(R.drawable.prugio_thumbnail)
//                .into((ImageView) findViewById(R.id.toolbar_image));

    }

    private class TabAdapter extends FragmentPagerAdapter {

        public TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return tabItems.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return DiscoverFragment.newInstance();
                case 1:
                    return MyRoomFragment.newInstance();
                case 2:
                    return WishListFragment.newInstance();
                case 3:
                    return ProfileFragment.newInstance();
                case 4:
                    return WishListFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabItems[position];
        }

//        public View getTabView(int position) {
//            View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab, null);
//            TextView tv = (TextView) v.findViewById(R.id.text1);
//            tv.setText(tabItems[position]);
////            ImageView img = (ImageView) v.findViewById(R.id.icon);
////            img.setImageResource(tabIcons[position]);
//            tab.setIcon
//            return v;
//        }
    }

    @Override
    public void setTitle(CharSequence title) {
        toolBarTitle = title;
//        toolbarTextView.setText(toolBarTitle);
    }

    private Drawable getTabIcon(int i) {
        switch (i) {
            case 0: return getResources().getDrawable(R.drawable.icon_find_place_nor);
            case 1: return getResources().getDrawable(R.drawable.icon_lock_nor);
            case 2: return getResources().getDrawable(R.drawable.icon_correct_nor);
            case 3: return getResources().getDrawable(R.drawable.icon_profile_nor);
            default: return getResources().getDrawable(R.drawable.icon_profile_nor);

        }
    }

    private void highlightTab(int position) {
//        tabLayout.getTabAt(position).getIcon().setColorFilter("#abffab");
//        ((TextView)tabLayout.getTabAt(position).getCustomView().findViewById(R.id.text1)).setTextColor("#abffab");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LOCATION_FILTER) {
            if(resultCode == Activity.RESULT_OK){
                VillimLocation location = data.getParcelableExtra(LocationFilterActivity.LOCATION);
                System.out.println(location.addrSummary);
                searchFilterLocation.setText(location.addrSummary);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if (requestCode == DATE_FILTER) {
            if(resultCode == Activity.RESULT_OK){
                startDate = (Date) data.getSerializableExtra(DateFilterActivity.START_DATE);
                endDate = (Date) data.getSerializableExtra(DateFilterActivity.END_DATE);

                String dateFilterText = String.format(getString(R.string.search_filter_date_format),
                        startDate.getMonth(), startDate.getDate(),
                        endDate.getMonth(), endDate.getDate());
                searchFilterDate.setText(dateFilterText);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private int getToolbarColorFromOffset(int curVal, int minVal, int maxVal, int initialColor, int finalColor) {
        float maxDiff = maxVal - minVal;
        float curDiff = curVal - minVal;

        float progress = curDiff / maxDiff;

        int r = (int)((Color.red(finalColor) - Color.red(initialColor)) * progress) + Color.red(initialColor);
        int g = (int)((Color.green(finalColor) - Color.green(initialColor)) * progress) + Color.green(initialColor);
        int b = (int)((Color.blue(finalColor) - Color.blue(initialColor)) * progress) + Color.blue(initialColor);

        return Color.rgb(r, g, b);
    }

    private int getToolbarLogoColorFromOffset(int curVal, int minVal, int maxVal, int initialColor, int finalColor) {
        float maxDiff = maxVal - minVal;
        float curDiff = curVal - minVal;

        float progress = curDiff / maxDiff;

        int r = (int)((Color.red(finalColor) - Color.red(initialColor)) * progress) + Color.red(initialColor);
        int g = (int)((Color.green(finalColor) - Color.green(initialColor)) * progress) + Color.green(initialColor);
        int b = (int)((Color.blue(finalColor) - Color.blue(initialColor)) * progress) + Color.blue(initialColor);

        return Color.rgb(r, g, b);
    }

}
