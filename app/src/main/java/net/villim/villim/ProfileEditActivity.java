package net.villim.villim;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.MenuPopupWindow;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static net.villim.villim.VillimKeys.KEY_CITY_OF_RESIDENCE;
import static net.villim.villim.VillimKeys.KEY_EMAIL;
import static net.villim.villim.VillimKeys.KEY_FIRSTNAME;
import static net.villim.villim.VillimKeys.KEY_LASTNAME;
import static net.villim.villim.VillimKeys.KEY_MESSAGE;
import static net.villim.villim.VillimKeys.KEY_PHONE_NUMBER;
import static net.villim.villim.VillimKeys.KEY_PROFILE_PIC;
import static net.villim.villim.VillimKeys.KEY_SEX;
import static net.villim.villim.VillimKeys.KEY_UPDATE_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_USER_INFO;
import static net.villim.villim.VillimKeys.SERVER_HOST;
import static net.villim.villim.VillimKeys.SERVER_SCHEME;
import static net.villim.villim.VillimKeys.UPDATE_PROFILE_URL;

public class ProfileEditActivity extends AppCompatActivity {
    private static final int PHOTO_PICKER = 300;

    private Toolbar toolbar;
    private Button closeButton;

    private ImageView profilePicture;
    private EditText firstnameForm;
    private EditText lastnameForm;
    private Spinner sexSpinner;
    private EditText emailForm;
    private EditText phoneNumberForm;
    private Button addPhoneNumberButton;
    private EditText cityForm;

    private Button bottomButton;
    private AVLoadingIndicatorView loadingIndicator;

    private VillimSession session;

    private boolean newProfilePic;
    private Uri profilePicUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        newProfilePic = false;
        session = new VillimSession(getApplicationContext());

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
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

        /* Profile picture */
        profilePicture = (ImageView) findViewById(R.id.profile_picture);
        if (session.getProfilePicUrl().isEmpty()) {
            Glide.with(this)
                    .load(R.drawable.img_default)
                    .into(profilePicture);
        } else {
            Glide.with(this)
                    .load(session.getProfilePicUrl())
                    .into(profilePicture);
        }
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhotoIntent, PHOTO_PICKER);//one can be replaced with any action code
            }
        });

        /* First & last name */
        firstnameForm = (EditText) findViewById(R.id.firstname_form);
        firstnameForm.setText(session.getFirstName());
        lastnameForm = (EditText) findViewById(R.id.lastname_form);
        lastnameForm.setText(session.getLastName());

        /* Sex */
        sexSpinner = (Spinner) findViewById(R.id.sex_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sex_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexSpinner.setAdapter(adapter);
        sexSpinner.setSelection(session.getSex());

        /* Email */
        emailForm = (EditText) findViewById(R.id.email_form);
        emailForm.setText(session.getEmail());

        /* Phone number */
        phoneNumberForm = (EditText) findViewById(R.id.phone_number_form);
        phoneNumberForm.setText(session.getPhoneNumber());

        /* City of residence */
        cityForm = (EditText) findViewById(R.id.city_form);
        cityForm.setText(session.getCityOfResidence());

        /* Buttom button */
        bottomButton = (Button) findViewById(R.id.bottom_button);
        bottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUpdateProfileRequest();
            }
        });

         /* Loading indicator */
        loadingIndicator = (AVLoadingIndicatorView) findViewById(R.id.loading_indicator);
    }


    private void sendUpdateProfileRequest() {
        startLoadingAnimation();

        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();

        URL url = new HttpUrl.Builder()
                .scheme(SERVER_SCHEME)
                .host(SERVER_HOST)
                .addPathSegments(UPDATE_PROFILE_URL)
                .build().url();

        File imageFiie = new File(profilePicUri.getPath());

        RequestBody requestBody;

        if (newProfilePic) {
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(KEY_FIRSTNAME, firstnameForm.getText().toString().trim())
                    .addFormDataPart(KEY_LASTNAME, lastnameForm.getText().toString().trim())
                    .addFormDataPart(KEY_SEX, Integer.toString(sexSpinner.getSelectedItemPosition()))
                    .addFormDataPart(KEY_EMAIL, emailForm.getText().toString().trim())
                    .addFormDataPart(KEY_PHONE_NUMBER, phoneNumberForm.getText().toString().trim())
                    .addFormDataPart(KEY_CITY_OF_RESIDENCE, cityForm.getText().toString().trim())
                    .build();
        } else {
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(KEY_FIRSTNAME, firstnameForm.getText().toString().trim())
                    .addFormDataPart(KEY_LASTNAME, lastnameForm.getText().toString().trim())
                    .addFormDataPart(KEY_SEX, Integer.toString(sexSpinner.getSelectedItemPosition()))
                    .addFormDataPart(KEY_EMAIL, emailForm.getText().toString().trim())
                    .addFormDataPart(KEY_PHONE_NUMBER, phoneNumberForm.getText().toString().trim())
                    .addFormDataPart(KEY_CITY_OF_RESIDENCE, cityForm.getText().toString().trim())
                    .addFormDataPart(KEY_PROFILE_PIC, imageFiie.getName(),
                            RequestBody.create(MediaType.parse(imageFiie.getPath()), imageFiie))
                    .build();
        }

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
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.getBoolean(KEY_UPDATE_SUCCESS)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), getString(R.string.profile_updated),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                        VillimUser user = VillimUser.createUserFromJSONObject(jsonObject.getJSONObject(KEY_USER_INFO));
                        session.updateUserSession(user);
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else {
                        showErrorMessage(jsonObject.getString(KEY_MESSAGE));
                    }
                    stopLoadingAnimation();
                } catch (JSONException e) {
                    showErrorMessage(getString(R.string.open_room_error));
                    stopLoadingAnimation();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PHOTO_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                newProfilePic = true;
                Uri uri = data.getData();
                profilePicUri = uri;
                Glide.with(this).load(uri).into(profilePicture);
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
                Toast.makeText(getApplicationContext(), message,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

}
