package net.villim.villim;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
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

import static net.villim.villim.VillimKeys.KEY_CONFIRMED_VISITS;
import static net.villim.villim.VillimKeys.KEY_PREFERENCE_CURRENCY;
import static net.villim.villim.VillimKeys.KEY_MESSAGE;
import static net.villim.villim.VillimKeys.KEY_QUERY_SUCCESS;
import static net.villim.villim.VillimKeys.SERVER_HOST;
import static net.villim.villim.VillimKeys.SERVER_SCHEME;
import static net.villim.villim.VillimKeys.VISIT_LIST_URL;


public class VisitFragment extends Fragment {

    public static final int VISIT_DETAIL = 200;

    private MainActivity activity;

    private Toolbar toolbar;
    private RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private AVLoadingIndicatorView loadingIndicator;

    private VillimSession session;

    public VisitFragment() {
        // Required empty public constructor
    }

    public static VisitFragment newInstance() {
        VisitFragment fragment = new VisitFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View visitView = inflater.inflate(R.layout.fragment_visit, container, false);

        activity = ((MainActivity) getActivity());
//        activity.showBottomButtons(false, false);

        session = new VillimSession(getActivity().getApplicationContext());

        /* Toolbar */
        toolbar = (Toolbar) visitView.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(false);
        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        relativeLayout = (RelativeLayout) visitView.findViewById(R.id.root_view);
        recyclerView = (RecyclerView) visitView.findViewById(R.id.discover_recycler_view);
        layoutManager = new LinearLayoutManager(activity);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        /* Loading indicator */
        loadingIndicator = (AVLoadingIndicatorView) visitView.findViewById(R.id.loading_indicator);

        if (session.getLoggedIn()) {
            sendVisitListRequest();
        } else {
            showNoVisitScreen();
        }

        return visitView;
    }

    // Make this async.
    private void populateView(JSONObject jsonObject) {
        // Network operation to fetch.
        try {
            JSONArray visitArray = jsonObject.getJSONArray(KEY_CONFIRMED_VISITS);
            VillimVisit[] visits = VillimVisit.visitArrayFromJsonArray(visitArray);
            VillimHouse[] houses = VillimHouse.houseArrayFromJsonArray(getActivity().getApplicationContext(), visitArray);

            if (visits.length == 0) {
                showNoVisitScreen();
            } else {
                adapter = new VisitRecyclerAdapter(visits, houses);
                recyclerView.setAdapter(adapter);
            }

        } catch (JSONException e) {
        }
    }

    public void showNoVisitScreen() {
        relativeLayout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_no_visit, null, false);

        /* Set default thumbnail */
        ImageView houseThumbnail = (ImageView) layout.findViewById(R.id.house_thumbnail);
        Glide.with(this).load(R.drawable.img_default).into(houseThumbnail);

        /* Set no visit text */
        TextView noVisitTextView = (TextView) layout.findViewById(R.id.house_name);
        noVisitTextView.setText(getString(R.string.no_visit_list));

        /* Set OnclikcListener to button */
        Button bottomButton = (Button) layout.findViewById(R.id.bottom_button);
        bottomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.selectTab(0);
            }
        });

        relativeLayout.addView(layout);
    }

    private void sendVisitListRequest() {
        startLoadingAnimation();

        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getActivity().getApplicationContext()));

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();


        URL url = new HttpUrl.Builder()
                .scheme(SERVER_SCHEME)
                .host(SERVER_HOST)
                .addPathSegments(VISIT_LIST_URL)
                .addQueryParameter(KEY_PREFERENCE_CURRENCY, Integer.toString(session.getCurrencyPref()))
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
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.getBoolean(KEY_QUERY_SUCCESS)) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateView(jsonObject);
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
                Toast.makeText(getActivity(), message,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == VISIT_DETAIL) {
            sendVisitListRequest();
        }
    }

}
