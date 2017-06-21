package net.villim.villim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static net.villim.villim.VillimKeys.KEY_CANCELLATION_POLICY;

public class CancellationPolicyActivity extends VillimActivity {

    private Toolbar toolbar;
    private Button closeButton;
    private TextView policyName;
    private TextView policyContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancellation_policy);

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

        /* 환불 정책 종류 */
        policyName = (TextView) findViewById(R.id.policy_name);
        policyName.setText(getIntent().getStringExtra(KEY_CANCELLATION_POLICY));

        /* 환불 정책 내용 */
        policyContent = (TextView) findViewById(R.id.policy_content);
//        policyContent.setText(getIntent().getStringExtra(KEY_REFUND_POLICY));

    }
}
