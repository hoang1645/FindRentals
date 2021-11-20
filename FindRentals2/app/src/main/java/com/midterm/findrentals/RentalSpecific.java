package com.midterm.findrentals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class RentalSpecific extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_specific);
        Intent intent = getIntent();
        int apartmentId = intent.getIntExtra("apartmentId", -1);
        String address = intent.getStringExtra("address");
        int cost = intent.getIntExtra("cost", 1000000);
        int homeOwner = intent.getIntExtra("homeOwner", -1);
        int capacity = intent.getIntExtra("capacity", 0);
        double latitude = Double.valueOf(intent.getStringExtra("latitude"));
        double longitude = Double.valueOf(intent.getStringExtra("longitude"));
        int picNum = intent.getIntExtra("picNum", -1);
        setTextViewContent(R.id.rentalSpecificApartmentID, "Apartment ID", String.valueOf(apartmentId));
        setTextViewContent(R.id.rentalSpecificCost, "Cost", String.valueOf(cost));
        setTextViewContent(R.id.rentalSpecificHomeOwnerId, "Homeowner ID", String.valueOf(homeOwner));
        setTextViewContent(R.id.rentalSpecificAddress, "Address", address);
        setTextViewContent(R.id.rentalSpecificCapacity, "Capacity", String.valueOf(capacity));
        setTextViewContent(R.id.rentalSpecificLatitude, "Latitude", String.valueOf(latitude));
        setTextViewContent(R.id.rentalSpecificLongitude, "Longitude", String.valueOf(longitude));
    }

    private void setTextViewContent(int idTextView, String title, String content) {
        TextView t = (TextView) findViewById(idTextView);
        t.setTextSize(20);
        t.setText(title + ": " + content);
    }


}