package net.villim.villim;

import android.content.Intent;
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
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTextView;
    private String[] tabItems;
    private int[] tabIcons;
    private CharSequence toolBarTitle;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTextView = (TextView) findViewById(R.id.toolbar_title);
        toolBarTitle = getString(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setTitle(toolBarTitle);

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
                /* Highlight the selected tab */

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
                Intent myIntent = new Intent(MainActivity.this, SearchActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

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
