package net.villim.villim;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PolicyFragment extends Fragment {

    private MainActivity activity;

    public PolicyFragment() {
        // Required empty public constructor
    }

    public static PolicyFragment newInstance() {
        PolicyFragment fragment = new PolicyFragment();
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
        View policyView = inflater.inflate(R.layout.fragment_policy, container, false);

        // Set bottom button text.
        activity = ((MainActivity) getActivity());
//        activity.showBottomButtons(false, false);

        return policyView;
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
