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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static net.villim.villim.VillimKeys.CHANGE_PASSCODE_URL;
import static net.villim.villim.VillimKeys.KEY_CHANGE_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_MESSAGE;
import static net.villim.villim.VillimKeys.KEY_PASSCODE;
import static net.villim.villim.VillimKeys.KEY_PASSCODE_CONFIRM;

public class ChangePasscodeActivity extends AppCompatActivity {

    private static final int PASSCODE_CHANGE_SUCCESS = 0;

    private Toolbar toolbar;
    private EditText newPasscodeForm;
    private EditText newPasscodeConfirmForm;
    private TextView errorMessage;

    private Button nextButton;


    private AVLoadingIndicatorView loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passcode);

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow_dark));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Passcode Forms */
        newPasscodeForm = (EditText) findViewById(R.id.new_passcode);
        newPasscodeConfirmForm = (EditText) findViewById(R.id.new_passcode_confirm);
        /* Set drawableLeft */
        Drawable emailIcon =  getResources().getDrawable(R.drawable.icon_lock);
        Drawable lockIcon =  getResources().getDrawable(R.drawable.icon_lock);
        int iconSize = getResources().getDimensionPixelSize(R.dimen.login_drawable_size);
        emailIcon.setBounds(0, 0, iconSize, iconSize);
        lockIcon.setBounds(0, 0, iconSize, iconSize);
        newPasscodeForm.setCompoundDrawables(emailIcon, null, null, null);
        newPasscodeConfirmForm.setCompoundDrawables(lockIcon, null, null, null);

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
                String passcode = newPasscodeForm.getText().toString().trim();
                String passcodeConfirm = newPasscodeConfirmForm.getText().toString().trim();
                boolean allFieldsFilledOut = !TextUtils.isEmpty(passcode) && !TextUtils.isEmpty(passcodeConfirm);
                boolean same = passcode.equals(passcodeConfirm);
                boolean tooLong = (passcode.length() > 12) && (passcodeConfirm.length() > 12);
                boolean tooShort = (passcode.length() < 4) && (passcodeConfirm.length() < 4);
                boolean allDigits = passcode.matches("[0-9]+") && passcodeConfirm.matches("[0-9]+");
                boolean validInput = allFieldsFilledOut && same && !tooLong && !tooShort && allDigits;
                if (validInput) {
                    sendPasscodeChangeRequest();
                } else if (!allFieldsFilledOut) {
                    showErrorMessage(getString(R.string.empty_field));
                } else if (!same) {
                    showErrorMessage(getString(R.string.passcode_different));
                } else if (tooLong) {
                    showErrorMessage(getString(R.string.passcode_too_long));
                } else if (tooShort) {
                    showErrorMessage(getString(R.string.passcode_too_short));
                } else if (!allDigits) {
                    showErrorMessage(getString(R.string.passcode_non_digit));
                } else {
                    showErrorMessage(getString(R.string.open_room_error));
                }
            }
        });
    }

    private void sendPasscodeChangeRequest() {
        startLoadingAnimation();
        hideErrorMessage();

        CookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

        OkHttpClient client = new OkHttpClient.Builder().cookieJar(cookieJar).build();

        RequestBody requestBody = new FormBody.Builder()
                .add(KEY_PASSCODE, newPasscodeForm.getText().toString().trim())
                .add(KEY_PASSCODE_CONFIRM, newPasscodeForm.getText().toString().trim())
                .build();

        Request request = new Request.Builder()
                .url(CHANGE_PASSCODE_URL)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Something went wrong.
                showErrorMessage(getString(R.string.cant_connect_to_server));
                stopLoadingAnimation();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    showErrorMessage(getString(R.string.server_error));
                    stopLoadingAnimation();
                    throw new IOException("Response not successful   " + response);
                }
                /* Request success. */
                try {
                    /* 주의: response.body().string()은 한 번 부를 수 있음 */
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.getBoolean(KEY_CHANGE_SUCCESS)) {
                        Intent intent = new Intent(ChangePasscodeActivity.this, ChangePasscodeSuccessActivity.class);
                        startActivityForResult(intent, PASSCODE_CHANGE_SUCCESS);
                        finish();
                    } else {
                        showErrorMessage(jsonObject.getString(KEY_MESSAGE));
                        stopLoadingAnimation();
                    }
                } catch (JSONException e) {
                    showErrorMessage(getString(R.string.server_error));
                    stopLoadingAnimation();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /* Requests to reservation activity */
        if (requestCode == PASSCODE_CHANGE_SUCCESS) {
            if (resultCode == Activity.RESULT_OK) {
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
