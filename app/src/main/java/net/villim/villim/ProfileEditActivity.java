package net.villim.villim;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import static net.villim.villim.VillimKeys.KEY_QUERY_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_USER_INFO;
import static net.villim.villim.VillimKeys.SERVER_HOST;
import static net.villim.villim.VillimKeys.SERVER_SCHEME;
import static net.villim.villim.VillimKeys.UPDATE_PROFILE_URL;

public class ProfileEditActivity extends VillimActivity {
    private static final int PHOTO_PICKER = 300;
    private static final int ADD_PHONE_NUMBER = 301;
    public static final String PROFILE_PIC_URI = "profile_pic_uri";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    public static final int FIRSTNAME = 0;
    public static final int LASTNAME = 1;
    public static final int EMAIL = 2;
    public static final int CITY = 3;

    private Toolbar toolbar;
    private Button saveButton;

    private ImageView profilePicture;
    private EditText firstnameContent;
    private EditText lastnameContent;
    //    private TextView sexContent;
    private EditText emailContent;
    private TextView phoneNumberContent;
    private Button addPhoneNumberButton;
    private EditText cityContent;

    private AVLoadingIndicatorView loadingIndicator;

    private VillimSession session;

    private boolean newFirstname;
    private boolean newLastname;
    private boolean newEmail;
    private boolean newPhoneNumber;
    private boolean newCity;
    private boolean newProfilePic;

    private Uri profilePicUri;
    private String phoneNumber;
    private int sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        newFirstname = false;
        newLastname = false;
        newEmail = false;
        newPhoneNumber = false;
        newCity = false;
        newProfilePic = false;
        session = new VillimSession(getApplicationContext());

        phoneNumber = session.getPhoneNumber();

//        sex = session.getSex();


        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.back_arrow_dark));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Edit profile button */
        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUpdateProfileRequest();
            }
        });

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
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhotoIntent, PHOTO_PICKER);
            }
        });

        /* First name */
        firstnameContent = (EditText) findViewById(R.id.firstname_content);
        firstnameContent.setText(session.getFirstName());
        firstnameContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().equals(session.getFirstName())) {
                    newFirstname = false;
                } else {
                    newFirstname = true;
                }
                updateSaveButtonStatus();
            }
        });

        /* Last name */
        lastnameContent = (EditText) findViewById(R.id.lastname_content);
        lastnameContent.setText(session.getLastName());
        lastnameContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().equals(session.getLastName())) {
                    newLastname = false;
                } else {
                    newLastname = true;
                }
                updateSaveButtonStatus();
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
        emailContent = (EditText) findViewById(R.id.email_content);
        emailContent.setText(session.getEmail());
        emailContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().equals(session.getEmail())) {
                    newEmail = false;
                } else {
                    newEmail = true;
                }
                updateSaveButtonStatus();
            }
        });

        /* Phone number */
        phoneNumberContent = (TextView) findViewById(R.id.phone_number_content);
        phoneNumberContent.setText(VillimUtils.formatPhoneNumber(session.getPhoneNumber()));
        addPhoneNumberButton = (Button) findViewById(R.id.add_phone_number_button);
        addPhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileEditActivity.this, AddPhoneNumberActivity.class);
                startActivityForResult(intent, ADD_PHONE_NUMBER);
            }
        });

        /* City of residence */
        cityContent = (EditText) findViewById(R.id.city_content);
        cityContent.setText(session.getCityOfResidence());
        cityContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().equals(session.getCityOfResidence())) {
                    newCity = false;
                } else {
                    newCity = true;
                }
                updateSaveButtonStatus();
            }
        });

        /* Buttom button */
