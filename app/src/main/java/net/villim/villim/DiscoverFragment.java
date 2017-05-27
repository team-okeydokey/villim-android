package net.villim.villim;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;


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
            jsonItem.put(getString(R.string.key_thumbnail), "AAA");
            jsonItem.put(getString(R.string.key_title), "BBB");
            jsonItem.put(getString(R.string.key_review), "CCC");
            jsonItem.put(getString(R.string.key_price), "DDD");
        } catch (JSONException e) {

        }

        DiscoverListObject obj = new DiscoverListObject(jsonItem);

        DiscoverListObject[] exampleArray = {obj, obj, obj, obj, obj, obj, obj, obj, obj};

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