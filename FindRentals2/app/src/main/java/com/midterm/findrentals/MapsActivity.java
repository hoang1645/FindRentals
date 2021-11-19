package com.midterm.findrentals;

import androidx.appcompat.widget.SearchView;
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
    private SearchView searchView;

    private ActivityMapsBinding binding;
    private List<LatLng> mLatLng;
    private List<Rental> rentals;
    private RentalRecyclerAdapter rentalRecyclerAdapter;
    public static final LatLng HCMC_LatLng = new LatLng(10.7553411,106.4150235);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        searchView = findViewById(R.id.idSearchView);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String address) {
                //rentalRecyclerAdapter.filterList(filter(address));
                return false;
            }
        });
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

    /*private ArrayList<Rental> filter(String text) {
        ArrayList<Rental> filteredList = new ArrayList<>();

        for (Rental item : rentals) {
            if (item.getAddress().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        return filteredList;
    }*/

}