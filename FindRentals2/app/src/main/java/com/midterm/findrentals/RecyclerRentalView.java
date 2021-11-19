package com.midterm.findrentals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.os.Bundle;

import java.util.ArrayList;

public class RecyclerRentalView extends AppCompatActivity {

    private ArrayList<Rental> rentals;
    private RecyclerView rcvRentals;
    private RentalRecyclerAdapter rentalRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_rental_view);
        rentals=loadData();
        initializeViews();
    }

    private void initializeViews() {
        rcvRentals=findViewById(R.id.rcvRentals);
        if(rcvRentals!=null){
            rentalRecyclerAdapter = new RentalRecyclerAdapter(this, rentals);
            rcvRentals.setAdapter(rentalRecyclerAdapter);
            rcvRentals.setLayoutManager(new LinearLayoutManager(this));
            rcvRentals.addItemDecoration(new SimpleDividerItemDecoration(this));
        }
    }

    private ArrayList<Rental> loadData() {
        rentals = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Coordinate c =new Coordinate(12.3,12.3);
            Address a = new Address("69 ABC Quan 1 TP HCM",c);
            Rental r = new Rental(i,a,(i+1)*1000000);
            rentals.add(r);
        }
        return rentals;
    }


}