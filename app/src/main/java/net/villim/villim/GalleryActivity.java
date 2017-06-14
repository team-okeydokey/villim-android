package net.villim.villim;

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

import static net.villim.villim.VillimKeys.KEY_HOUSE_PIC_URLS;

public class GalleryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button closeButton;
    private HorizontalScrollView scrollview;
    private LinearLayoutCompat linearLayout;

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

        /* ScrollView */
        scrollview = (HorizontalScrollView) findViewById(R.id.horizontal_scrollview);
        linearLayout  = (LinearLayoutCompat) findViewById(R.id.linear_layout);

        loadImages(getIntent().getStringArrayExtra(KEY_HOUSE_PIC_URLS));
    }

    private void loadImages(String[] urls) {
        for (int i = 0; i < urls.length; ++i) {
            ImageView imageView = new ImageView(this);
            linearLayout.addView(imageView);
            Glide.with(this)
                    .load(urls[i])
                    .into(imageView);
        }
    }

}
