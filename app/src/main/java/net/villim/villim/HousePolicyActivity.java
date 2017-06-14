package net.villim.villim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static net.villim.villim.VillimKeys.KEY_HOUSE_POLICY;

public class HousePolicyActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button closeButton;
    private TextView housePolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_policy);

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

        /* 숙소 이용규칙 텍스트 */
        housePolicy = (TextView) findViewById(R.id.house_policy);
        housePolicy.setText(getIntent().getStringExtra(KEY_HOUSE_POLICY));
    }
}
