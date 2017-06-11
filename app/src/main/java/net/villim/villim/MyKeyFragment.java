package net.villim.villim;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rojoxpress.slidebutton.SlideButton;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static net.villim.villim.VillimKeys.KEY_ID;
import static net.villim.villim.VillimKeys.KEY_RESERVATION_ACTIVE;


public class MyKeyFragment extends Fragment {
    private static final String MY_KEY_URL = "http://www.mocky.io/v2/593dccba1100004722722b03";

    private MainActivity activity;

    private ImageView houseThumbnail;
    private TextView houseName;
    private TextView validDates;
    private TextView errorMessage;
    private SlideButton slideButton;

    private AVLoadingIndicatorView loadingIndicator;

    private VillimSession session;

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
        houseName = (TextView) myKeyView.findViewById(R.id.house_name);
        validDates = (TextView) myKeyView.findViewById(R.id.valid_dates);

        /* House thumbnail */
        houseThumbnail = (ImageView) myKeyView.findViewById(R.id.house_thumbnail);

        /* Slide Button */
        slideButton = (SlideButton) myKeyView.findViewById(R.id.slide_button);

        /* Error Message */
        errorMessage = (TextView) myKeyView.findViewById(R.id.error_message);
        errorMessage.setVisibility(View.INVISIBLE);

        /* Loading indicator */
        loadingIndicator = (AVLoadingIndicatorView) myKeyView.findViewById(R.id.loading_indicator);

        populateView();
        sendRequest();

        return myKeyView;
    }

    private void populateView() {
        displayNoRoom();
    }

    private void sendRequest() {
        startLoadingAnimation();
        hideErrorMessage();

        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder()
                .add(KEY_ID, Integer.toString(session.getId()))
                .build();

        Request request = new Request.Builder()
                .url(MY_KEY_URL)
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
                    showErrorMessage(getString(R.string.server_error));
                    stopLoadingAnimation();
                    throw new IOException("Response not successful   " + response);
                }
                /* Request success. */
                try {
                    /* 주의: response.body().string()은 한 번 부를 수 있음 */
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.getBoolean(KEY_RESERVATION_ACTIVE)) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                displayRoomInfo();
                            }
                        });
                    } else {
                        System.out.println("ddddd");
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
                    stopLoadingAnimation();
                }
            }
        });
    }

    public void displayRoomInfo() {

        /* Set up slide button */
        slideButton.setText(getString(R.string.find_room));
        slideButton.setThumb(ResourcesCompat.getDrawable(getResources(),
                R.drawable.slider_thumb_active, null));

    }

    public void displayNoRoom() {
        System.out.println("ddddd");
        /* House name */
        houseName.setText(getString(R.string.no_reserved_room));

        /* House dates */
        validDates.setVisibility(View.INVISIBLE);

        /* Set up slide button */
        slideButton.setText(getString(R.string.find_room));
        slideButton.setThumb(ResourcesCompat.getDrawable(getResources(),
                R.drawable.slider_thumb_inactive, null));

        /* House thumbnail */
        Glide.with(this)
                .load(R.drawable.prugio_thumbnail)
                .into(houseThumbnail);
        System.out.println("ddddd");
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
