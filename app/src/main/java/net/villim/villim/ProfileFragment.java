package net.villim.villim;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


public class ProfileFragment extends Fragment {

    public static final int LOGIN = 0;

    private VillimSession session;

    private MainActivity activity;
    private String[] profileItems;
    private ListView profileListView;
    private TextView profileName;
    private ImageView profilePicture;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View profileView = inflater.inflate(R.layout.fragment_profile, container, false);

        // Retrieve session.
        session = new VillimSession(getActivity().getApplicationContext());

        // Remove bottom bar.
        activity = ((MainActivity) getActivity());

        /* Listview with profile items. */
        profileListView = (ListView) profileView.findViewById(R.id.profile_listView);
        profileItems = getResources().getStringArray(R.array.profile_items);
        profileListView.setAdapter(new ProfileAdapter(activity, R.layout.profile_list_item, R.id.profile_list_item_name, profileItems));
        profileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    /* Login & logout */
                    case 0:
                        if (session.getLoggedIn()) {
                            /* Log out */
                        } else {
                            /* Launch login page */
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            getActivity().startActivityForResult(intent, LOGIN);
                        }
                        break;

                    /* Profile view & edit */
                    case 1:
                        break;

                    /* FAQ */
                    case 2:
                        break;

                    /* Settings */
                    case 3:
                        break;

                    /* Privacy Policy */
                    case 4:
                        break;

                    default:
                        break;
                }
            }
        });

        // Profile name.
        profileName = (TextView) profileView.findViewById(R.id.profile_name);

        // Profile pic.
        profilePicture = (ImageView) profileView.findViewById(R.id.profile_picture);

        populateView();

        return profileView;
    }

    // Make this async.
    private void populateView() {
        // Network operation to fetch.



        // Profile pic.
        if (session.getLoggedIn()) {
            Glide.with(this)
                    .load(session.getProfilePicUrl())
                    .into(profilePicture);
        } else {
            Glide.with(this)
                    .load(R.drawable.prugio_thumbnail)
                    .into(profilePicture);
        }
    }


    // Used to populate profile info list layout.
    private class ProfileAdapter extends ArrayAdapter<String> {

        public ProfileAdapter(Context context, int resource, int textViewResourceId, String[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = super.getView(position, convertView, parent);

            /* If login/logout cell */
            if (position == 0) {
                TextView textView = (TextView) itemView.findViewById(R.id.profile_list_item_name);
                if (session.getLoggedIn()) {
                    textView.setText(getString(R.string.profile_logout));
                }
            }

            return itemView;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                /* Populate user name */
                profileName.setText(session.getName());

                /* Fetch profile image and populate view */
                Glide.with(this)
                        .load(session.getProfilePicUrl())
                        .into(profilePicture);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
