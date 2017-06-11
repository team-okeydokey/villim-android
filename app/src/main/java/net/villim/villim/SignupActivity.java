package net.villim.villim;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static net.villim.villim.VillimKeys.KEY_EMAIL;
import static net.villim.villim.VillimKeys.KEY_FIRSTNAME;
import static net.villim.villim.VillimKeys.KEY_LASTNAME;
import static net.villim.villim.VillimKeys.KEY_LOGIN_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_MESSAGE;
import static net.villim.villim.VillimKeys.KEY_PASSWORD;
import static net.villim.villim.VillimKeys.KEY_SIGNUP_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_USER;
import static net.villim.villim.VillimKeys.KEY_USER_INFO;

public class SignupActivity extends AppCompatActivity {

    private static final String LOGIN_URL = "http://175.207.29.19/a/signup";

    private Toolbar toolbar;

    private EditText signupFormLastname;
    private EditText signupFormFirstname;
    private EditText signupFormEmail;
    private EditText signupFormPassword;
    private TextView errorMessage;

    private Button nextButton;

    private AVLoadingIndicatorView loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow_dark));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Signup Forms */
        signupFormLastname = (EditText) findViewById(R.id.signup_form_email);
        signupFormFirstname = (EditText) findViewById(R.id.signup_form_password);
        signupFormEmail = (EditText) findViewById(R.id.signup_form_email);
        signupFormPassword = (EditText) findViewById(R.id.signup_form_password);
//        signupFormLastname.setOnKeyListener(new View.OnKeyListener() {
//            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
//                //If the keyevent is a key-down event on the "enter" button
//                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    signupFormFirstname.requestFocus();
//                }
//                return false;
//            }
//        });
//        signupFormFirstname.setOnKeyListener(new View.OnKeyListener() {
//            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
//                //If the keyevent is a key-down event on the "enter" button
//                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    signupFormEmail.requestFocus();
//                }
//                return false;
//            }
//        });
//        signupFormEmail.setOnKeyListener(new View.OnKeyListener() {
//            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
//                //If the keyevent is a key-down event on the "enter" button
//                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    signupFormPassword.requestFocus();
//                }
//                return false;
//            }
//        });
//        signupFormPassword.setOnKeyListener(new View.OnKeyListener() {
//            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
//                //If the keyevent is a key-down event on the "enter" button
//                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    signupFormPassword.clearFocus();
//                    return true;
//                }
//                return false;
//            }
//        });

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
                boolean allFieldsFilledOut =
                        !TextUtils.isEmpty(signupFormLastname.getText().toString().trim()) &&
                        !TextUtils.isEmpty(signupFormFirstname.getText().toString().trim()) &&
                        !TextUtils.isEmpty(signupFormEmail.getText().toString().trim()) &&
                        !TextUtils.isEmpty(signupFormPassword.getText());
                boolean validInput = allFieldsFilledOut;
                if (validInput) {
                    sendRequest();
                } else {
                    showErrorMessage(getString(R.string.empty_field));
                }
            }
        });
    }

    private void sendRequest() {
        startLoadingAnimation();
        hideErrorMessage();

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add(KEY_FIRSTNAME, signupFormFirstname.getText().toString().trim())
                .add(KEY_LASTNAME, signupFormLastname.getText().toString().trim())
                .add(KEY_EMAIL, signupFormEmail.getText().toString().trim())
                .add(KEY_PASSWORD, signupFormPassword.getText().toString().trim())
                .build();

        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //something went wrong
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    showErrorMessage(getString(R.string.server_error));
                    stopLoadingAnimation();
                    throw new IOException("Response not successful   " + response);
                }
                //success do whatever you want. for example -->
                try {
                    /* 주의: response.body().string()은 한 번 부를 수 있음 */
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.getBoolean(KEY_SIGNUP_SUCCESS)) {
                        VillimUser user = VillimUser.createUserFromJSONObject((JSONObject) jsonObject.get(KEY_USER_INFO));
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(KEY_USER, user);
                        setResult(Activity.RESULT_OK, returnIntent);
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
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
