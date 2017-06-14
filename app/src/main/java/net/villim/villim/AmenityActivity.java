package net.villim.villim;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import static net.villim.villim.VillimKeys.KEY_AMENITY_IDS;

public class AmenityActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView amenityListView;

    private int[] amenityIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amenity);

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* ListView */
        amenityIds = getIntent().getIntArrayExtra(KEY_AMENITY_IDS);
        amenityListView = (ListView) findViewById(R.id.amenity_listview);

    }
}
