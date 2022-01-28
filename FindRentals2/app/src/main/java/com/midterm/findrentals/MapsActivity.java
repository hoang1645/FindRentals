package com.midterm.findrentals;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private Context mContext = this;
    private SearchView searchView;
    private View textViewOptions;
    private MapDirectionHelper mapDirectionHelper;

    private LatLng startLatLng, destLatLng, currLatLng;
    private int findRouteFrom = -1;
    private float zoomLevel = 13;
    private List<Marker> tempMarker;
    private int currMarkerId = -1;

    private RentalViewModel mRentalViewModel;
    private List<Rental> mRentals;
    public static final LatLng defaultLatLng = new LatLng(10.7792897,106.6636144);

    private static final float RED_CODE = BitmapDescriptorFactory.HUE_RED;
    private static final float GREEN_CODE = BitmapDescriptorFactory.HUE_GREEN;

    private static final float NULL_LATLNG = -999;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        textViewOptions = findViewById(R.id.textviewOptions);
        searchView = findViewById(R.id.idSearchView);
        setSearchViewListener();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (savedInstanceState != null) {
            restoreAfterConfigChanged(savedInstanceState);
        }
    }

    private void restoreAfterConfigChanged(Bundle savedInstanceState) {
        currLatLng = restoreLatLng(savedInstanceState, "curr");
        startLatLng = restoreLatLng(savedInstanceState, "start");
        destLatLng = restoreLatLng(savedInstanceState, "dest");

        currMarkerId = savedInstanceState.getInt("currMarkerId");
        findRouteFrom = savedInstanceState.getInt("findRouteFrom");
        zoomLevel = savedInstanceState.getFloat("zoomLevel");
    }

    private LatLng restoreLatLng(Bundle savedInstanceState, String name){
        float lat = savedInstanceState.getFloat(name + "Lat");
        float lng = savedInstanceState.getFloat(name + "Lng");
        if (lat != NULL_LATLNG && lng != NULL_LATLNG){
            return new LatLng(lat, lng);
        }
        return null;
    }

    private void setSearchViewListener() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String address) {
                if (address != ""){
                    LatLng latLngQuery = getLocationFromAddress(address + "Ho Chi Minh City");
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngQuery));
                    findRoute(latLngQuery);
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String address) {
                return false;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mMap != null) {
            LatLng currLatLng = mMap.getCameraPosition().target;

            putLatLng(outState, currLatLng, "curr");
            putLatLng(outState, startLatLng, "start");
            putLatLng(outState, destLatLng, "dest");

            outState.putInt("currMarkerId", currMarkerId);
            outState.putInt("findRouteFrom", findRouteFrom);
            outState.putFloat("zoomLevel", mMap.getCameraPosition().zoom);
        }
    }

    private void putLatLng(Bundle outState, LatLng latLng, String name) {
        if (latLng != null){
            outState.putFloat(name + "Lat", (float) latLng.latitude);
            outState.putFloat(name + "Lng", (float) latLng.longitude);
        }
        else{
            outState.putFloat(name + "Lat", NULL_LATLNG);
            outState.putFloat(name + "Lng", NULL_LATLNG);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        loadDataFromViewModel();
        searchView.clearFocus();

        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLongClickListener(this);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLatLng));
        restoreCameraPosition();

        mapDirectionHelper = new MapDirectionHelper(mMap, this);
        restoreRouteAfterConfigChanged();
    }

    private void restoreCameraPosition() {
        if (currLatLng != null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLatLng, zoomLevel));
        }
    }

    private void restoreRouteAfterConfigChanged() {
        if (startLatLng != null && destLatLng != null){
            if (findRouteFrom == 1) {
                findRoute(destLatLng);
            }
            else if (findRouteFrom == 0){
                findRoute(startLatLng);
            }
        }
    }

    private void loadDataFromViewModel() {
        mRentalViewModel = new ViewModelProvider(this).get(RentalViewModel.class);
        RentalViewModel.RentalCollection mRentalCollection = mRentalViewModel.downloadRentals(mUser);
        mRentals = mRentalCollection.allRentals;
        addRentalsOnMap(mRentals);
    }

    private void addRentalsOnMap(List<Rental> rentals) {
        if (rentals == null) return;
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
            markerOptions.title("Available rental").snippet(address);
        Marker marker = null;
        try {
            marker = mMap.addMarker(markerOptions);
            marker.setTag(id);

            if (id != -1) {
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
            }
        }
        catch (Exception e){
            Log.d("1", "1");
        }

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
        currMarkerId = -1;
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
        currMarkerId = id;
        if (id == -1) return false;
        createPopupMenu(marker);

        return false;
    }

    private void createPopupMenu(Marker marker) {
        LatLng latLng = marker.getPosition();

        PopupMenu popup = new PopupMenu(MapsActivity.this, textViewOptions);
        popup.getMenuInflater().inflate(R.menu.menu_marker_context, popup.getMenu());
        popup.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.view_detail:
                                currMarkerId = -1;
                                Intent intent = createIntent2DetailView(mContext, marker);
                                if (intent != null){
                                    startActivity(intent);
                                    return true;
                                }
                                return false;
                            case R.id.find_route_from:
                                currMarkerId = -1;
                                clearRoute();
                                displayToast("Choose destination on search bar or click on map");
                                startLatLng = latLng;
                                findRouteFrom = 1;
                                return true;
                            case R.id.find_route_to:
                                currMarkerId = -1;
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
    }

    private Intent createIntent2DetailView(Context context, Marker marker) {
        int i = (Integer) marker.getTag();
        if (i==-1) return null;

        Intent intent = new Intent(context, RentalSpecific.class);
        Rental currentItem = mRentals.get(i);

        intent.putExtra("address", currentItem.getAddress());
        intent.putExtra("cost", currentItem.getCost());
        intent.putExtra("homeOwnerName", mRentalViewModel.getHomeownerUserInformationFromRental(currentItem).getName());
        intent.putExtra("homeOwnerTel", mRentalViewModel.getHomeownerUserInformationFromRental(currentItem).getTel());
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