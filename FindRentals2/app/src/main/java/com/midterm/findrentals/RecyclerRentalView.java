package com.midterm.findrentals;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class RecyclerRentalView extends AppCompatActivity {

    private List<Rental> rentals;
    private RecyclerView rcvRentals;
    private RentalRecyclerAdapter rentalRecyclerAdapter;

    private RentalViewModel rentalViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_rental_view);
        rentalViewModel = new ViewModelProvider(this).get(RentalViewModel.class);
        rentalViewModel.getAllRentals().observe(this, new Observer<List<Rental>>() {
            @Override
            public void onChanged(@Nullable final List<Rental> rentalsList) {
                rentals = rentalsList;
                initializeViews();
            }
        });
    }

    private List<Rental> filter(String text) {
        List<Rental> filteredList = new ArrayList<>();
        for (Rental item : rentals) {
            if (item.getAddress().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rental_view_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                rentalRecyclerAdapter.filterList(filter(s));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                rentalRecyclerAdapter.filterList(filter(s));
                return false;
            }
        });
        return true;
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


}