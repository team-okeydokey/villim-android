package net.villim.villim;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class VisitFragment extends Fragment {

    private MainActivity activity;
    private TextView roomName;
    private TextView visitDate;
    private ImageView roomThumbnail;

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

        // Remove bottom bar.
        activity = ((MainActivity) getActivity());
//        activity.showBottomButtons(false, false);

        populateView();

        return visitView;
    }

    // Make this async.
    private void populateView() {
        // Network operation to fetch.
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
