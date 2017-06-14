package net.villim.villim;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import static net.villim.villim.VillimKeys.KEY_HOUSE_ID;
import static net.villim.villim.VillimKeys.KEY_HOUSE_NAME;
import static net.villim.villim.VillimKeys.KEY_HOUSE_POLICY;
import static net.villim.villim.VillimKeys.KEY_HOUSE_RATING;
import static net.villim.villim.VillimKeys.KEY_HOUSE_REVIEW_COUNT;
import static net.villim.villim.VillimKeys.KEY_LATITUDE;
import static net.villim.villim.VillimKeys.KEY_LOCK_ADDR;
import static net.villim.villim.VillimKeys.KEY_LOCK_PC;
import static net.villim.villim.VillimKeys.KEY_LONGITUDE;
import static net.villim.villim.VillimKeys.KEY_NUM_BATHROOM;
import static net.villim.villim.VillimKeys.KEY_NUM_BED;
import static net.villim.villim.VillimKeys.KEY_NUM_BEDROOM;
import static net.villim.villim.VillimKeys.KEY_NUM_GUEST;
import static net.villim.villim.VillimKeys.KEY_RATE_PER_NIGHT;


public class DiscoverFragment extends Fragment {

    private MainActivity activity;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

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


        populateView();

        return discoverView;
    }

    // Make this async.
    private void populateView() {
        // Network operation to fetch.


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
        } catch (JSONException e) {

        }

        VillimHouse obj = new VillimHouse(jsonItem);

        VillimHouse[] exampleArray = {obj, obj, obj, obj, obj, obj, obj, obj, obj};

        adapter = new DiscoverRecyclerAdapter(exampleArray);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}