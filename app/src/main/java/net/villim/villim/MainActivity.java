package net.villim.villim;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolBar;
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

        toolBar = (Toolbar) findViewById(R.id.toolBar);
        toolbarTextView = (TextView) findViewById(R.id.toolbar_title);
        toolBarTitle = getString(R.string.app_name);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setTitle(toolBarTitle);

        /* Bototm tab */
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabItems = getResources().getStringArray(R.array.tab_items);
        tabIcons = getResources().getIntArray(R.array.tab_icons);
        // Set default screen to 방 찾기.
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
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
//            tab.setCustomView(tabAdapter.getTabView(i));
            tab.setIcon(R.drawable.ic_whatshot_black_24dp);
        }

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
                    return DiscoverFragment.newInstance();
                case 2:
                    return MyRoomFragment.newInstance();
                case 3:
                    return DiscoverFragment.newInstance();
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

//

}
