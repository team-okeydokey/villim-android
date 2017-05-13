package net.villim.villim;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class MyRoomFragment extends Fragment {

    private TextView myroomTitle;
    private TextView myroomRoomName;

    private TextView myroomNumGuest;
    private TextView myroomRoomType;
    private TextView myroomNumBed;
    private TextView myroomNumBath;

    private TextView myroomRoomDuration;
    private ImageView myroomThumbnail;

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

        myroomTitle = (TextView) myRoomView.findViewById(R.id.myroom_title);

        // Room name.
        myroomRoomName = (TextView) myRoomView.findViewById(R.id.myroom_roomname);

        // Room info.
        myroomNumGuest = (TextView) myRoomView.findViewById(R.id.myroom_numguest);
        myroomRoomType = (TextView) myRoomView.findViewById(R.id.myroom_roomtype);
        myroomNumBed = (TextView) myRoomView.findViewById(R.id.myroom_numbed);
        myroomNumBath = (TextView) myRoomView.findViewById(R.id.myroom_numbath);

        // Room duration.
        myroomRoomDuration = (TextView) myRoomView.findViewById(R.id.myroom_roomduration);

        myroomThumbnail = (ImageView) myRoomView.findViewById(R.id.myroom_thumbnail);

        populateView();

        return myRoomView;
    }

    // Make this async.
    private void populateView() {
        // Network operation to fetch.

        // Populate room name.
        myroomRoomName.setText("Prugio Studio");

        // Populate room information.
        myroomNumGuest.setText("Guest 1");
        myroomRoomType.setText("Studio");
        myroomNumBed.setText("Bed 1");
        myroomNumBath.setText("Bath 1");

        // Populate room duration.
        myroomRoomDuration.setText("Duration of stay: 2017/04/16 ~ 06/19");

        // Fetch and insert room thumbnail.
        Glide.with(this)
                .load(R.drawable.prugio_thumbnail)
                .into(myroomThumbnail);
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
