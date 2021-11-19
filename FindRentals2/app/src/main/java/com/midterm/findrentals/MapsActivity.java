package com.midterm.findrentals;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.midterm.findrentals.databinding.ActivityMapsBinding;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private List<LatLng> mLatLng;
    public static final LatLng HCMC_LatLng = new LatLng(10.7553411,106.4150235);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mLatLng = new ArrayList<>();
        mLatLng.add(new LatLng(10.7890417,106.699433));
        for (int i=0;i<mLatLng.size();i++){
            addMarkerOnMap(mLatLng.get(i));
        }
        //mMap.addMarker(new MarkerOptions().position(HCMC_LatLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(HCMC_LatLng));
    }

    private Marker addMarkerOnMap(LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(position);
        Marker marker = mMap.addMarker(markerOptions);
        return marker;
    }
}