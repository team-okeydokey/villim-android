package net.villim.villim;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;


public class ProfileFragment extends Fragment {

    private MainActivity activity;
    private String[] profileItems;
    private ListView profileListView;
    private ImageView profilePicture;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View profileView = inflater.inflate(R.layout.fragment_profile, container, false);

        activity = ((MainActivity) getActivity());
        activity.setBottomButton(false, null);

        profileListView = (ListView) profileView.findViewById(R.id.profile_listView);
        profileItems = getResources().getStringArray(R.array.profile_items);
        profileListView.setAdapter(new ArrayAdapter<String>(activity, R.layout.profile_list_item, profileItems));

        // Set title.
        activity.setTitle(getString(R.string.profile_title));

        // Add header.
        View profileListHeader = inflater.inflate(R.layout.profile_list_header, profileListView, false);
        profileListView.addHeaderView(profileListHeader);

        // Profile pic.
        profilePicture = (ImageView) profileView.findViewById(R.id.profile_picture);
        ViewCompat.setTranslationZ(profilePicture, 7);

        populateView();

        return profileView;
    }

    // Make this async.
    private void populateView() {
        // Network operation to fetch.

        // Fetch and insert room thumbnail.
        Glide.with(this)
                .load(R.drawable.prugio_thumbnail)
                .into(profilePicture);

        // Remove bottom bar.
        activity.setBottomButton(false, null);
    }

}
