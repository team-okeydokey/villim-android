package net.villim.villim;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static net.villim.villim.VillimKeys.KEY_VISIT_INFO;

public class VisitRequestSuccessActivity extends VillimActivity {

    private Toolbar toolbar;
    private Button closeButton;

    private TextView instructions;
    private TextView reservationCode;
    private TextView reservationGuest;
    private TextView reservationDate;
    private TextView reservationStatus;

    private VillimSession session;
    private VillimVisit visit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_request_success);

        session = new VillimSession(this);

        /* Extract info */
        visit = getIntent().getParcelableExtra(KEY_VISIT_INFO);

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
        reservationCode.setText(Integer.toString(visit.visitId));
        reservationGuest.setText(session.getFullName());
        String visitTimeText;
        if (visit.visitTime.equals("null")) {
            visitTimeText = getString(R.string.no_date);
        } else {
            visitTimeText = visit.visitTime;
        }
        reservationDate.setText(visitTimeText);
        reservationStatus.setText(VillimVisit.stringFromVisitStatus(this, visit.visitStatus));
    }
}
