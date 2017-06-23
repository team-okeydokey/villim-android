package net.villim.villim;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
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

public class ProfileViewActivity extends AppCompatActivity implements SimpleEditTextDialog.SimpleEditTextDialogListener {
    private static final int PHOTO_PICKER = 300;

    public static final int FIRSTNAME = 0;
    public static final int LASTNAME = 1;
    public static final int EMAIL = 2;
    public static final int CITY = 3;

    private Toolbar toolbar;
    private Button editProfileButton;
    private Button bottomButton;

    private RelativeLayout firstnameContainer;
    private RelativeLayout lastnameContainer;
    private RelativeLayout sexContainer;
    private RelativeLayout emailContainer;
    private RelativeLayout cityContainer;

    private ImageView profilePicture;
    private TextView firstnameContent;
    private TextView lastnameContent;
    //    private TextView sexContent;
    private TextView emailContent;
    private TextView phoneNumberContent;
    private Button addPhoneNumberButton;
    private TextView cityContent;

    private AVLoadingIndicatorView loadingIndicator;

    private VillimSession session;

    private boolean newProfilePic;
    private Uri profilePicUri;
    private int sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        newProfilePic = false;
        session = new VillimSession(getApplicationContext());
//        sex = session.getSex();


        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow_dark));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Edit profile button */
//        editProfileButton = (Button) findViewById(R.id.edit_button);
//        editProfileButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        /* Profile picture */
        profilePicture = (ImageView) findViewById(R.id.profile_picture);
        if (session.getProfilePicUrl().isEmpty()) {
            loadDefaultImage();
        } else {
            profilePicUri = Uri.parse(session.getProfilePicUrl());
            loadProfilePhoto(profilePicUri);

        }
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhotoIntent, PHOTO_PICKER);
            }
        });

        /* First name */
        firstnameContent = (TextView) findViewById(R.id.firstname_content);
        firstnameContent.setText(session.getFirstName());
        firstnameContainer = (RelativeLayout) findViewById(R.id.firstname_container);
        firstnameContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleEditTextDialog newFragment = SimpleEditTextDialog.newInstance(
                        String.format(getString(R.string.edit_item), getString(R.string.firstname)),
                        FIRSTNAME, getString(R.string.firstname), firstnameContent.getText().toString());
                newFragment.show(getFragmentManager(), "dialog");
            }
        });

        /* Last name */
        lastnameContent = (TextView) findViewById(R.id.lastname_content);
        lastnameContent.setText(session.getLastName());
        lastnameContainer = (RelativeLayout) findViewById(R.id.lastname_container);
        lastnameContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleEditTextDialog newFragment = SimpleEditTextDialog.newInstance(
                        String.format(getString(R.string.edit_item), getString(R.string.lastname)),
                        LASTNAME, getString(R.string.lastname), lastnameContent.getText().toString());
                newFragment.show(getFragmentManager(), "dialog");
            }
        });

        /* Sex */
