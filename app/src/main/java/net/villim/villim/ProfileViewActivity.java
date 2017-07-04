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

public class ProfileViewActivity extends VillimActivity {
    private static final int EDIT_PROFILE = 300;

    private Toolbar toolbar;
    private Button editProfileButton;
    private Button bottomButton;

    private LinearLayout firstnameContainer;
    private LinearLayout lastnameContainer;
    private LinearLayout sexContainer;
    private LinearLayout emailContainer;
    private LinearLayout cityContainer;

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
        editProfileButton = (Button) findViewById(R.id.edit_button);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(ProfileViewActivity.this, ProfileEditActivity.class);
                startActivityForResult(editIntent, EDIT_PROFILE);
            }
        });

        /* Profile picture */
        profilePicture = (ImageView) findViewById(R.id.profile_picture);

        /* First name */
        firstnameContent = (TextView) findViewById(R.id.firstname_content);

        /* Last name */
        lastnameContent = (TextView) findViewById(R.id.lastname_content);

        /* Email */
        emailContent = (TextView) findViewById(R.id.email_content);

        /* Phone number */
        phoneNumberContent = (TextView) findViewById(R.id.phone_number_content);

        /* City of residence */
        cityContent = (TextView) findViewById(R.id.city_content);

         /* Loading indicator */
        loadingIndicator = (AVLoadingIndicatorView) findViewById(R.id.loading_indicator);

        populateView();
    }

    private void populateView() {
        /* Profile picture */
        if (session.getProfilePicUrl().isEmpty()) {
            loadDefaultImage();
        } else {
            profilePicUri = Uri.parse(session.getProfilePicUrl());
            loadProfilePhoto(profilePicUri);

        }

         /* First name */
        firstnameContent.setText(session.getFirstName());

        /* Last name */
        lastnameContent.setText(session.getLastName());

        /* Email */
        emailContent.setText(session.getEmail());

        /* Phone number */
        phoneNumberContent.setText(VillimUtils.formatPhoneNumber(session.getPhoneNumber()));

        /* City of residence */
        cityContent.setText(session.getCityOfResidence());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == EDIT_PROFILE && data != null) {

            if (resultCode == Activity.RESULT_OK) {
//                Uri uri = Uri.parse(data.getExtras().getString(ProfileEditActivity.PROFILE_PIC_URI));
//                profilePicUri = uri;
//                loadProfilePhoto(uri);
//                activateBottomButton();
                populateView();
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

//    @Override
//    public void onConfirm(DialogFragment dialog, int dataType, String newData) {
//        dialog.dismiss();
//
//        TextView newDataTextView;
//        switch (dataType) {
//            case FIRSTNAME:
//                newDataTextView = firstnameContent;
//                break;
//            case LASTNAME:
//                newDataTextView = lastnameContent;
//                break;
//            case EMAIL:
//                newDataTextView = emailContent;
//                break;
//            case CITY:
//                newDataTextView = cityContent;
//                break;
//            default:
//                newDataTextView = firstnameContent;
//        }
//
//        boolean notSame = !newDataTextView.getText().toString().trim().equals(newData);
//        boolean valid = !TextUtils.isEmpty(newData);
//        if (notSame && valid) {
//            newDataTextView.setText(newData);
////            activateBottomButton();
//        }
//
//    }

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

    @Override
    public boolean onSupportNavigateUp() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
        return true;
    }

}
