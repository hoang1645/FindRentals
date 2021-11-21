package com.midterm.findrentals;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RentalSpecific extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_specific);
        Intent intent = getIntent();
        String address = intent.getStringExtra("address");
        int cost = intent.getIntExtra("cost", 1000000);
        String homeOwner = intent.getStringExtra("homeOwnerName");
        String tel = intent.getStringExtra("homeOwnerTel");
        int capacity = intent.getIntExtra("capacity", 0);
        int picNum = intent.getIntExtra("picNum", -1);
        setTextViewContent(R.id.rentalSpecificCost, "Cost", String.valueOf(cost) + " VND/month");
        setTextViewContent(R.id.rentalSpecificHomeOwnerName, "Homeowner's name", homeOwner);
        setTextViewContent(R.id.rentalSpecificHomeOwnerTel, "Telephone number", tel);
        setTextViewContent(R.id.rentalSpecificAddress, "Address", address);
        setTextViewContent(R.id.rentalSpecificCapacity, "Capacity", String.valueOf(capacity) + " m2");
    }

    private void setTextViewContent(int idTextView, String title, String content) {
        TextView t = (TextView) findViewById(idTextView);
        t.setTextSize(20);
        t.setText(title + ": " + content);
    }


}