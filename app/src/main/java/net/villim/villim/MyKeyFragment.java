package net.villim.villim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.rojoxpress.slidebutton.SlideButton;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static net.villim.villim.VillimKeys.KEY_END_DATE;
import static net.villim.villim.VillimKeys.KEY_HOUSE_NAME;
import static net.villim.villim.VillimKeys.KEY_HOUSE_THUMBNAIL_URL;
import static net.villim.villim.VillimKeys.KEY_ID;
import static net.villim.villim.VillimKeys.KEY_MESSAGE;
import static net.villim.villim.VillimKeys.KEY_OPEN_AUTHORIZED;
import static net.villim.villim.VillimKeys.KEY_OPEN_SUCESS;
import static net.villim.villim.VillimKeys.KEY_QUERY_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_RESERVATION_ACTIVE;
import static net.villim.villim.VillimKeys.KEY_ROOM_ID;
import static net.villim.villim.VillimKeys.KEY_START_DATE;
import static net.villim.villim.VillimKeys.MY_HOUSE_URL;
import static net.villim.villim.VillimKeys.OPEN_DOORLOCK_URL;
import static net.villim.villim.VillimKeys.SERVER_HOST;
import static net.villim.villim.VillimKeys.SERVER_SCHEME;


public class MyKeyFragment extends Fragment {

    private MainActivity activity;

    private ImageView houseThumbnail;
    private TextView houseNameTextView;
    private TextView validDates;
    private TextView errorMessage;
    private SlideButton slideButton;
    private Button reviewButton;
    private Button changePasscodeButton;

    private AVLoadingIndicatorView loadingIndicator;

    private VillimSession session;

    private String houseName;
    private String houseThumbnailUrl;
    private Date startDate;
    private Date endDate;

    public MyKeyFragment() {
        // Required empty public constructor
    }

