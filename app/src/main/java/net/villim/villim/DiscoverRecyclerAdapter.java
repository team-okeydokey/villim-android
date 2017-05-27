package net.villim.villim;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

/**
 * Created by seongmin on 5/26/17.
 */

public class DiscoverRecyclerAdapter extends RecyclerView.Adapter<DiscoverRecyclerAdapter.ViewHolder> {
    private DiscoverListObject[] roomList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView roomThumbnail;
        public TextView roomTitle;

        public ViewHolder(View v) {
            super(v);
            roomThumbnail = (ImageView) v.findViewById(R.id.discover_room_thumbnail);
            roomTitle = (TextView) v.findViewById(R.id.discover_room_title);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public DiscoverRecyclerAdapter(DiscoverListObject[] dataset) {
        roomList = dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DiscoverRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.discover_recycler_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Context context = holder.itemView.getContext();
        holder.roomTitle.setText(roomList[position].data.get(context.getString(R.string.key_title)));
        Glide.with(context)
                .load(R.drawable.prugio_thumbnail)
                .into(holder.roomThumbnail);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return roomList.length;
    }
}