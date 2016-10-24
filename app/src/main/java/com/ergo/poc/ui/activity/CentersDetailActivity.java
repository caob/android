package com.ergo.poc.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.ergo.poc.R;
import com.ergo.poc.data.model.Centers;
import com.ergo.poc.util.CentersUtils;
import com.ergo.poc.util.Constant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by mariano on 10/24/16.
 */
public class CentersDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.centers_detail_name)
    TextView name;

    @BindView(R.id.centers_detail_distance)
    TextView distance;

    @BindView(R.id.centers_detail_address)
    TextView address;

    @BindView(R.id.centers_detail_attention)
    TextView attention;

    @BindView(R.id.centers_detail_map)
    MapView mapView;

    private Centers centers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centers_detail);
        ButterKnife.bind(this);

        centers = CentersUtils.getInstance().getCenters();

        initToolbar();
        initViews(savedInstanceState);
    }

    private void initToolbar() {
        toolbar.setTitle(centers.getType());
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViews(Bundle bundle) {
        name.setText(centers.getName());
        distance.setText(String.format(getResources().getString(R.string.centers_detail_distance), centers.getDistanceParsed()));
        address.setText(String.format(getResources().getString(R.string.centers_detail_address), centers.getFullAddress()));
        attention.setText(String.format(getResources().getString(R.string.centers_detail_attention), centers.getAttention()));

        mapView.onCreate(bundle);
        mapView.onResume();
        mapView.getMapAsync(mapReady);
        MapsInitializer.initialize(this);
    }

    private OnMapReadyCallback mapReady = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(centers.getLatitudeDouble()
                    , centers.getLongitudeDouble()), 15));

            int markerColor = 0;

            switch (centers.getType()) {
                case Constant.CENTERS_OWNER:
                    markerColor = R.drawable.map_owner;
                    break;
                case Constant.CENTERS_RETAIL:
                    markerColor = R.drawable.map_retail;
                    break;
                case Constant.CENTERS_DEALER:
                    markerColor = R.drawable.map_dealer;
                    break;
            }

            googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(markerColor))
                    .position(new LatLng(centers.getLatitudeDouble(), centers.getLongitudeDouble()))
                    .title(centers.getName())
                    .snippet(centers.getAddress())).showInfoWindow();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
