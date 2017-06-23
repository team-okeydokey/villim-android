package net.villim.villim;


import android.app.Activity;
import android.app.DialogFragment;
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

import static net.villim.villim.VillimKeys.FAQ_URL;
import static net.villim.villim.VillimKeys.TERMS_OF_SERVICE_URL;
import static net.villim.villim.WebViewActivity.TITLE;
import static net.villim.villim.WebViewActivity.URL;


public class ProfileFragment extends Fragment implements LogoutDialog.LogoutDialogListener {

    public static final int LOGIN = 100;
    public static final int SIGNUP = 101;
    public static final int PROFILE_EDIT = 102;

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

                String title = (String) parent.getItemAtPosition(position);

                if (title.equals(getString(R.string.login))) {
                    /* Launch login page */
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivityForResult(intent, LOGIN);
                } else if (title.equals(getString(R.string.logout))) {
                     /* Log out */
                    LogoutDialog dialog = LogoutDialog.newInstance(
                            ProfileFragment.this, session.getFullName());
                    dialog.show(getActivity().getFragmentManager(), "LogoutFragment");

                } else if (title.equals(getString(R.string.profile_title))) {
                    launchProfileEditActivity();
                } else if (title.equals(getString(R.string.faq))) {
                    launchFAQWebview();
                } else if (title.equals(getString(R.string.settings))) {
                    launchSettingsActivity();
                } else if (title.equals(getString(R.string.privacy_policy))) {
                    launchPrivacyPolicyWebview();
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

    private void launchSettingsActivity() {
        Intent intent = new Intent(getActivity(), SettingsActivity.class);
        getActivity().startActivity(intent);
    }

    private void launchPrivacyPolicyWebview() {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra(URL, TERMS_OF_SERVICE_URL);
        intent.putExtra(TITLE, getString(R.string.privacy_policy));
        getActivity().startActivity(intent);
    }

    private void launchFAQWebview() {
        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        intent.putExtra(URL, FAQ_URL);
        intent.putExtra(TITLE, getString(R.string.faq));
        getActivity().startActivity(intent);
    }

    private void launchProfileEditActivity() {
        Intent intent = new Intent(getActivity(), ProfileViewActivity.class);
        getActivity().startActivity(intent);
    }

    // Make this async.
    private void populateView() {
        // Network operation to fetch.

         /* Profile Name */
        String titleString = session.getLoggedIn() ? session.getFullName() : getString(R.string.profile);
        profileName.setText(titleString);

        /* Profile Pic */
        int profilePicVisibility = session.getLoggedIn() ? View.VISIBLE : View.INVISIBLE;
        profilePicture.setVisibility(profilePicVisibility);

        if (session.getLoggedIn()) {

            if (session.getProfilePicUrl().isEmpty()) {
                Glide.with(this)
                        .load(R.drawable.img_default)
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
        profileName.setText(getString(R.string.profile));

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
                    textView.setText(getString(R.string.logout));
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
            Drawable itemIcon = getResources().getDrawable(iconResource);
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
        } else if (requestCode == PROFILE_EDIT) {
            if (resultCode == Activity.RESULT_OK) {
                populateView();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    /* Logout dialog */
    public void onDialogPositiveClick(DialogFragment dialog) {
        dialog.dismiss();
        logout();
    }

    public void onDialogNegativeClick(DialogFragment dialog) {

    }

}
