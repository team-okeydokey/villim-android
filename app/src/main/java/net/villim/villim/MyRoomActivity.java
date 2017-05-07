package net.villim.villim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MyRoomActivity extends AppCompatActivity {

    private TextView myroomTitle;
    private ImageView myroomThumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_room);

        myroomTitle = (TextView) findViewById(R.id.myroom_title);
        myroomThumbnail = (ImageView) findViewById(R.id.myroom_thumbnail);

        loadThumbnail();
    }

    private void loadThumbnail() {
        Glide.with(this).load(R.drawable.prugio_thumbnail).into(myroomThumbnail);
    }


}
