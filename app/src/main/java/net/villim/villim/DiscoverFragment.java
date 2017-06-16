package net.villim.villim;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import static net.villim.villim.VillimKeys.KEY_ADDITIONAL_GUEST_FEE;
import static net.villim.villim.VillimKeys.KEY_ADDR_DIRECTION;
import static net.villim.villim.VillimKeys.KEY_ADDR_FULL;
import static net.villim.villim.VillimKeys.KEY_ADDR_SUMMARY;
import static net.villim.villim.VillimKeys.KEY_AMENITY_IDS;
import static net.villim.villim.VillimKeys.KEY_CANCELLATION_POLICY;
import static net.villim.villim.VillimKeys.KEY_CLEANING_FEE;
import static net.villim.villim.VillimKeys.KEY_DEPOSIT;
import static net.villim.villim.VillimKeys.KEY_DESCRIPTION;
import static net.villim.villim.VillimKeys.KEY_HOST_ID;
import static net.villim.villim.VillimKeys.KEY_HOST_NAME;
import static net.villim.villim.VillimKeys.KEY_HOST_PROFILE_PIC_URL;
import static net.villim.villim.VillimKeys.KEY_HOST_RATING;
import static net.villim.villim.VillimKeys.KEY_HOST_REVIEW_COUNT;
import static net.villim.villim.VillimKeys.KEY_HOUSES;
import static net.villim.villim.VillimKeys.KEY_HOUSE_ID;
import static net.villim.villim.VillimKeys.KEY_HOUSE_NAME;
import static net.villim.villim.VillimKeys.KEY_HOUSE_PIC_URLS;
import static net.villim.villim.VillimKeys.KEY_HOUSE_POLICY;
import static net.villim.villim.VillimKeys.KEY_HOUSE_RATING;
import static net.villim.villim.VillimKeys.KEY_HOUSE_REVIEW_COUNT;
import static net.villim.villim.VillimKeys.KEY_LATITUDE;
import static net.villim.villim.VillimKeys.KEY_LOCK_ADDR;
import static net.villim.villim.VillimKeys.KEY_LOCK_PC;
import static net.villim.villim.VillimKeys.KEY_LONGITUDE;
import static net.villim.villim.VillimKeys.KEY_MESSAGE;
import static net.villim.villim.VillimKeys.KEY_NUM_BATHROOM;
import static net.villim.villim.VillimKeys.KEY_NUM_BED;
import static net.villim.villim.VillimKeys.KEY_NUM_BEDROOM;
import static net.villim.villim.VillimKeys.KEY_NUM_GUEST;
import static net.villim.villim.VillimKeys.KEY_QUERY_SUCCESS;
import static net.villim.villim.VillimKeys.KEY_RATE_PER_NIGHT;


public class DiscoverFragment extends Fragment {

    private MainActivity activity;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    private AVLoadingIndicatorView loadingIndicator;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    public static DiscoverFragment newInstance() {
        DiscoverFragment fragment = new DiscoverFragment();
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
        View discoverView = inflater.inflate(R.layout.fragment_discover, container, false);

        activity = ((MainActivity) getActivity());

        recyclerView = (RecyclerView) discoverView.findViewById(R.id.discover_recycler_view);
        layoutManager = new LinearLayoutManager(activity);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        /* Loading indicator */
        loadingIndicator = (AVLoadingIndicatorView) discoverView.findViewById(R.id.loading_indicator);

        sendFeaturedtHousesRequest();

        return discoverView;
    }

