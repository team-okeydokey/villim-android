package net.villim.villim;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


public class MyKeyFragment extends Fragment {

    private MainActivity activity;

    private RelativeLayout container;

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
        View view = inflater.inflate(R.layout.fragment_my_key, container, false);
        activity = ((MainActivity) getActivity());
        container = (RelativeLayout) view.findViewById(R.id.container);
        container.setFitsSystemWindows(true);
        return view;
    }

}
