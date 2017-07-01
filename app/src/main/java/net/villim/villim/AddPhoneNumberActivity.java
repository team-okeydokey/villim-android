package net.villim.villim;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static net.villim.villim.VillimKeys.CHANGE_PASSCODE_URL;
import static net.villim.villim.VillimKeys.KEY_CHANGE_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_MESSAGE;
import static net.villim.villim.VillimKeys.KEY_PASSCODE;
import static net.villim.villim.VillimKeys.KEY_PASSCODE_CONFIRM;
import static net.villim.villim.VillimKeys.KEY_PHONE_NUMBER;
import static net.villim.villim.VillimKeys.SERVER_HOST;
import static net.villim.villim.VillimKeys.SERVER_SCHEME;

public class AddPhoneNumberActivity extends VillimActivity {
    private static final int VERIFY_PHONE = 401;

    private Toolbar toolbar;
    private EditText phonenumberForm;
    private TextView errorMessage;
    private Button nextButton;

    private AVLoadingIndicatorView loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phone_number);

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow_dark));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Phone number form */
        phonenumberForm = (EditText) findViewById(R.id.phone_number);

        /* Set drawableLeft */
        final Drawable phoneIcon =  getResources().getDrawable(R.drawable.icon_phone);
        int iconSize = getResources().getDimensionPixelSize(R.dimen.login_drawable_size);
        phoneIcon.setBounds(0, 0, iconSize, iconSize);
        phonenumberForm.setCompoundDrawables(phoneIcon, null, null, null);

        /* Error Message */
        errorMessage = (TextView) findViewById(R.id.error_message);
        errorMessage.setVisibility(View.INVISIBLE);

        /* Bottom buttons */
        nextButton = (Button) findViewById(R.id.next_button);

        /* Loading indicator */
        loadingIndicator = (AVLoadingIndicatorView) findViewById(R.id.loading_indicator);

        /* Make call to server when next button is pressed. */
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phonenumberForm.getText().toString().trim();
                boolean allFieldsFilledOut = !TextUtils.isEmpty(phoneNumber);
                boolean tooLong = (phoneNumber.length() > 11);
                boolean tooShort = (phoneNumber.length() < 10);
                boolean allDigits = phoneNumber.matches("[0-9]+");
                boolean validInput = allFieldsFilledOut && !tooLong && !tooShort && allDigits;
                if (validInput) {
                    Intent intent = new Intent(AddPhoneNumberActivity.this, VerifyPhoneNumberActivity.class);
                    startActivityForResult(intent, VERIFY_PHONE);
                } else if (!allFieldsFilledOut) {
                    showErrorMessage(getString(R.string.empty_field));
                } else if (tooLong) {
                    showErrorMessage(getString(R.string.phone_number_too_long));
                } else if (tooShort) {
                    showErrorMessage(getString(R.string.phone_number_too_short));
                } else if (!allDigits) {
                    showErrorMessage(getString(R.string.phone_number_non_digit));
                } else {
                    showErrorMessage(getString(R.string.open_room_error));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /* Requests to reservation activity */
        if (requestCode == VERIFY_PHONE) {
            if (resultCode == Activity.RESULT_OK) {

                String phoneNumber = data.getExtras().getString(KEY_PHONE_NUMBER);

                Intent returnIntent = new Intent();
                returnIntent.putExtra(KEY_PHONE_NUMBER, phoneNumber);
                finish();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                finish();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void startLoadingAnimation() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingIndicator.smoothToShow();
            }
        });
    }

    public void stopLoadingAnimation() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingIndicator.smoothToHide();
            }
        });
    }

    public void showErrorMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                errorMessage.setText(message);
                errorMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideErrorMessage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                errorMessage.setVisibility(View.INVISIBLE);
            }
        });
    }
}
