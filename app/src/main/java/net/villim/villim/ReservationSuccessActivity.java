package net.villim.villim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReservationSuccessActivity extends AppCompatActivity {

    public static final String RESERVATION = "reservation";

    private Toolbar toolbar;
    private Button closeButton;

    private TextView instructions;
    private TextView reservationCode;
    private TextView reservationGuest;
    private TextView reservationDate;
    private TextView reservationStatus;

    private VillimSession session;
    private VillimReservation reservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_success);

        session = new VillimSession(this);

        /* Extract info */
        reservation = getIntent().getParcelableExtra(RESERVATION);

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

        /* Populate Textviews */
        instructions = (TextView) findViewById(R.id.instructions);
        reservationCode = (TextView) findViewById(R.id.reservation_code);
        reservationGuest = (TextView) findViewById(R.id.reservation_guest);
        reservationDate = (TextView) findViewById(R.id.reservation_date);
        reservationStatus = (TextView) findViewById(R.id.reservation_status);

        instructions.setText(getString(R.string.reservation_success_instruction_format));
        reservationCode.setText(reservation.reservationCode);
        reservationGuest.setText(session.getFullName());
        reservationDate.setText(reservation.reservationTime);
        reservationStatus.setText(VillimReservation.stringFromReservationStatus(this, reservation.reservationStatus));
    }
}
