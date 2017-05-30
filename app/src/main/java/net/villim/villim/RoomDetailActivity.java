package net.villim.villim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class RoomDetailActivity extends AppCompatActivity {

    private ImageView toolbarImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        toolbarImage = (ImageView) findViewById(R.id.toolbar_image);

        Glide.with(this)
                .load(R.drawable.prugio_thumbnail)
                .into(toolbarImage);



    }
}
