package com.midterm.findrentals;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.midterm.findrentals.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    private SearchView searchView;
    private View textViewOptions;
    private MapDirectionHelper mapDirectionHelper;

    private LatLng startLatLng, destLatLng;
    private Location lastLocation;

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
        textViewOptions = (TextView) findViewById(R.id.textviewOptions);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String address) {
                LatLng latLngQuery = getLocationFromAddress(address + "Ho Chi Minh City");
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngQuery));
                addMarkerOnMap(latLngQuery.latitude, latLngQuery.longitude, GREEN_CODE, -1);
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
        mRentals.add(new Rental ("Bến Thành, Quận 1",
                999999999, 4, 1, 1, 10.778189,106.694152));
        mRentals.add(new Rental ("Số 4 Phạm Ngọc Thạch, Bến Nghé, Quận 1",
                999999999, 4, 2, 2, 10.7800669,106.6944312));

        addRentalsOnMap();

        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLongClickListener(this);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(HCMC_LatLng));
        mapDirectionHelper = new MapDirectionHelper(mMap, this);
    }

    private void addRentalsOnMap() {
        for (int i=0;i<mRentals.size();i++){
            addMarkerOnMap(mRentals.get(i).getLatitude(), mRentals.get(i).getLongitude(), RED_CODE, i);
        }
    }

    private Marker addMarkerOnMap(double lat, double lng, float colorCode, int id) {
        LatLng position = new LatLng(lat, lng);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.defaultMarker(colorCode));
        Marker marker = mMap.addMarker(markerOptions);
        marker.setTag(id);
        return marker;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_marker_context, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_detail:
                /*Intent intent = new Intent(MainActivity.this,
                        OrderActivity.class);
                intent.putExtra(EXTRA_MESSAGE, mOrderMessage);
                startActivity(intent);*/
                displayToast("view detail");
                return true;
            case R.id.find_route:
                displayToast("find route");
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Context context = this;
        LatLng latLng = marker.getPosition();
        PopupMenu popup = new PopupMenu(MapsActivity.this, textViewOptions);
        popup.getMenuInflater().inflate(R.menu.menu_marker_context, popup.getMenu());
        popup.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.view_detail:
                                displayToast("view detail");
                                int id = (Integer) marker.getTag();
                                if (id==-1) return false;
                                Intent intent = new Intent(context, RentalSpecific.class);
                                Rental currentItem = mRentals.get(id);
                                intent.putExtra("apartmentId", currentItem.getApartment_id());
                                intent.putExtra("address", currentItem.getAddress());
                                intent.putExtra("cost",currentItem.getCost());
                                intent.putExtra("homeOwner", currentItem.getHomeownerID());
                                intent.putExtra("capacity", currentItem.getCapacity());
                                intent.putExtra("latitude", String.valueOf(currentItem.getLatitude()));
                                intent.putExtra("longitude", String.valueOf(currentItem.getLongitude()));
                                intent.putExtra("picNum", currentItem.getPicsNum());
                                startActivity(intent);
                                return true;
                            case R.id.find_route:
                                displayToast("find route");
                                if (startLatLng == null) {
                                    startLatLng = latLng;
                                }
                                else {
                                    destLatLng = latLng;
                                    mapDirectionHelper.startDirection(startLatLng, destLatLng);
                                    return true;
                                }
                            default:
                        }
                        return false;
                    }
                });
        popup.show();

        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (startLatLng == null) {
            startLatLng = latLng;
            Toast.makeText(this,
                    "start:"+latLng.toString(), Toast.LENGTH_SHORT).show();
            addMarkerOnMap(latLng.latitude, latLng.longitude, GREEN_CODE, -1);
        }
        else {
            destLatLng = latLng;
            Toast.makeText(this,
                    "dest:"+latLng.toString(), Toast.LENGTH_SHORT).show();
            mapDirectionHelper.startDirection(startLatLng, destLatLng);
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        startLatLng = destLatLng = null;
        mapDirectionHelper.clearDirectionResult();
        Toast.makeText(this,
                "clear start+dest",
                Toast.LENGTH_SHORT).show();
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