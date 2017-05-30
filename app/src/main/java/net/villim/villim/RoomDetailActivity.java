package net.villim.villim;

import android.graphics.PorterDuff;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class RoomDetailActivity extends AppCompatActivity {

    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView toolbarImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        toolbarImage = (ImageView) findViewById(R.id.toolbar_image);

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow_light));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Change back button color on collapse */
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if ((collapsingToolbarLayout.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(collapsingToolbarLayout))) {
                    toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.arrow_dark), PorterDuff.Mode.SRC_ATOP);

                } else {
                    toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.arrow_light), PorterDuff.Mode.SRC_ATOP);
                }
            }
        });

        TextView tv = (TextView) findViewById(R.id.text_view);
//        tv.setNestedScrollingEnabled(false);

        populateView();

    }

    // Make this async.
    private void populateView() {
        Glide.with(this)
                .load(R.drawable.prugio_thumbnail)
                .into(toolbarImage);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
