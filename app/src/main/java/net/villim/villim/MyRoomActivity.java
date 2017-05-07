package net.villim.villim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MyRoomActivity extends AppCompatActivity {

    private TextView myroomTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_room);

        myroomTitle = (TextView) findViewById(R.id.myroom_title);

    }


}
