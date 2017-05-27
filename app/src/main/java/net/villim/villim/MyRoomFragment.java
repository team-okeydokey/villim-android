package net.villim.villim;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.fragment_my_room, container, false);

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
		/*
		 * When this container fragment is created, we fill it with our first
		 * "real" fragment
		 */
        transaction.replace(R.id.myroom_root, new MyRoomOpenFragment());

        transaction.commit();

        return view;
    }

}
