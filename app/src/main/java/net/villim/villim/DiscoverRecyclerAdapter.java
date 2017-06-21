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

import java.text.NumberFormat;

import static net.villim.villim.CalendarActivity.END_DATE;
import static net.villim.villim.CalendarActivity.START_DATE;
import static net.villim.villim.MainActivity.DATE_SELECTED;

/**
 * Created by seongmin on 5/26/17.
 */

public class DiscoverRecyclerAdapter extends RecyclerView.Adapter<DiscoverRecyclerAdapter.ViewHolder> {
    private VillimHouse[] houseList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView houseThumbnail;
        public TextView houseName;
        public TextView currencySymbol;
        public TextView houseRate;
        public RatingBar houseRatingBar;
        public TextView houseRatingCount;


        public ViewHolder(View v) {
            super(v);
            houseThumbnail = (ImageView) v.findViewById(R.id.discover_room_thumbnail);
            houseName = (TextView) v.findViewById(R.id.discover_room_title);
            currencySymbol = (TextView) v.findViewById(R.id.discover_room_currency_symbol);
            houseRate = (TextView) v.findViewById(R.id.discover_room_price_value);
            houseRatingBar = (RatingBar) v.findViewById(R.id.discover_room_review_rating);
            houseRatingCount = (TextView) v.findViewById(R.id.discover_room_review_count);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DiscoverRecyclerAdapter(VillimHouse[] dataset) {
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
                Intent intent = new Intent(mainActivity, HouseDetailActivity.class);
                Bundle args = new Bundle();
                args.putParcelable(mainActivity.getString(R.string.key_house), houseList[vh.getAdapterPosition()]);
                args.putBoolean(DATE_SELECTED, mainActivity.getDateSelected());
                if (mainActivity.getDateSelected()) {
                    args.putSerializable(START_DATE, mainActivity.getStartDate());
                    args.putSerializable(END_DATE, mainActivity.getEndDate());
                }
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
        VillimHouse currItem = houseList[position];
        /* Room Title */
        holder.houseName.setText(currItem.houseName);
        /* Currency symbol */
        VillimSession session = new VillimSession(context);
        holder.currencySymbol.setText(
                VillimUtil.currencyStringFromInt(context, session.getCurrencyPref(), false));
        /* Room Rate */
        int price = currItem.ratePerNight;
        String priceString = NumberFormat.getIntegerInstance().format(price);
        String priceText = String.format(context.getString(R.string.room_price_value, priceString));
        holder.houseRate.setText(priceText);
        /* Room Rating Value */
        holder.houseRatingBar.setRating(currItem.houseRating);
        /* Room Rating Count */
        int count = currItem.houseReviewCount;
        String countText = String.format(context.getString(R.string.review_count_text), count);
        holder.houseRatingCount.setText(countText);
        /* Room Tumbnail */
        Glide.with(context)
                .load(currItem.houseThumbnailUrl)
                .into(holder.houseThumbnail);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return houseList.length;
    }

    private VillimHouse getHouseAtPosition(int position) {
        return houseList[position];
    }
}