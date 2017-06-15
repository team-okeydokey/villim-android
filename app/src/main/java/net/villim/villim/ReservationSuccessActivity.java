package net.villim.villim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ReservationSuccessActivity extends AppCompatActivity {

    public static final String RESERVATION = "reservation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_success);
    }
}
