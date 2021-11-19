package com.midterm.findrentals;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.midterm.findrentals.databinding.ActivityMapsBinding;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private SearchView searchView;

    private ActivityMapsBinding binding;
    private List<Rental> mRentals;
    public static final LatLng HCMC_LatLng = new LatLng(10.7553411,106.4150235);

    private static final float RED_CODE = BitmapDescriptorFactory.HUE_RED;
    private static final float GREEN_CODE = BitmapDescriptorFactory.HUE_GREEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        searchView = findViewById(R.id.idSearchView);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String address) {
                LatLng latTndQuery = getLocationFromAddress(address + "Ho Chi Minh City");
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latTndQuery));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String address) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mRentals = new ArrayList<>();
        mRentals.add(new Rental (1, "Bến Thành, Quận 1",
                999999999, 4, 1, 1, 10.778189,106.694152));
        mRentals.add(new Rental (2, "Số 4 Phạm Ngọc Thạch, Bến Nghé, Quận 1",
                999999999, 4, 2, 2, 10.7800669,106.6944312));

        addRentalsOnMap();

        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(HCMC_LatLng));
    }

    private void addRentalsOnMap() {
        for (int i=0;i<mRentals.size();i++){
            addMarkerOnMap(mRentals.get(i).getLatitude(), mRentals.get(i).getLongitude(), RED_CODE);
        }
    }

    private Marker addMarkerOnMap(double lat, double lng, float colorCode) {
        LatLng position = new LatLng(lat, lng);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.defaultMarker(colorCode));
        Marker marker = mMap.addMarker(markerOptions);
        return marker;
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {

        addMarkerOnMap(latLng.latitude, latLng.longitude, GREEN_CODE);

    }

    private ArrayList<Rental> filter(String text) {
        ArrayList<Rental> filteredList = new ArrayList<>();

        for (Rental item : mRentals) {
            if (item.getAddress().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        return filteredList;
    }

    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;
        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return p1;
    }

}