    public static MyKeyFragment newInstance() {
        MyKeyFragment fragment = new MyKeyFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        View myKeyView = inflater.inflate(R.layout.fragment_my_key, container, false);
        activity = ((MainActivity) getActivity());

        /* Retrieve session. */
        session = new VillimSession(getActivity().getApplicationContext());

        /* House name & dates */
        houseNameTextView = (TextView) myKeyView.findViewById(R.id.house_name);
        validDates = (TextView) myKeyView.findViewById(R.id.valid_dates);

        /* House thumbnail */
        houseThumbnail = (ImageView) myKeyView.findViewById(R.id.house_thumbnail);

        /* Slide Button */
        slideButton = (SlideButton) myKeyView.findViewById(R.id.slide_button);
        slideButton.setClickable(false);
        slideButton.setEnabled(false);

        /* Review button */
        reviewButton = (Button) myKeyView.findViewById(R.id.review_button);
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewIntent = new Intent(activity, ReviewActivity.class);
                activity.startActivity(reviewIntent);
            }
        });
        reviewButton.setEnabled(false);
        reviewButton.setClickable(false);

        /* Change passcode button */
        changePasscodeButton = (Button) myKeyView.findViewById(R.id.change_passcode_button);
        changePasscodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doorlockIntent = new Intent(activity, ChangePasscodeActivity.class);
                activity.startActivity(doorlockIntent);
            }
        });
        changePasscodeButton.setEnabled(false);
        changePasscodeButton.setClickable(false);

        /* Error Message */
        errorMessage = (TextView) myKeyView.findViewById(R.id.error_message);
        errorMessage.setVisibility(View.INVISIBLE);

        /* Loading indicator */
        loadingIndicator = (AVLoadingIndicatorView) myKeyView.findViewById(R.id.loading_indicator);

        populateView();
        if (session.getLoggedIn()) {
            sendMyHouseRequest();
        } else {
            displayNoRoom();
        }

        return myKeyView;
    }

    private void populateView() {
//        displayNoRoom();
    }

    private void sendMyHouseRequest() {
        startLoadingAnimation();
        hideErrorMessage();

        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getActivity().getApplicationContext()));

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();

        URL url = new HttpUrl.Builder()
                .scheme(SERVER_SCHEME)
                .host(SERVER_HOST)
                .addPathSegments(MY_HOUSE_URL)
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        displayNoRoom();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    showErrorMessage(getString(R.string.server_error));
                    stopLoadingAnimation();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayNoRoom();
                        }
                    });
                    throw new IOException("Response not successful   " + response);
                }
                /* Request success. */
                try {
                    /* 주의: response.body().string()은 한 번 부를 수 있음 */
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.getBoolean(KEY_QUERY_SUCCESS)) {

                        houseName = jsonObject.optString(KEY_HOUSE_NAME);
                        startDate = VillimUtil.dateFromDateString(jsonObject.optString(KEY_START_DATE));
                        endDate = VillimUtil.dateFromDateString(jsonObject.optString(KEY_END_DATE));
                        houseThumbnailUrl = jsonObject.optString(KEY_HOUSE_THUMBNAIL_URL);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                displayRoomInfo();
                            }
                        });
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                displayNoRoom();
                            }
                        });
                    }
                    stopLoadingAnimation();
                } catch (JSONException e) {
                    showErrorMessage(getString(R.string.open_room_error));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayNoRoom();
                        }
                    });
                    stopLoadingAnimation();
                }
            }
        });
    }

    private void sendOpenDoorlockRequest() {
        startLoadingAnimation();
        hideErrorMessage();

        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getActivity().getApplicationContext()));

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();


        URL url = new HttpUrl.Builder()
                .scheme(SERVER_SCHEME)
                .host(SERVER_HOST)
                .addPathSegments(OPEN_DOORLOCK_URL)
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
                    if (jsonObject.getBoolean(KEY_OPEN_AUTHORIZED)) {
                        if (jsonObject.getBoolean(KEY_OPEN_SUCESS)) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.doorlock_open_success),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            showErrorMessage(jsonObject.getString(KEY_MESSAGE));
                        }

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

    public void displayRoomInfo() {
        /* Set up slide button */
        slideButton.setText(getString(R.string.open_doorlock));
        slideButton.setThumb(ResourcesCompat.getDrawable(getResources(),
                R.drawable.slider_thumb_active, null));
        slideButton.setThumbOffset(2);
        slideButton.setOnClickListener(null);
        slideButton.setOnSlideChangeListener(null);
        slideButton.setSlideButtonListener(new SlideButton.SlideButtonListener() {
            @Override
            public void onSlide() {
                sendOpenDoorlockRequest();
            }
        });
        slideButton.setClickable(true);
        slideButton.setEnabled(true);

        /* House Name */
        houseNameTextView.setText(houseName);

        /* Dates */
        String dateString = String.format(getString(R.string.stay_duration_format),
                startDate.getYear() + 1900, startDate.getMonth() + 1, startDate.getDate(),
                endDate.getYear() + 1900, endDate.getMonth() + 1, endDate.getDate());
        validDates.setText(dateString);

        /* House thumbnail */
        Glide.with(this)
                .load(houseThumbnailUrl)
                .into(houseThumbnail);
    }

    public void displayNoRoom() {
        /* House name */
        houseNameTextView.setText(getString(R.string.no_reserved_room));

        /* House dates */
        validDates.setVisibility(View.INVISIBLE);

        /* Set up slide button */
        slideButton.setText(getString(R.string.find_room));
        slideButton.setThumb(ResourcesCompat.getDrawable(getResources(),
                R.drawable.slider_thumb_inactive, null));
        slideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Go to discovery fragment */
                stopLoadingAnimation();
                activity.selectTab(0);
            }
        });
        slideButton.setSlideButtonListener(null);
        slideButton.setClickable(true);
        slideButton.setEnabled(true);

        /* Top buttons */
        reviewButton.setEnabled(false);
        reviewButton.setClickable(false);
        reviewButton.setTextColor(getResources().getColor(R.color.text_button_inactive));

        changePasscodeButton.setEnabled(false);
        changePasscodeButton.setClickable(false);
        changePasscodeButton.setTextColor(getResources().getColor(R.color.text_button_inactive));

        /* House thumbnail */
        Glide.with(this)
                .load(R.drawable.img_default)
                .into(houseThumbnail);
    }

    public void startLoadingAnimation() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingIndicator.smoothToShow();
            }
        });
    }

    public void stopLoadingAnimation() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingIndicator.smoothToHide();
            }
        });
    }

    public void showErrorMessage(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                errorMessage.setText(message);
                errorMessage.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideErrorMessage() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                errorMessage.setVisibility(View.INVISIBLE);
            }
        });
    }

}
