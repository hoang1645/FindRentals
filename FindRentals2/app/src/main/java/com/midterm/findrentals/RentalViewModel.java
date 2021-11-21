package com.midterm.findrentals;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class RentalViewModel extends AndroidViewModel {
    private RentalRepository repository;
    private LiveData<List<Rental>> allRentals;


    public RentalViewModel(@NonNull Application application) {
        super(application);
        repository = new RentalRepository(application);
        allRentals = repository.getAllRentals();
    }
    public LiveData<List<Rental>> getAllRentals()
    {
        return allRentals;
    }

    public void deleteRental(Rental rental)
    {
        repository.deleteRental(rental);
        allRentals = repository.getAllRentals();
    }

    public void insert(Rental rental)
    {
        repository.insert(rental);
    }

    public LiveData<List<Rental>> getRentalsByAddress(String addr)
    {
        return repository.getRentalsByAddress(addr);
    }
    public LiveData<List<Rental>> getRentalsSortedByCost(String sort)
    {
        return repository.getRentalsSortedByCost(sort);
    }
    public LiveData<List<Rental>> getRentalsSortedByCapacity(String sort)
    {
        return repository.getRentalsSortedByCapacity(sort);
    }
    public LiveData<List<Rental>> findRentalsByCost(int cost, boolean upper)
    {
        return repository.findRentalsByCost(cost, upper);
    }
    public LiveData<List<Rental>> findRentalsByCapacity(int cap, int capp, boolean upper) {
        return repository.findRentalsByCapacity(cap, capp, upper);
    }
}