//        bottomButton = (Button) findViewById(R.id.bottom_button);
//        bottomButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendUpdateProfileRequest();
//            }
//        });

         /* Loading indicator */
        loadingIndicator = (AVLoadingIndicatorView) findViewById(R.id.loading_indicator);

        updateSaveButtonStatus();
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

            File imageFiie = session.getLocalStoreProfilePictureFile();

            if (imageFiie == null) {
                showErrorMessage(getString(R.string.upload_error));
                stopLoadingAnimation();
                return;
            } else {
                requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart(KEY_FIRSTNAME, firstnameContent.getText().toString().trim())
                        .addFormDataPart(KEY_LASTNAME, lastnameContent.getText().toString().trim())
                        .addFormDataPart(KEY_EMAIL, emailContent.getText().toString().trim())
                        .addFormDataPart(KEY_PHONE_NUMBER, phoneNumber)
                        .addFormDataPart(KEY_CITY_OF_RESIDENCE, cityContent.getText().toString().trim())
                        .addFormDataPart(KEY_PROFILE_PIC, imageFiie.getName(),
                                RequestBody.create(MEDIA_TYPE_PNG, imageFiie))
                        .build();

            }

        } else {
            requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(KEY_FIRSTNAME, firstnameContent.getText().toString().trim())
                    .addFormDataPart(KEY_LASTNAME, lastnameContent.getText().toString().trim())
                    .addFormDataPart(KEY_EMAIL, emailContent.getText().toString().trim())
                    .addFormDataPart(KEY_PHONE_NUMBER, phoneNumber)
                    .addFormDataPart(KEY_CITY_OF_RESIDENCE, cityContent.getText().toString().trim())
                    .build();
        }

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        client.newCall(request).

                enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        // Something went wrong.
                        showErrorMessage(getString(R.string.cant_connect_to_server));
                        stopLoadingAnimation();
                        e.printStackTrace();
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
                            if (jsonObject.getBoolean(KEY_QUERY_SUCCESS)) {
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
                                returnIntent.putExtra(PROFILE_PIC_URI, profilePicUri.toString());
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

    private String getImagePath(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int columnIndex = cursor.getColumnIndex(proj[0]);
            cursor.moveToFirst();
            return cursor.getString(columnIndex);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private static String getImageUrlWithAuthority(Context context, Uri uri) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(context, bmp).toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static Uri writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PHOTO_PICKER && data != null) {

            if (resultCode == Activity.RESULT_OK) {
                newProfilePic = true;
                Uri uri = data.getData();
                profilePicUri = uri;
                savePhotoCache(profilePicUri);
                loadProfilePhoto(profilePicUri);
                updateSaveButtonStatus();
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        } else if (requestCode == ADD_PHONE_NUMBER && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                phoneNumber = data.getExtras().getString(KEY_PHONE_NUMBER).trim();
                String phoneNumberString = VillimUtils.formatPhoneNumber(phoneNumber);
                phoneNumberContent.setText(phoneNumberString);
                newPhoneNumber = true;
                updateSaveButtonStatus();
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
//            activateSaveButton();
//        }
//
//    }

    private void savePhotoCache(Uri uri) {
        /* Save image into cache */
        File filepath = Environment.getExternalStorageDirectory();
        File dir = new File(filepath.getAbsolutePath()
                + "/" + getString(R.string.app_name));
        dir.mkdirs();

        File imageFiie = new File(dir, session.getUserId() + ".jpg");
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//            OutputStream os = new BufferedOutputStream(new FileOutputStream(imageFiie));
            OutputStream os = new FileOutputStream(imageFiie);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);
            os.flush();
            os.close();
        } catch (IOException e) {
            imageFiie = null;
        }

        /* Update session */
        session.setLocalStoreProfilePicturePath(imageFiie.getAbsolutePath());
    }

    private void loadProfilePhoto(Uri uri) {
        profilePicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
        File profilePic = session.getLocalStoreProfilePictureFile();

        if (profilePic == null) {
            Glide.with(this).load(session.getProfilePicUrl()).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(profilePicture);
        } else {
            Glide.with(this).load(profilePic).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true).into(profilePicture);
        }
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

    private void updateSaveButtonStatus() {
        if (newFirstname || newLastname || newProfilePic || newCity || newEmail || newPhoneNumber) {
            activateSaveButton();
        } else {
            deactivateSaveButton();
        }
    }

    private void activateSaveButton() {
        saveButton.setEnabled(true);
        saveButton.setTextColor(getResources().getColorStateList(R.color.blue_text_button));
    }

    private void deactivateSaveButton() {
        saveButton.setEnabled(false);
        saveButton.setTextColor(getResources().getColor(R.color.dark_button_disabled));
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
        return true;
    }


}
