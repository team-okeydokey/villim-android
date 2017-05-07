package net.villim.villim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MyRoomActivity extends AppCompatActivity {

    private LinearLayoutCompat mainLayout;
    private TextView myroomTitle;
    private ImageView myroomThumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_room);

        mainLayout = (LinearLayoutCompat) findViewById(R.id.main_layout);
        myroomTitle = (TextView) findViewById(R.id.myroom_title);
        myroomThumbnail = (ImageView) findViewById(R.id.myroom_thumbnail);

        populateView();
    }

    private void populateView() {
        // Fetch and insert room thumbnail.
        Glide.with(this).load(R.drawable.prugio_thumbnail).into(myroomThumbnail);
    }


}
