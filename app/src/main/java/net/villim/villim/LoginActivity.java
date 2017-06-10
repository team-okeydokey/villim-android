package net.villim.villim;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static net.villim.villim.VillimKeys.KEY_EMAIL;
import static net.villim.villim.VillimKeys.KEY_ID;
import static net.villim.villim.VillimKeys.KEY_LOGIN_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_NAME;
import static net.villim.villim.VillimKeys.KEY_PASSWORD;
import static net.villim.villim.VillimKeys.KEY_PROFILE_PIC_URL;
import static net.villim.villim.VillimKeys.KEY_USER_INFO;

public class LoginActivity extends AppCompatActivity {

    private static final String LOGIN_URL = "http://www.mocky.io/v2/593c2915100000c816c477e4";

    private Toolbar toolbar;
    private EditText loginFormEmail;
    private EditText loginFormPassword;

    private Button nextButton;
    private Button findPasswordButton;
    private Button signupButton;

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

        /* Bottom buttons */
        nextButton = (Button) findViewById(R.id.next_button);
        findPasswordButton = (Button) findViewById(R.id.find_password_button);
        signupButton = (Button) findViewById(R.id.signup_button);

        /* Make call to server when next button is pressed. */
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkHttpClient client = new OkHttpClient();

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart(KEY_EMAIL, loginFormEmail.getText().toString())
                        .addFormDataPart(KEY_PASSWORD, loginFormPassword.getText().toString())
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
                            throw new IOException("Response not successful   " + response);
                        }
                        //success do whatever you want. for example -->
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());
                            if ((boolean)jsonObject.get(KEY_LOGIN_SUCCESS)){
                                login((JSONObject) jsonObject.get(KEY_USER_INFO));
                            }
                        } catch (JSONException e) {
                        }
                    }
                });
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

    private void login(JSONObject userInfo) {
        /* Store session */
        VillimSession session = new VillimSession(getApplicationContext());
        session.setLoggedIn(true);

        /* Create user instance */
        VillimUser user = new VillimUser();
        try {
            /* No need to null check here because if we dont set it, it's going to be null anyway */
            user.id = Integer.parseInt(userInfo.get(KEY_ID).toString());
            user.name = userInfo.get(KEY_NAME).toString();
            user.email = userInfo.get(KEY_EMAIL).toString();
            user.profilePicUrl = userInfo.get(KEY_PROFILE_PIC_URL).toString();

            /* Store basic info in shared preferences */
            session.setId(user.id);
            session.setName(user.name);
            session.setEmail(user.email);
            session.setProfilePicUrl(user.profilePicUrl);

            /* Return from activity */
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();

        } catch (JSONException e) {

        }

    }
}
