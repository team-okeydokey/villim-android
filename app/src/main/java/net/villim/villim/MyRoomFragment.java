package net.villim.villim;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class MyRoomFragment extends Fragment {

    private MainActivity activity;
    private TextView roomName;
    private TextView keyExpirationDate;
    private ImageView roomThumbnail;
    private Button changePasscodeButton;

    public MyRoomFragment() {
        // Required empty public constructor
    }

    public static MyRoomFragment newInstance() {
        MyRoomFragment fragment = new MyRoomFragment();
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
        View myRoomView = inflater.inflate(R.layout.fragment_my_room, container, false);

        activity = ((MainActivity) getActivity());
        activity.registerBottomButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        // Set title.
        activity.setTitle(getString(R.string.myroom_title));

        // Room name.
        roomName = (TextView) myRoomView.findViewById(R.id.room_name);

        // Room duration.
        roomThumbnail = (ImageView) myRoomView.findViewById(R.id.room_thumbnail);
        keyExpirationDate = (TextView) myRoomView.findViewById(R.id.key_expiration_date);

        // Change passcode button.
        changePasscodeButton = (Button) myRoomView.findViewById(R.id.change_passcode_button);
        changePasscodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.main_frame, new PasscodeFragment())
                        .addToBackStack(null).commitAllowingStateLoss();
            }
        });

        populateView();

        return myRoomView;
    }

    // Make this async.
    private void populateView() {
        // Network operation to fetch.

        // Populate room name.
        roomName.setText("프루지오 스튜디오");

        // Populate room duration.
        keyExpirationDate.setText("2017-08-12");

        // Fetch and insert room thumbnail.
        Glide.with(this)
                .load(R.drawable.prugio_thumbnail)
                .into(roomThumbnail);

        // Set bottom button text.
        String bottomButtonText = getActivity().getString(R.string.open_room);
        activity.setBottomButton(true, bottomButtonText);
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
