package net.villim.villim;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;


public class MainActivity extends VillimActivity {

    private static final int LOCATION_FILTER = 0;
    private static final int DATE_FILTER = 1;

    public final static String DATE_SELECTED = "date_selected";

    /* Tab fragment indices */
    private static final int DISCOVERY_FRAGMENT = 0;
    private static final int MY_KEY_FRAGMENT = 1;
    private static final int VISIT_FRAGMENT = 2;
    private static final int PROFILE_FRAGMENT = 3;

    /* We can store the fragment references becasue we are using a FragmentPagerAdapter.
       If FragmentStatePagerAdapter is used, weak references must be used to refer to fragments. */
    private ProfileFragment profileFragment;
    private VisitFragment visitFragment;

    private CoordinatorLayout container;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    //    private TextView toolbarTextView;
    private ImageView toolbarLogo;
    private String[] tabItems;
    private int[] tabIcons;
    private CharSequence toolBarTitle;
    private static TabLayout tabLayout;
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

    /* These variables control the visibility of clear button on filters */
    private boolean locationSelected;
    private boolean dateSelected;
    private final int DRAWABLE_LEFT = 0;
    private final int DRAWABLE_RIGHT = 2;
    private final int HIDE = 0;
    private final int SHOW = 255;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appBarOpen = false;
        locationSelected = false;
        locationSelected = false;

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
        setUpSerachFilters();

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
                    hideToolbar();
                } else {
                    searchButton.setVisibility(View.VISIBLE);
                    showToolbar();
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
                appBarOpen = !appBarOpen;
                appBarLayout.setExpanded(appBarOpen);
            }
        });


    }

    private void setUpSerachFilters() {
        /* Set search filter icons */
        int markerSize = getResources().getDimensionPixelSize(R.dimen.marker_icon_size);
        int calendarWidth = getResources().getDimensionPixelSize(R.dimen.calendar_icon_width);
        int calendarHeight = getResources().getDimensionPixelSize(R.dimen.calendar_icon_height);
        int clearSize = getResources().getDimensionPixelSize(R.dimen.clear_icon_size);
        Drawable markerIcon = getResources().getDrawable(R.drawable.icon_marker);
        Drawable calendarIcon = getResources().getDrawable(R.drawable.icon_calendar);
        Drawable clearIcon = getResources().getDrawable(R.drawable.btn_delete_white);
        Drawable dateClearIcon = getResources().getDrawable(R.drawable.btn_delete_white);
        markerIcon.setBounds(0, 0, markerSize, markerSize);
        calendarIcon.setBounds(0, 0, calendarWidth, calendarHeight);
        clearIcon.setBounds(0, 0, clearSize, clearSize);
        dateClearIcon.setBounds(0, 0, clearSize, clearSize);
        searchFilterLocation.setCompoundDrawables(markerIcon, null, clearIcon, null);
        searchFilterDate.setCompoundDrawables(calendarIcon, null, dateClearIcon, null);
        /* Launch filter activities */
        searchFilterLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true;
                    case MotionEvent.ACTION_UP:
                        /* When right drawable(clear button) is selected. */
                        if (locationSelected && event.getRawX() >= (searchFilterDate.getRight() - searchFilterDate.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            searchFilterLocation.setText(getString(R.string.all_locations));
                            locationSelected = false;
                            searchFilterLocation.getCompoundDrawables()[DRAWABLE_RIGHT].mutate().setAlpha(HIDE);
                        } else {
                            Intent locationFilterIntent = new Intent(MainActivity.this, LocationFilterActivity.class);
                            MainActivity.this.startActivityForResult(locationFilterIntent, LOCATION_FILTER);
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
        searchFilterDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true;
                    case MotionEvent.ACTION_UP:
                        /* When right drawable(clear button) is selected. */
                        if (dateSelected && event.getRawX() >= (searchFilterDate.getRight() - searchFilterDate.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            searchFilterDate.setText(getString(R.string.date_filter_title));
                            startDate = null;
                            endDate = null;
                            dateSelected = false;
                            searchFilterDate.getCompoundDrawables()[DRAWABLE_RIGHT].mutate().setAlpha(HIDE);
                        } else {
                            Intent dateFilterIntent = new Intent(MainActivity.this, CalendarActivity.class);
                            dateFilterIntent.putExtra(CalendarActivity.START_DATE, startDate);
                            dateFilterIntent.putExtra(CalendarActivity.END_DATE, endDate);
                            MainActivity.this.startActivityForResult(dateFilterIntent, DATE_FILTER);
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
        /* Hide clear buttons initially. Setting one will have the same effect on both,
           because both drawables are referencing the same clear icon object.
           We have to call mutate() on a drawable before changing its bitmap. */
        searchFilterLocation.getCompoundDrawables()[DRAWABLE_RIGHT].setAlpha(HIDE);
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
                case DISCOVERY_FRAGMENT:
                    return DiscoverFragment.newInstance();
                case MY_KEY_FRAGMENT:
                    return MyKeyFragment.newInstance();
                case VISIT_FRAGMENT:
                    return VisitFragment.newInstance();
                case PROFILE_FRAGMENT:
                    return ProfileFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
            // save the appropriate reference depending on position
            switch (position) {
                case PROFILE_FRAGMENT:
                    profileFragment = (ProfileFragment) createdFragment;
                    break;
                case VISIT_FRAGMENT:
                    visitFragment = (VisitFragment) createdFragment;
                    break;
                default:
                    break;
            }
            return createdFragment;
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
            case 0:
                return getResources().getDrawable(R.drawable.icon_find_place_nor);
            case 1:
                return getResources().getDrawable(R.drawable.icon_lock_nor);
            case 2:
                return getResources().getDrawable(R.drawable.icon_correct_nor);
            case 3:
                return getResources().getDrawable(R.drawable.icon_profile_nor);
            default:
                return getResources().getDrawable(R.drawable.icon_profile_nor);

        }
    }

    private void highlightTab(int position) {
//        tabLayout.getTabAt(position).getIcon().setColorFilter("#abffab");
//        ((TextView)tabLayout.getTabAt(position).getCustomView().findViewById(R.id.text1)).setTextColor("#abffab");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /* Route requests to appropriate fragments */
        if (requestCode == ProfileFragment.LOGIN && null != profileFragment) {
            profileFragment.onActivityResult(requestCode, resultCode, data);
            return;
        } else if (requestCode == VisitFragment.VISIT_DETAIL && null != visitFragment) {
            visitFragment.onActivityResult(requestCode, resultCode, data);
            return;
        }

        /* Requests to mainactivity */
        if (requestCode == LOCATION_FILTER) {
            if (resultCode == Activity.RESULT_OK) {
                VillimLocation location = data.getParcelableExtra(LocationFilterActivity.LOCATION);
                searchFilterLocation.setText(location.addrSummary);
                /* Show clear button */
                locationSelected = true;
                searchFilterLocation.getCompoundDrawables()[DRAWABLE_RIGHT].mutate().setAlpha(SHOW);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if (requestCode == DATE_FILTER) {
            if (resultCode == Activity.RESULT_OK) {
                startDate = (Date) data.getSerializableExtra(CalendarActivity.START_DATE);
                endDate = (Date) data.getSerializableExtra(CalendarActivity.END_DATE);

                String dateFilterText = String.format(getString(R.string.search_filter_date_format),
                        startDate.getMonth() + 1, startDate.getDate(),
                        endDate.getMonth() + 1, endDate.getDate());
                searchFilterDate.setText(dateFilterText);
                /* Show clear button */
                dateSelected = true;
                searchFilterDate.getCompoundDrawables()[DRAWABLE_RIGHT].mutate().setAlpha(SHOW);
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

        int r = (int) ((Color.red(finalColor) - Color.red(initialColor)) * progress) + Color.red(initialColor);
        int g = (int) ((Color.green(finalColor) - Color.green(initialColor)) * progress) + Color.green(initialColor);
        int b = (int) ((Color.blue(finalColor) - Color.blue(initialColor)) * progress) + Color.blue(initialColor);

        return Color.rgb(r, g, b);
    }

    private int getToolbarLogoColorFromOffset(int curVal, int minVal, int maxVal, int initialColor, int finalColor) {
        float maxDiff = maxVal - minVal;
        float curDiff = curVal - minVal;

        float progress = curDiff / maxDiff;

        int r = (int) ((Color.red(finalColor) - Color.red(initialColor)) * progress) + Color.red(initialColor);
        int g = (int) ((Color.green(finalColor) - Color.green(initialColor)) * progress) + Color.green(initialColor);
        int b = (int) ((Color.blue(finalColor) - Color.blue(initialColor)) * progress) + Color.blue(initialColor);

        return Color.rgb(r, g, b);
    }

    public void hideToolbar() {
        /* Detach dependant views */
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) viewPager.getLayoutParams();
        params.setBehavior(null);
        viewPager.requestLayout();

        /* Control visibility */
        appBarLayout.setExpanded(false, false);
        appBarLayout.setVisibility(View.GONE);
    }

    public void showToolbar() {
        /* Reattach dependant views */
        CoordinatorLayout.LayoutParams params =
                (CoordinatorLayout.LayoutParams) viewPager.getLayoutParams();
        params.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        viewPager.requestLayout();


        /* Control visibility */
        appBarLayout.setVisibility(View.VISIBLE);
    }

    public static void selectTab(int index) {
        TabLayout.Tab tab = tabLayout.getTabAt(index);
        tab.select();
    }

    public boolean getDateSelected() {
        return dateSelected;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return  endDate;
    }


}
