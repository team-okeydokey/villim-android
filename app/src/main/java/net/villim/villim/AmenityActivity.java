package net.villim.villim;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static net.villim.villim.VillimKeys.KEY_AMENITY_IDS;

public class AmenityActivity extends VillimActivity {

    private Toolbar toolbar;
    private ListView amenityListView;
    private Button closeButton;

    private int[] amenityIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amenity);

        amenityIds = getIntent().getIntArrayExtra(KEY_AMENITY_IDS);

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Back button */
        closeButton = (Button) findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /* ListView */
        amenityIds = getIntent().getIntArrayExtra(KEY_AMENITY_IDS);
        amenityListView = (ListView) findViewById(R.id.amenity_listview);
        ArrayList<Integer> amenityIdArrayList = new ArrayList<>();
        for (int index = 0; index < amenityIds.length; index++) {
            amenityIdArrayList.add(amenityIds[index]);
        }
        final ArrayAdapter<Integer> adapter = new AmenityAdapter(this,
                R.layout.amenity_list_item,
                R.id.amenity_list_item_name,
                amenityIdArrayList);

        amenityListView.setAdapter(adapter);

    }

    public class AmenityAdapter extends ArrayAdapter<Integer> {
        public AmenityAdapter(Context context, int resource, int textViewResourceId, ArrayList<Integer> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = super.getView(position, convertView, parent);

            TextView textView = (TextView) itemView.findViewById(R.id.amenity_list_item_name);

            /* Set name */
            textView.setText(VillimAmenity.getAmenityName(getApplicationContext(), amenityIds[position]));

            /* Add drawableLeft */
            Drawable itemIcon =  VillimAmenity.getAmenityDrawable(getApplicationContext(), amenityIds[position]);
            int iconSize = getResources().getDimensionPixelSize(R.dimen.amenity_list_drawable_size);
            itemIcon.setBounds(0, 0, iconSize, iconSize);
            textView.setCompoundDrawables(itemIcon, null, null, null);

            return itemView;
        }
    }
}
