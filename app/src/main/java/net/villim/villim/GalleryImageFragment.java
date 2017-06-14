package net.villim.villim;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GalleryImageFragment extends Fragment {

    public static final String PICTURE_URL = "picture_url";
    
    public GalleryImageFragment() {
        // Required empty public constructor
    }

    public static final GalleryImageFragment newInstance(String message)
    {
        GalleryImageFragment fragment = new GalleryImageFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(PICTURE_URL, message);
        fragment.setArguments(bdl);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery_image, container, false);
        String pictureUrl = getArguments().getString(PICTURE_URL);

        ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
        Glide.with(this)
                .load(pictureUrl)
                .into(imageView);
        return view;
    }


}
