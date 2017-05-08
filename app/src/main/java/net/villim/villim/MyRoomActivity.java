package net.villim.villim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MyRoomActivity extends AppCompatActivity {

    private TextView myroomTitle;
    private TextView myroomRoomName;

    private TextView myroomNumGuest;
    private TextView myroomRoomType;
    private TextView myroomNumBed;
    private TextView myroomNumBath;

    private TextView myroomRoomDuration;
    private ImageView myroomThumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_room);

        myroomTitle = (TextView) findViewById(R.id.myroom_title);

        // Room name.
        myroomRoomName = (TextView) findViewById(R.id.myroom_roomname);

        // Room info.
        myroomNumGuest = (TextView) findViewById(R.id.myroom_numguest);
        myroomRoomType = (TextView) findViewById(R.id.myroom_roomtype);
        myroomNumBed = (TextView) findViewById(R.id.myroom_numbed);
        myroomNumBath = (TextView) findViewById(R.id.myroom_numbath);

        // Room duration.
        myroomRoomDuration = (TextView) findViewById(R.id.myroom_roomduration);

        myroomThumbnail = (ImageView) findViewById(R.id.myroom_thumbnail);

        populateView();
    }

    // Make this async.
    private void populateView() {
        // Network operation to fetch.

        // Populate room name.
        myroomRoomName.setText("Prugio Studio");

        // Populate room information.
        myroomNumGuest.setText("Guest 1");
        myroomRoomType.setText("Studio");
        myroomNumBed.setText("Bed 1");
        myroomNumBath.setText("Bath 1");

        // Populate room duration.
        myroomRoomDuration.setText("Duration of stay: 2017/04/16 ~ 06/19");

        // Fetch and insert room thumbnail.
        Glide.with(this)
                .load(R.drawable.prugio_thumbnail)
                .into(myroomThumbnail);
    }


}
