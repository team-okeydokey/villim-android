package net.villim.villim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HouseDescriptionActivity extends AppCompatActivity {

    public static final String KEY_BASIC_DESCRIPTION = "basic_description";

    private Toolbar toolbar;
    private Button closeButton;
    private TextView basicDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_description);

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

        /* 설명 텍스트 */
        basicDescription = (TextView) findViewById(R.id.basic_description);
        basicDescription.setText(getIntent().getStringExtra(KEY_BASIC_DESCRIPTION));
    }
}
