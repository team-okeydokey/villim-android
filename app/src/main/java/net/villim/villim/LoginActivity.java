package net.villim.villim;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import static net.villim.villim.VillimKeys.KEY_EMAIL;
import static net.villim.villim.VillimKeys.KEY_QUERY_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_MESSAGE;
import static net.villim.villim.VillimKeys.KEY_PASSWORD;
import static net.villim.villim.VillimKeys.KEY_USER_INFO;
import static net.villim.villim.VillimKeys.LOGIN_URL;
import static net.villim.villim.VillimKeys.SERVER_HOST;
import static net.villim.villim.VillimKeys.SERVER_SCHEME;


public class LoginActivity extends VillimActivity {

    private Toolbar toolbar;
    private EditText loginFormEmail;
    private EditText loginFormPassword;
    private TextView errorMessage;

    private Button nextButton;
    private Button findPasswordButton;
    private Button signupButton;

    private AVLoadingIndicatorView loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow_dark));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Login Forms */
        loginFormEmail = (EditText) findViewById(R.id.login_form_email);
        loginFormPassword = (EditText) findViewById(R.id.login_form_password);
        /* Set drawableLeft */
        Drawable emailIcon =  getResources().getDrawable(R.drawable.icon_email);
        Drawable lockIcon =  getResources().getDrawable(R.drawable.icon_lock);
        int iconSize = getResources().getDimensionPixelSize(R.dimen.login_drawable_size);
        emailIcon.setBounds(0, 0, iconSize, iconSize);
        lockIcon.setBounds(0, 0, iconSize, iconSize);
        loginFormEmail.setCompoundDrawables(emailIcon, null, null, null);
        loginFormPassword.setCompoundDrawables(lockIcon, null, null, null);

        /* Error Message */
        errorMessage = (TextView) findViewById(R.id.error_message);
        errorMessage.setVisibility(View.INVISIBLE);

        /* Bottom buttons */
        nextButton = (Button) findViewById(R.id.next_button);
        findPasswordButton = (Button) findViewById(R.id.find_password_button);
        findPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(intent);
            }
        });
        signupButton = (Button) findViewById(R.id.signup_button);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivityForResult(intent, ProfileFragment.SIGNUP);
            }
        });

        /* Loading indicator */
        loadingIndicator = (AVLoadingIndicatorView) findViewById(R.id.loading_indicator);

        /* Make call to server when next button is pressed. */
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean allFieldsFilledOut =
                        !TextUtils.isEmpty(loginFormEmail.getText().toString().trim()) &&
                                !TextUtils.isEmpty(loginFormPassword.getText());
                boolean validInput = allFieldsFilledOut;
                if (validInput) {
                    sendLoginRequest();
                } else {
                    showErrorMessage(getString(R.string.empty_field));
                }
            }
        });

    }

    private void sendLoginRequest() {
        startLoadingAnimation();
        hideErrorMessage();

        CookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

        OkHttpClient client = new OkHttpClient.Builder().cookieJar(cookieJar).build();

        RequestBody requestBody = new FormBody.Builder()
                .add(KEY_EMAIL, loginFormEmail.getText().toString().trim())
                .add(KEY_PASSWORD, loginFormPassword.getText().toString())
                .build();

        URL url = new HttpUrl.Builder()
                .scheme(SERVER_SCHEME)
                .host(SERVER_HOST)
                .addPathSegments(LOGIN_URL)
                .build().url();

        Request request = new Request.Builder()
                .url(url)
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
                    if (jsonObject.getBoolean(KEY_QUERY_SUCCESS)) {
                        VillimUser user = VillimUser.createUserFromJSONObject((JSONObject) jsonObject.get(KEY_USER_INFO));
                        login(user);
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

    private void login(VillimUser user) {
        /* Store session */
        VillimSession session = new VillimSession(getApplicationContext());
        session.setLoggedIn(true);
        session.updateUserSession(user);

        /* Return from activity */
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ProfileFragment.SIGNUP) {
            if (resultCode == Activity.RESULT_OK) {
                VillimUser user = data.getParcelableExtra(VillimKeys.KEY_USER);
                login(user);
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
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
