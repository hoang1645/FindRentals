package com.midterm.findrentals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openMapsView(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void openListView(View view) {
        Intent intent = new Intent(this, RecyclerRentalView.class);
        startActivity(intent);
    }

    public void openYourRental(View view){
        Intent intent = new Intent(this, YourRental.class);
        startActivity(intent);
    }

    public void openFavoriteList(View view){
        Intent intent = new Intent(this, FavoriteActivity.class);
        startActivity(intent);
    }

    public void openProfile(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void openSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}