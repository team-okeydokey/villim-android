package net.villim.villim;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static net.villim.villim.VillimKeys.KEY_HOUSE_PIC_URLS;

public class GalleryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button closeButton;
    private ViewPager viewPager;
    private GalleryPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Back button */
        closeButton = (Button) findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /* Image gallery viewpager */
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        List<Fragment> fragments = createImageFragments(getIntent().getStringArrayExtra(KEY_HOUSE_PIC_URLS));
        adapter = new GalleryPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }


    private class GalleryPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        public GalleryPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }
        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }
        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }

    private List<Fragment> createImageFragments(String[] urls) {
        List<Fragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < urls.length; ++i) {
            fragmentList.add(GalleryImageFragment.newInstance(urls[i]));
        }
        return fragmentList;
    }

}
