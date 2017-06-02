package net.villim.villim;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by seongmin on 5/26/17.
 */

public class DiscoverRecyclerAdapter extends RecyclerView.Adapter<DiscoverRecyclerAdapter.ViewHolder> {
    private VillimRoom[] houseList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView houseThumbnail;
        public TextView houseName;
        public TextView housePriceValue;
        public RatingBar houseRatingBar;
        public TextView houseRatingCount;


        public ViewHolder(View v) {
            super(v);
            houseThumbnail = (ImageView) v.findViewById(R.id.discover_room_thumbnail);
            houseName = (TextView) v.findViewById(R.id.discover_room_title);
            housePriceValue = (TextView) v.findViewById(R.id.discover_room_price_value);
            houseRatingBar = (RatingBar) v.findViewById(R.id.discover_room_review_rating);
            houseRatingCount = (TextView) v.findViewById(R.id.discover_room_review_count);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DiscoverRecyclerAdapter(VillimRoom[] dataset) {
        houseList = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DiscoverRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.discover_recycler_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        final ViewHolder vh = new ViewHolder(v);
        // Set onclick listener.
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) v.getContext();
                Intent intent = new Intent(mainActivity, RoomDetailActivity.class);
                Bundle args = new Bundle();
                args.putParcelable(mainActivity.getString(R.string.key_house), houseList[vh.getAdapterPosition()]);
                intent.putExtras(args);
                mainActivity.startActivity(intent);
            }
        });
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        populateView(holder, position);
    }

    // Make this async.
    private void populateView(ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        VillimRoom currItem = houseList[position];
        /* Room Title */
        holder.houseName.setText(currItem.houseName);
        /* Room Rate */
        int price = currItem.housePrice;
        String priceText = String.format(context.getString(R.string.room_price_value, price));
        holder.housePriceValue.setText(priceText);
        /* Room Rating Value */
        holder.houseRatingBar.setRating(currItem.houseRating);
        /* Room Rating Count */
        int count = currItem.houseReviewCount;
        String countText = String.format(context.getString(R.string.review_count_text), count);
        holder.houseRatingCount.setText(countText);
        /* Room Tumbnail */
        Glide.with(context)
                .load(R.drawable.prugio_thumbnail)
                .into(holder.houseThumbnail);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return houseList.length;
    }

    private VillimRoom getHouseAtPosition(int position) {
        return houseList[position];
    }
}