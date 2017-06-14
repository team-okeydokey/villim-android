package net.villim.villim;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;


public class ProfileFragment extends Fragment {

    public static final int LOGIN = 100;
    public static final int SIGNUP = 101;

    private VillimSession session;

    private MainActivity activity;
    private ArrayList<String> profileItems;
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
        activity = ((MainActivity) getActivity());

        // Retrieve session.
        session = new VillimSession(getActivity().getApplicationContext());

        /* Listview with profile items. */
        profileListView = (ListView) profileView.findViewById(R.id.profile_listView);
        int arrayId = session.getLoggedIn() ? R.array.profile_items_logged_in : R.array.profile_items_logged_out;
        String[] itemArray = getResources().getStringArray(arrayId);
        ArrayList<String> profileItems = new ArrayList<>(Arrays.asList(itemArray));
        final ArrayAdapter<String> adapter = new ProfileAdapter(activity, R.layout.profile_list_item, R.id.profile_list_item_name, profileItems);

        profileListView.setAdapter(adapter);
        profileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    /* Login & logout */
                    case 0:
                        if (session.getLoggedIn()) {
                            /* Log out */
                            logout();
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
        String titleString = session.getLoggedIn() ? session.getFullName() : getString(R.string.profile_title);
        profileName = (TextView) profileView.findViewById(R.id.profile_name);
        profileName.setText(titleString);

        // Profile pic.
        int profilePicVisibility = session.getLoggedIn() ? View.VISIBLE : View.INVISIBLE;
        profilePicture = (ImageView) profileView.findViewById(R.id.profile_picture);
        profilePicture.setVisibility(profilePicVisibility);

        populateView();

        return profileView;
    }

    // Make this async.
    private void populateView() {
        // Network operation to fetch.

        // Profile pic.
        if (session.getLoggedIn()) {

            if (session.getProfilePicUrl().isEmpty()) {
                Glide.with(this)
                        .load(R.drawable.prugio_thumbnail)
                        .into(profilePicture);
            } else {
                Glide.with(this)
                        .load(session.getProfilePicUrl())
                        .into(profilePicture);
            }
        }
    }

    private void logout() {
        session.setLoggedIn(false);

        /* Update listview items */
        String[] itemArray = getResources().getStringArray(R.array.profile_items_logged_out);
        profileItems = new ArrayList<>(Arrays.asList(itemArray));
        ProfileAdapter adapter = (ProfileAdapter) profileListView.getAdapter();
        adapter.clear();
        adapter.addAll(profileItems);
        adapter.notifyDataSetChanged();

        /* Remove name */
        profileName.setText(getString(R.string.profile_title));

        /* Remove profile picture. */
        profilePicture.setVisibility(View.INVISIBLE);
    }

    private void login() {
        session.setLoggedIn(true);

        /* Change list items to include profile view and edit */
        String[] itemArray = getResources().getStringArray(R.array.profile_items_logged_in);
        profileItems = new ArrayList<>(Arrays.asList(itemArray));
        ProfileAdapter adapter = (ProfileAdapter) profileListView.getAdapter();
        adapter.clear();
        adapter.addAll(profileItems);
        adapter.notifyDataSetChanged();

        /* Populate user name */
        profileName.setText(session.getFullName());

        /* Fetch profile image and populate view */
        if (session.getProfilePicUrl().isEmpty()) {
            Glide.with(this)
                    .load(R.drawable.img_default)
                    .into(profilePicture);
        } else {
            Glide.with(this)
                    .load(session.getProfilePicUrl())
                    .into(profilePicture);
        }
        profilePicture.setVisibility(View.VISIBLE);
    }


    // Used to populate profile info list layout.
    private class ProfileAdapter extends ArrayAdapter<String> {

        public ProfileAdapter(Context context, int resource, int textViewResourceId, ArrayList<String> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public int getCount() {
            return super.getCount();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = super.getView(position, convertView, parent);

            TextView textView = (TextView) itemView.findViewById(R.id.profile_list_item_name);

            /* If login/logout cell */
            if (position == 0) {
                if (session.getLoggedIn()) {
                    textView.setText(getString(R.string.profile_logout));
                }
            }

            /* Set drawable */
            int iconResource;
            boolean loggedIn = session.getLoggedIn();
            switch (position) {
                case 0:
                    iconResource = R.drawable.icon_login;
                    break;
                case 1:
                    iconResource = loggedIn ? R.drawable.icon_profile : R.drawable.icon_faq;
                    break;
                case 2:
                    iconResource = loggedIn ? R.drawable.icon_faq : R.drawable.icon_setting;
                    break;
                case 3:
                    iconResource = loggedIn ? R.drawable.icon_setting : R.drawable.icon_shield;
                    break;
                case 4:
                    iconResource = loggedIn ? R.drawable.icon_shield : R.drawable.icon_shield;
                    break;
                default:
                    iconResource = loggedIn ? R.drawable.icon_shield : R.drawable.icon_shield;
                    break;
            }
            Drawable itemIcon =  getResources().getDrawable(iconResource);
            int iconSize = getResources().getDimensionPixelSize(R.dimen.profile_list_drawable_size);
            itemIcon.setBounds(0, 0, iconSize, iconSize);
            textView.setCompoundDrawables(itemIcon, null, null, null);

            return itemView;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                login();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }


}