//        sexContent = (TextView) findViewById(R.id.sex_content);
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.sex_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sexContent.setAdapter(adapter);
//        sexContent.setSelection(session.getSex());
//        sexContent.setText(session.getSex());

        /* Email */
        emailContent = (TextView) findViewById(R.id.email_content);
        emailContent.setText(session.getEmail());
        emailContainer = (RelativeLayout) findViewById(R.id.email_container);
        emailContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleEditTextDialog newFragment = SimpleEditTextDialog.newInstance(
                        String.format(getString(R.string.edit_item), getString(R.string.email)),
                        EMAIL, getString(R.string.email), emailContent.getText().toString());
                newFragment.show(getFragmentManager(), "dialog");
            }
        });

        /* Phone number */
        phoneNumberContent = (TextView) findViewById(R.id.phone_number_content);
        phoneNumberContent.setText(session.getPhoneNumber());

        /* City of residence */
        cityContent = (TextView) findViewById(R.id.city_content);
        cityContent.setText(session.getCityOfResidence());
        cityContainer = (RelativeLayout) findViewById(R.id.city_container);
        cityContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleEditTextDialog newFragment = SimpleEditTextDialog.newInstance(
                        String.format(getString(R.string.edit_item), getString(R.string.city_of_residence)),
                        CITY, getString(R.string.city_of_residence), cityContent.getText().toString());
                newFragment.show(getFragmentManager(), "dialog");
            }
        });

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

        deactivateBottomButton();
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

        RequestBody requestBody;

        if (newProfilePic) {
            File imageFiie = new File(profilePicUri.getPath());
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(KEY_FIRSTNAME, firstnameContent.getText().toString().trim())
                    .addFormDataPart(KEY_LASTNAME, lastnameContent.getText().toString().trim())
//                    .addFormDataPart(KEY_SEX, Integer.toString(sex))
                    .addFormDataPart(KEY_EMAIL, emailContent.getText().toString().trim())
                    .addFormDataPart(KEY_PHONE_NUMBER, phoneNumberContent.getText().toString().trim())
                    .addFormDataPart(KEY_CITY_OF_RESIDENCE, cityContent.getText().toString().trim())
                    .addFormDataPart(KEY_PROFILE_PIC, imageFiie.getName(),
                            RequestBody.create(MediaType.parse(imageFiie.getPath()), imageFiie))
                    .build();
        } else {
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(KEY_FIRSTNAME, firstnameContent.getText().toString().trim())
                    .addFormDataPart(KEY_LASTNAME, lastnameContent.getText().toString().trim())
//                    .addFormDataPart(KEY_SEX, Integer.toString(sex))
                    .addFormDataPart(KEY_EMAIL, emailContent.getText().toString().trim())
                    .addFormDataPart(KEY_PHONE_NUMBER, phoneNumberContent.getText().toString().trim())
                    .addFormDataPart(KEY_CITY_OF_RESIDENCE, cityContent.getText().toString().trim())
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

        if (requestCode == PHOTO_PICKER && data != null) {

            if (resultCode == Activity.RESULT_OK) {
                newProfilePic = true;
                Uri uri = data.getData();
                profilePicUri = uri;
                loadProfilePhoto(uri);
                activateBottomButton();
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

    @Override
    public void onConfirm(DialogFragment dialog, int dataType, String newData) {
        dialog.dismiss();

        TextView newDataTextView;
        switch (dataType) {
            case FIRSTNAME:
                newDataTextView = firstnameContent;
                break;
            case LASTNAME:
                newDataTextView = lastnameContent;
                break;
            case EMAIL:
                newDataTextView = emailContent;
                break;
            case CITY:
                newDataTextView = cityContent;
                break;
            default:
                newDataTextView = firstnameContent;
        }

        boolean notSame = !newDataTextView.getText().toString().trim().equals(newData);
        boolean valid = !TextUtils.isEmpty(newData);
        if (notSame && valid) {
            newDataTextView.setText(newData);
            activateBottomButton();
        }

    }

    private void loadProfilePhoto(Uri uri) {
        profilePicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(this).load(uri).into(profilePicture);
    }

    private void loadDefaultImage() {
        profilePicture.setScaleType(ImageView.ScaleType.CENTER);

        int iconSize = getResources().getDimensionPixelSize(R.dimen.photo_icon_size);

        Drawable cameraIcon = getResources().getDrawable(R.drawable.ic_photo_camera_white_48dp);

        cameraIcon.setBounds(0, 0, iconSize, iconSize);

//        Glide.with(this)
//                .load("")
//                .placeholder(cameraIcon)
//                .override(iconSize, iconSize)
//                .into(profilePicture);

        profilePicture.setImageDrawable(cameraIcon);

    }

    private void activateBottomButton() {
        bottomButton.setEnabled(true);
        bottomButton.setBackground(getResources().getDrawable(R.drawable.next_button));
        bottomButton.setTextColor(getResources().getColorStateList(R.color.white_text_button));
    }

    private void deactivateBottomButton() {
        bottomButton.setEnabled(false);
        bottomButton.setBackgroundColor(getResources().getColor(R.color.red_button_disabled));
        bottomButton.setTextColor(Color.WHITE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
        return true;
    }

}
