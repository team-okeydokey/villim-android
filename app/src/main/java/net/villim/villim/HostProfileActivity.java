package net.villim.villim;

import android.icu.text.SymbolTable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
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
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static net.villim.villim.VillimKeys.HOST_INFO_URL;
import static net.villim.villim.VillimKeys.KEY_ABOUT;
import static net.villim.villim.VillimKeys.KEY_ADDR_SUMMARY;
import static net.villim.villim.VillimKeys.KEY_HOST_ID;
import static net.villim.villim.VillimKeys.KEY_HOST_NAME;
import static net.villim.villim.VillimKeys.KEY_HOST_PROFILE_PIC_URL;
import static net.villim.villim.VillimKeys.KEY_MESSAGE;
import static net.villim.villim.VillimKeys.KEY_QUERY_SUCCESS;
import static net.villim.villim.VillimKeys.SERVER_HOST;
import static net.villim.villim.VillimKeys.SERVER_SCHEME;


public class HostProfileActivity extends VillimActivity {

    private Toolbar toolbar;
    private Button closeButton;

    private ImageView hostPicture;
    private TextView hostName;
    private TextView hostLocation;
    private TextView hostAbout;

    private String hostProfilePicUrl;

    private AVLoadingIndicatorView loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_profile);

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

        /* 호스트 사진 */
        hostPicture = (ImageView) findViewById(R.id.host_picture);
        hostProfilePicUrl = getIntent().getStringExtra(KEY_HOST_PROFILE_PIC_URL);
        Glide.with(getApplicationContext())
                .load(hostProfilePicUrl)
                .into(hostPicture);

        /* 호스트 이름 */
        hostName = (TextView) findViewById(R.id.host_name);
        hostName.setText(getIntent().getStringExtra(KEY_HOST_NAME));

        /* 호스트 장소 */
        hostLocation = (TextView) findViewById(R.id.host_location);
        hostLocation.setText(getIntent().getStringExtra(KEY_ADDR_SUMMARY));

        /* 호스트 설명 */
        hostAbout = (TextView) findViewById(R.id.host_about);

        /* Loading indicator */
        loadingIndicator = (AVLoadingIndicatorView) findViewById(R.id.loading_indicator);

        sendHostInfoRequest();
    }

    private void sendHostInfoRequest() {
        startLoadingAnimation();
        hideErrorMessage();

        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();

                URL url = new HttpUrl.Builder()
                .scheme(SERVER_SCHEME)
                .host(SERVER_HOST)
                .addPathSegments(HOST_INFO_URL)
                .addQueryParameter(KEY_HOST_ID, Integer.toString(getIntent().getIntExtra(KEY_HOST_ID, 0)))
                .build().url();

        Request request = new Request.Builder()
                .url(url)
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
                        /* Set host picture */
                        final String hostPicUrl = jsonObject.getString(KEY_HOST_PROFILE_PIC_URL);
                        final String hostDescriptionString = jsonObject.getString(KEY_ABOUT);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                /* Set host about */
                                hostAbout.setText(hostDescriptionString);

                                /* Compare profile pic urls */
                                if (!hostPicUrl.equals(hostProfilePicUrl)) {
                                    Glide.with(getApplicationContext())
                                            .load(hostPicUrl)
                                            .into(hostPicture);
                                }

                            }
                        });



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
                hostAbout.setText(message);
            }
        });
    }

    public void hideErrorMessage() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hostAbout.setText("");
            }
        });
    }
}
