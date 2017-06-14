package net.villim.villim;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static net.villim.villim.VillimKeys.KEY_ADDR_DIRECTION;
import static net.villim.villim.VillimKeys.KEY_LATITUDE;
import static net.villim.villim.VillimKeys.KEY_LONGITUDE;

public class FullScreenMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Toolbar toolbar;
    private MapFragment mapFragment;
    private Button closeButton;
    private TextView directionText;

    /* 변수로 저장 안 하고 그냥 토글 해도 되지만, 확장성을 위해 변수 지정함. */
    private boolean menuVisible;

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_map);

        menuVisible = true;

        /* Toolbar */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /* Map */
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        /* Coordinates */
        latitude = getIntent().getDoubleExtra(KEY_LATITUDE, 0);
        longitude = getIntent().getDoubleExtra(KEY_LONGITUDE, 0);

        /* Back button */
        closeButton = (Button) findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /* 오시는 길 */
        directionText = (TextView) findViewById(R.id.direction_text);
        String directionString = String.format(getString(R.string.direction_format), getIntent().getStringExtra(KEY_ADDR_DIRECTION));
        directionText.setText(directionString);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 17.0f));

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                toggleBars();
            }
        });
    }

    private void toggleBars() {
        if (menuVisible) {
            toolbar.setVisibility(View.GONE);
            directionText.setVisibility(View.GONE);
            menuVisible = false;
        } else {
            toolbar.setVisibility(View.VISIBLE);
            directionText.setVisibility(View.VISIBLE);
            menuVisible = true;
        }
    }
}
