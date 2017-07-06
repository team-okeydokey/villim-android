package net.villim.villim;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import static net.villim.villim.VillimKeys.KEY_EMAIL;
import static net.villim.villim.VillimKeys.KEY_MESSAGE;
import static net.villim.villim.VillimKeys.KEY_PHONE_NUMBER;
import static net.villim.villim.VillimKeys.KEY_POST_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_VERIFICATION_CODE;
import static net.villim.villim.VillimKeys.SERVER_HOST;
import static net.villim.villim.VillimKeys.SERVER_SCHEME;
import static net.villim.villim.VillimKeys.VERIFY_PHONE_URL;

public class VerifyPhoneNumberActivity extends VillimActivity {

    private Toolbar toolbar;
    private EditText code1;
    private EditText code2;
    private EditText code3;
    private EditText code4;
    private EditText code5;
    private EditText code6;

    private Button nextButton;

    private TextView errorMessage;
    private AVLoadingIndicatorView loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_number);

        final String phoneNumber = getIntent().getStringExtra(KEY_PHONE_NUMBER);

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow_dark));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Verification code Forms */
        code1 = (EditText) findViewById(R.id.code_form_1);
        code2 = (EditText) findViewById(R.id.code_form_2);
        code3 = (EditText) findViewById(R.id.code_form_3);
        code4 = (EditText) findViewById(R.id.code_form_4);
        code5 = (EditText) findViewById(R.id.code_form_5);
        code6 = (EditText) findViewById(R.id.code_form_6);

        code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (code1.getText().toString().length() == 1) {
                    code2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (code2.getText().toString().length() == 1) {
                    code3.requestFocus();
                } else if (code2.getText().toString().length() == 0) {
                    code2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (code3.getText().toString().length() == 1) {
                    code4.requestFocus();
                } else if (code3.getText().toString().length() == 0) {
                    code2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (code4.getText().toString().length() == 1) {
                    code5.requestFocus();
                } else if (code4.getText().toString().length() == 0) {
                    code3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (code5.getText().toString().length() == 1) {
                    code6.requestFocus();
                }
                else if (code5.getText().toString().length() == 0) {
                    code4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (code6.getText().toString().length() == 0) {
                    code5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


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

                String code1Input = code1.getText().toString().trim();
                String code2Input = code2.getText().toString().trim();
                String code3Input = code3.getText().toString().trim();
                String code4Input = code4.getText().toString().trim();
                String code5Input = code5.getText().toString().trim();
                String code6Input = code6.getText().toString().trim();

                boolean allFieldsFilledOut = !TextUtils.isEmpty(code1Input) &&
                        !TextUtils.isEmpty(code2Input) &&
                        !TextUtils.isEmpty(code3Input) &&
                        !TextUtils.isEmpty(code4Input) &&
                        !TextUtils.isEmpty(code5Input) &&
                        !TextUtils.isEmpty(code6Input);

                boolean allDigits = code1Input.matches("[0-9]+") &&
                        code2Input.matches("[0-9]+") &&
                        code3Input.matches("[0-9]+") &&
                        code4Input.matches("[0-9]+") &&
                        code5Input.matches("[0-9]+") &&
                        code6Input.matches("[0-9]+");

                boolean validInput = allFieldsFilledOut && allDigits;
                if (validInput) {
                    String verificationCode = extractCodeInput();
                    sendVerifyPhoneRequest(phoneNumber, verificationCode);
                } else if (!allFieldsFilledOut) {
                    showErrorMessage(getString(R.string.empty_field));
                } else if (!allDigits) {
                    showErrorMessage(getString(R.string.phone_number_non_digit));
                }
            }
        });
    }

    public String extractCodeInput() {
        String code1Input = code1.getText().toString().trim();
        String code2Input = code2.getText().toString().trim();
        String code3Input = code3.getText().toString().trim();
        String code4Input = code4.getText().toString().trim();
        String code5Input = code5.getText().toString().trim();
        String code6Input = code6.getText().toString().trim();
        return code1Input + code2Input + code3Input + code4Input + code5Input + code6Input;
    }

    private void sendVerifyPhoneRequest(String phoneNumber, String verificationCode) {
        startLoadingAnimation();
        hideErrorMessage();

        CookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

        OkHttpClient client = new OkHttpClient.Builder().cookieJar(cookieJar).build();

        RequestBody requestBody = new FormBody.Builder()
                .add(KEY_PHONE_NUMBER, phoneNumber)
                .add(KEY_VERIFICATION_CODE, verificationCode)
                .build();

        URL url = new HttpUrl.Builder()
                .scheme(SERVER_SCHEME)
                .host(SERVER_HOST)
                .addPathSegments(VERIFY_PHONE_URL)
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
                    if (jsonObject.getBoolean(KEY_SUCCESS)) {
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else {
                        showErrorMessage(jsonObject.getString(KEY_MESSAGE));
                    }
                    stopLoadingAnimation();
                } catch (JSONException e) {
                    showErrorMessage(getString(R.string.server_error));
                    stopLoadingAnimation();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
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
