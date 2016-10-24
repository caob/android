package com.ergo.poc.ui.fragment.home;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ergo.poc.R;
import com.ergo.poc.data.adapter.CentersAdapter;
import com.ergo.poc.data.api.ResultListener;
import com.ergo.poc.data.controller.CentersController;
import com.ergo.poc.data.model.Centers;
import com.ergo.poc.ui.activity.CentersDetailActivity;
import com.ergo.poc.util.CentersUtils;
import com.ergo.poc.util.Constant;
import com.ergo.poc.util.KeySaver;
import com.ergo.poc.util.LocationUpdate;
import com.ergo.poc.util.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Collections;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by mariano on 10/21/16.
 */
public class CentersFragment extends Fragment {

    @BindString(R.string.centers_progress_title)
    String progressTitle;

    @BindString(R.string.centers_progress_message)
    String progressMessage;

    @BindView(R.id.centers_recycler)
    RecyclerView recyclerView;

    @BindView(R.id.centers_map_view)
    MapView mapView;

    private CentersAdapter adapter;

    private ProgressDialog progressDialog;

    private GoogleMap map;

    private List<Centers> centersList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_centers, container, false);
        ButterKnife.bind(this, view);

        initViews(savedInstanceState);

        return view;
    }

    private void initViews(Bundle bundle) {
        adapter = new CentersAdapter();
        adapter.setOnItemClickListener(itemClickListener);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        mapView.onCreate(bundle);
        mapView.onResume();
        mapView.getMapAsync(mapReady);
        MapsInitializer.initialize(getActivity());

        ((AppCompatActivity)getActivity()).getSupportActionBar()
                .setTitle(getActivity().getResources().getString(R.string.centers_title));
    }

    private void loadCenters(boolean hasLocation) {
        if (hasLocation) {
            CentersController centersController = new CentersController();
            centersController.getCenters(new ResultListener<List<Centers>>() {
                @Override
                public void loading() {
                    progressDialog = Utils.showProgressDialog(getActivity(), progressTitle, progressMessage);
                }

                @Override
                public void finish(List<Centers> result) {
                    fetchData(result);
                }

                @Override
                public void error(Throwable error) {
                    progressDialog.cancel();
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Utils.requestLocationPermission(getActivity(), this);
        }
    }

    private OnMapReadyCallback mapReady = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            loadCenters(Utils.hasLocationPermission(getActivity()));
        }
    };

    private CentersAdapter.OnItemClickListener itemClickListener = new CentersAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, Centers centers) {
            centers.setDistanceUpdated(centers.getDistanceParsed());
            CentersUtils.getInstance().setCenters(centers);
            startActivity(new Intent(getActivity(), CentersDetailActivity.class));
        }
    };

    private void fetchData(List<Centers> result) {

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        LocationUpdate.getInstance().setLocation(locationManager
                .getLastKnownLocation(locationManager
                        .getBestProvider(new Criteria(), false)));
        progressDialog.cancel();
        adapter.setCentersList(result);
        adapter.notifyDataSetChanged();

        addMarkers(result);
    }

    private void addMarkers(List<Centers> centers) {

        Location location = LocationUpdate.getInstance().getLocation();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude()
                , location.getLongitude()), 12));

        for (Centers item : centers) {

            int markerColor = 0;

            switch (item.getType()) {
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

            map.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(markerColor))
                    .position(new LatLng(item.getLatitudeDouble(), item.getLongitudeDouble()))
                    .title(item.getName())
                    .snippet(item.getAddress()));
        }

        centersList = centers;

        map.setOnInfoWindowClickListener(mapClickListener);

        Collections.sort(centers);
        adapter.notifyDataSetChanged();
    }

    private GoogleMap.OnInfoWindowClickListener mapClickListener =  new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            for (Centers centers : centersList) {
                if (marker.getSnippet().contentEquals(centers.getAddress())) {
                    centers.setDistanceUpdated(centers.getDistanceParsed());
                    CentersUtils.getInstance().setCenters(centers);
                    startActivity(new Intent(getActivity(), CentersDetailActivity.class));
                    break;
                }
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Constant.FINE_LOCATION) {
            KeySaver.saveShare(getActivity(), Constant.LOCATION, grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED);
            loadCenters(Utils.hasLocationPermission(getActivity()));
        }

    }
}
