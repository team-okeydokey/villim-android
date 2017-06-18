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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

import static net.villim.villim.VillimKeys.FIND_PASSWORD_URL;
import static net.villim.villim.VillimKeys.KEY_POST_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_EMAIL;
import static net.villim.villim.VillimKeys.KEY_MESSAGE;
import static net.villim.villim.VillimKeys.SERVER_HOST;
import static net.villim.villim.VillimKeys.SERVER_SCHEME;


public class FindPasswordSuccessActivity extends VillimActivity {

    private Toolbar toolbar;

    private ImageView planeImageView;
    private Button sendAgainButton;
    private Button confirmButton;

    private TextView errorMessage;
    private AVLoadingIndicatorView loadingIndicator;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password_success);

        email = getIntent().getStringExtra(KEY_EMAIL);

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow_dark));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Bottom buttons */
        sendAgainButton = (Button) findViewById(R.id.send_again_button);
        confirmButton = (Button) findViewById(R.id.confirm_button);

         /* Error Message */
        errorMessage = (TextView) findViewById(R.id.error_message);
        errorMessage.setVisibility(View.INVISIBLE);

        /* Loading indicator */
        loadingIndicator = (AVLoadingIndicatorView) findViewById(R.id.loading_indicator);

        /* Make call to server when send again button is pressed. */
        sendAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResetPasswordRequest();
            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

         /* Lock picture */
        planeImageView = (ImageView) findViewById(R.id.plane_imageview);
        Glide.with(this).load(R.drawable.img_paper_plane).into(planeImageView);
    }

    private void sendResetPasswordRequest() {
        startLoadingAnimation();
        hideErrorMessage();

        CookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

        OkHttpClient client = new OkHttpClient.Builder().cookieJar(cookieJar).build();

        RequestBody requestBody = new FormBody.Builder()
                .add(KEY_EMAIL, email)
                .build();

        URL url = new HttpUrl.Builder()
                .scheme(SERVER_SCHEME)
                .host(SERVER_HOST)
                .addPathSegments(FIND_PASSWORD_URL)
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
                    if (jsonObject.getBoolean(KEY_POST_SUCCESS)) {
                        showErrorMessage(getString(R.string.send_again_complete));
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

