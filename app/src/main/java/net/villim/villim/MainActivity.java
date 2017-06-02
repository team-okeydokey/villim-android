package net.villim.villim;

import android.content.Intent;
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


public class MainActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private TextView toolbarTextView;
    private ImageView toolbarLogo;
    private String[] tabItems;
    private int[] tabIcons;
    private CharSequence toolBarTitle;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button searchButton;
    private RelativeLayout searchFilters;
    private TextView searchFilterLocation;
    private TextView searchFilterDate;

    private boolean appBarOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appBarOpen = false;

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTextView = (TextView) findViewById(R.id.toolbar_title);
        toolbarLogo = (ImageView) findViewById(R.id.toolbar_logo);
        toolBarTitle = getString(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setTitle(toolBarTitle);

        /* Search Filters */
        searchFilters = (RelativeLayout) findViewById(R.id.search_filters);
        searchFilterLocation = (TextView) findViewById(R.id.search_filter_location);
        searchFilterDate = (TextView) findViewById(R.id.search_filter_date);

        searchFilterLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, LocationFilterActivity.class);
                MainActivity.this.startActivity(myIntent);

            }
        });

        /* Scroll behaviors */
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) { // Completely open.
                    toolbarTextView.setTextColor(getResources().getColor(android.R.color.white));
                    toolbarLogo.setColorFilter(getResources().getColor(android.R.color.white));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.search_filter_open));
                    appBarOpen = true;
                } else if (verticalOffset == -appBarLayout.getTotalScrollRange()) { // Completely collapsed.
                    toolbarTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
                    toolbarLogo.setColorFilter(getResources().getColor(R.color.colorPrimary));
                    toolbar.setBackgroundColor(getResources().getColor(android.R.color.white));
                    appBarOpen = false;
                } else {
                    toolbarTextView.setTextColor(getResources().getColor(android.R.color.white));
                    toolbarLogo.setColorFilter(getResources().getColor(android.R.color.white));
                    toolbar.setBackgroundColor(getResources().getColor(R.color.search_filter_open));
                }
            }
        });

        appBarLayout.setExpanded(false);

        /* Bototm tab */
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabItems = getResources().getStringArray(R.array.tab_items);
        tabIcons = getResources().getIntArray(R.array.tab_icons);
        // Set default screen to 방 찾기.
        final TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                toolBarTitle = tabItems[position];
                //setTitle(toolBarTitle);
            }

        });
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(R.layout.tab);
            tab.setIcon(R.drawable.ic_whatshot_black_24dp);
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
                    return WishListFragment.newInstance();
                case 2:
                    return MyRoomFragment.newInstance();
                case 3:
                    return MessageFragment.newInstance();
                case 4:
                    return ProfileFragment.newInstance();
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
        toolbarTextView.setText(toolBarTitle);
    }


    private void highlightTab(int position) {
//        tabLayout.getTabAt(position).getIcon().setColorFilter("#abffab");
//        ((TextView)tabLayout.getTabAt(position).getCustomView().findViewById(R.id.text1)).setTextColor("#abffab");
    }
//

}