    private void sendFeaturedtHousesRequest() {
        startLoadingAnimation();

        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getActivity().getApplicationContext()));

        OkHttpClient client = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();


        URL url = new HttpUrl.Builder()
                .scheme("http")
                .host("www.mocky.io")
                .addPathSegments("v2/5944039a120000380bfcb53f")
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

    // Make this async.
    private void populateView(JSONObject jsonObject) {


        JSONObject jsonItem = new JSONObject();
        try {
            jsonItem.put(KEY_HOUSE_ID, 0);
            jsonItem.put(KEY_HOUSE_NAME, "[HongDae]1min from Hongik Univ.Stn");
            jsonItem.put(KEY_ADDR_FULL, "[HongDae]1min from Hongik Univ.Stn");
            jsonItem.put(KEY_ADDR_SUMMARY, "강남구, 서울, 한국");
            jsonItem.put(KEY_ADDR_DIRECTION, "강남구청역 4번 출구로 나와서 내리막길로 5분 걸어오세요.");
            jsonItem.put(KEY_DESCRIPTION, "- 강남역 1번 출구 바로 앞입니다\n" +
                    "- 호텔 수준의 침구류, 매 예약마다 깨끗이 세탁 (퀸 사이즈)\n" +
                    "- 신축 깨끗한 오피스텔, 좋은 전망\n" +
                    "- 요리용 주방 도구 풀세트\n" +
                    "- 무선 wifi 제공\n" +
                    "- 세탁기 & 건조기\n" +
                    "- 침실 두 개");
            jsonItem.put(KEY_NUM_GUEST, 4);
            jsonItem.put(KEY_NUM_BEDROOM, 2);
            jsonItem.put(KEY_NUM_BED, 2);
            jsonItem.put(KEY_NUM_BATHROOM, 1);
            jsonItem.put(KEY_RATE_PER_NIGHT, 102000);
            jsonItem.put(KEY_DEPOSIT, 102000);
            jsonItem.put(KEY_ADDITIONAL_GUEST_FEE, 102000);
            jsonItem.put(KEY_CLEANING_FEE, 102000);
            jsonItem.put(KEY_LOCK_ADDR, 192);
            jsonItem.put(KEY_LOCK_PC, 244110);
            jsonItem.put(KEY_LATITUDE, 37.5172);
            jsonItem.put(KEY_LONGITUDE, 127.0413);
            jsonItem.put(KEY_HOUSE_POLICY, "- 흡연금지\n" +
                    "- 반려동물 동반에 적합하지 않음\n" +
                    "- 파티나 이벤트 금지\n" +
                    "- 체크인은 15:00 이후입니다.\n\n" +
                    "- No smoking\n" +
                    "- Not too loud after 10pm\n" +
                    "- No shoes in room(Eastern Culture)");
            jsonItem.put(KEY_CANCELLATION_POLICY, "Refund Policy");
            jsonItem.put(KEY_HOST_ID, 0);
            jsonItem.put(KEY_HOST_NAME, "Kim Woobin, 김우빈");
            jsonItem.put(KEY_HOST_RATING, 4.7);
            jsonItem.put(KEY_HOST_REVIEW_COUNT, 143);
            jsonItem.put(KEY_HOST_PROFILE_PIC_URL, "http://blogs.agu.org/georneys/files/2012/12/IMG_7273-1024x682.jpg");
            jsonItem.put(KEY_HOUSE_RATING, 3.6);
            jsonItem.put(KEY_HOUSE_REVIEW_COUNT, 72);
            JSONArray amenitiesArray = new JSONArray();
            amenitiesArray.put(1);
            amenitiesArray.put(2);
            amenitiesArray.put(3);
            amenitiesArray.put(4);
            amenitiesArray.put(5);
            amenitiesArray.put(6);
            amenitiesArray.put(7);
            jsonItem.put(KEY_AMENITY_IDS, amenitiesArray);
            JSONArray housePicUrlsArray = new JSONArray();
            housePicUrlsArray.put("https://cdn.houseplans.com/product/o2d2ui14afb1sov3cnslpummre/w560x373.jpg?v=15");
            housePicUrlsArray.put("https://s-media-cache-ak0.pinimg.com/736x/7f/be/50/7fbe50ec634c65709d7fe6ac267c4e6f.jpg");
            housePicUrlsArray.put("http://hookedonhouses.net/wp-content/uploads/2009/01/Father-of-the-Bride-Lookalike-house.jpg");
            housePicUrlsArray.put("https://s-media-cache-ak0.pinimg.com/736x/86/8d/59/868d596b9cb5083257f43912989efca5.jpg");
            housePicUrlsArray.put("http://images.all-free-download.com/images/graphiclarge/green_house_icon_312519.jpg");
            jsonItem.put(KEY_HOUSE_PIC_URLS, housePicUrlsArray);
        } catch (JSONException e) {

        }


        try {
            JSONArray houseArray = jsonObject.getJSONArray(KEY_HOUSES);
            VillimHouse[] houses = VillimHouse.houseArrayFromJsonArray(houseArray);

            adapter = new DiscoverRecyclerAdapter(houses);
            recyclerView.setAdapter(adapter);
        } catch (JSONException e) {
        }

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

}