package net.villim.villim;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import static net.villim.villim.VillimKeys.APP_VERSION;

public class SettingsActivity extends AppCompatActivity
        implements LanguagePreferenceDialog.LanguagePreferenceDialogListener,
        CurrencyPreferenceDialog.currencyPreferenceDialogListener {

    private VillimSession session;

    private Toolbar toolbar;


    private Switch pushSwitch;
    private RelativeLayout currencyItem;
    private RelativeLayout languageItem;
    private TextView currencyTextView;
    private TextView languageTextView;
    private TextView versionInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        session = new VillimSession(getApplicationContext());
        boolean loggedIn = session.getLoggedIn();

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow_dark));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Push notifications */
        pushSwitch = (Switch) findViewById(R.id.push_switch);
        pushSwitch.setChecked(session.getPushPref());

        /* Currency */
        currencyItem = (RelativeLayout) findViewById(R.id.currency_item);
        currencyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment currencyDialog = new CurrencyPreferenceDialog();
                currencyDialog.show(getFragmentManager(), "");
            }
        });
        currencyTextView = (TextView) findViewById(R.id.currency_textview);
        String currencyString = VillimUtil.currencyStringFromInt(this, session.getCurrencyPref(), true);
        currencyTextView.setText(currencyString);

        /* Language */
        languageItem = (RelativeLayout) findViewById(R.id.language_item);
        languageItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment languageDialog = new LanguagePreferenceDialog();
                languageDialog.show(getFragmentManager(), "");
            }
        });
        languageTextView = (TextView) findViewById(R.id.language_textview);
        languageTextView.setText(VillimUtil.languageStringFromInt(this, session.getLanguagePref()));

        /* Version info */
        versionInfoTextView = (TextView) findViewById(R.id.version_info_textview);
        versionInfoTextView.setText(APP_VERSION);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /* Currency choose dialog */
    @Override
    public void onCurrencyPicked(DialogFragment dialog, int code) {
        session.setCurrencyPref(code);
        currencyTextView.setText(VillimUtil.currencyStringFromInt(this, session.getCurrencyPref(), true));
        dialog.dismiss();
    }

    /* Language choose dialog */
    @Override
    public void onLanguagePicked(DialogFragment dialog, int code) {
        session.setLanguagePref(code);
        languageTextView.setText(VillimUtil.languageStringFromInt(this, session.getLanguagePref()));
        dialog.dismiss();
    }

}
