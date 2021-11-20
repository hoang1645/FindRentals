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
            Rental r = new Rental("69 Nguyễn Văn Cừ, quận 5, thành phố Hồ Chí Minh",2000000,4,i,i,12.4,12.4);
            rentals.add(r);
        }
        return rentals;
    }


}