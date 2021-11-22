package com.midterm.findrentals;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private Context mContext = this;
    private SearchView searchView;
    private View textViewOptions;
    private MapDirectionHelper mapDirectionHelper;

    private LatLng startLatLng, destLatLng;
    private int findRouteFrom = -1;
    private List<Marker> tempMarker;

    private ActivityMapsBinding binding;
    private RentalViewModel mRentalViewModel;
    private List<Rental> mRentals;
    private List<Homeowner> mHomeowners;
    public static final LatLng defaultLatLng = new LatLng(10.7792897,106.6636144);

    private static final float RED_CODE = BitmapDescriptorFactory.HUE_RED;
    private static final float GREEN_CODE = BitmapDescriptorFactory.HUE_GREEN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        searchView = findViewById(R.id.idSearchView);
        textViewOptions = findViewById(R.id.textviewOptions);

        mRentals = new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mRentalViewModel = new ViewModelProvider(this).get(RentalViewModel.class);
        mRentalViewModel.getAllRentals().observe(this, new Observer<List<Rental>>() {
            @Override
            public void onChanged(@Nullable final List<Rental> rentals) {
                mRentals = rentals;
                addRentalsOnMap(mRentals);
            }
        });
        mRentalViewModel.getAllHomeowners().observe(this, new Observer<List<Homeowner>>() {
            @Override
            public void onChanged(@Nullable final List<Homeowner> homeowners) {
                mHomeowners = homeowners;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String address) {
                LatLng latLngQuery = getLocationFromAddress(address + "Ho Chi Minh City");
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngQuery));
                findRoute(latLngQuery);
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

        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLongClickListener(this);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLatLng));
        mapDirectionHelper = new MapDirectionHelper(mMap, this);
    }

    private void addRentalsOnMap(List<Rental> rentals) {
        for (int i=0;i<rentals.size();i++){
            addMarkerOnMap(rentals.get(i).getLatitude(), rentals.get(i).getLongitude(),
                    GREEN_CODE, i, rentals.get(i).getAddress());
        }
    }

    private Marker addMarkerOnMap(double lat, double lng, float colorCode, int id, String address) {
        LatLng position = new LatLng(lat, lng);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.defaultMarker(colorCode));
        if (id != -1)
            markerOptions.title("Available").snippet(address);

        Marker marker = mMap.addMarker(markerOptions);
        marker.setTag(id);

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }
            @Override
            public View getInfoContents(Marker marker) {
                return createMarkerContentLayout(marker);
            }
        });

        return marker;
    }

    public LinearLayout createMarkerContentLayout(Marker marker){
        LinearLayout info = new LinearLayout(mContext);
        info.setOrientation(LinearLayout.VERTICAL);

        TextView title = new TextView(mContext);
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, Typeface.BOLD);
        title.setText(marker.getTitle());
        title.setWidth(400);

        TextView snippet = new TextView(mContext);
        snippet.setTextColor(Color.BLACK);
        snippet.setText(marker.getSnippet());
        snippet.setGravity(Gravity.CENTER);
        snippet.setWidth(400);

        info.addView(title);
        info.addView(snippet);

        return info;
    }

    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }

    private void findRoute(LatLng latLng) {
        if (tempMarker==null)
            tempMarker = new ArrayList<>();
        clearTempMarker();
        if (findRouteFrom == 1) {
            destLatLng = latLng;
            Marker destMarker = addMarkerOnMap(latLng.latitude, latLng.longitude, RED_CODE,
                    -1, null);
            tempMarker.add(destMarker);
            mapDirectionHelper.startDirection(startLatLng, destLatLng);
            displayToast("Long click on map to clear route");
        }
        else if (findRouteFrom == 0){
            startLatLng = latLng;
            Marker startMarker = addMarkerOnMap(latLng.latitude, latLng.longitude, RED_CODE,
                    -1, null);
            tempMarker.add(startMarker);
            mapDirectionHelper.startDirection(startLatLng, destLatLng);
            displayToast("Long click on map to clear route");
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        int id = (Integer) marker.getTag();
        if (id == -1) return false;

        LatLng latLng = marker.getPosition();

        PopupMenu popup = new PopupMenu(MapsActivity.this, textViewOptions);
        popup.getMenuInflater().inflate(R.menu.menu_marker_context, popup.getMenu());
        popup.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.view_detail:
                                Intent intent = createIntent2DetailView(mContext, marker);
                                if (intent != null){
                                    startActivity(intent);
                                    return true;
                                }
                                return false;
                            case R.id.find_route_from:
                                clearRoute();
                                displayToast("Choose destination on search bar or click on map");
                                startLatLng = latLng;
                                findRouteFrom = 1;
                                return true;
                            case R.id.find_route_to:
                                clearRoute();
                                displayToast("Choose start location on search bar or click on map");
                                destLatLng = latLng;
                                findRouteFrom = 0;
                                return true;
                            default:
                        }
                        return false;
                    }
                });
        popup.show();

        return false;
    }

    public Homeowner getHomeownerFromID(int id) {
        for (Homeowner homeowner : mHomeowners) {
            if (homeowner.homeowner_id == id) {
                return homeowner;
            }
        }
        return null;
    }

    private Intent createIntent2DetailView(Context context, Marker marker) {
        int i = (Integer) marker.getTag();
        if (i==-1) return null;

        Intent intent = new Intent(context, RentalSpecific.class);
        Rental currentItem = mRentals.get(i);

        intent.putExtra("address", currentItem.getAddress());
        intent.putExtra("cost", currentItem.getCost());
        intent.putExtra("homeOwnerName", getHomeownerFromID(currentItem.getHomeownerID()).name);
        intent.putExtra("homeOwnerTel", getHomeownerFromID(currentItem.getHomeownerID()).telephoneNumber);
        intent.putExtra("capacity", currentItem.getCapacity());
        intent.putExtra("picNum", currentItem.getPicsNum());
        intent.putExtra("apartment_id",currentItem.getApartment_id());

        return intent;
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        findRoute(latLng);
    }

    public void clearRoute(){
        startLatLng = destLatLng = null;
        findRouteFrom = -1;
        mapDirectionHelper.clearDirectionResult();
        clearTempMarker();
    }

    public void clearTempMarker(){
        if (tempMarker != null){
            for (int i=0;i<tempMarker.size();i++)
                tempMarker.get(i).remove();
            tempMarker.clear();
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        clearRoute();
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