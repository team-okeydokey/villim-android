package net.villim.villim;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

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
                        .addFormDataPart("email", "someValue")
                        .build();

                Request request = new Request.Builder()
                        .url("http://www.mocky.io/v2/593b6bca100000a101c476e9")
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
                        System.out.println(response.body().string());
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
}
