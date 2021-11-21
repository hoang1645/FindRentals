package com.midterm.findrentals;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RentalRepository {
    private ApartmentDAO apartmentDAO;
    private LiveData<List<Rental>> allRentals;

    public RentalRepository(Application application)
    {
        RentalRoomDatabase rrd = RentalRoomDatabase.getDatabase(application);
        apartmentDAO = rrd.apartmentDAO();
        allRentals = apartmentDAO.getAll();
    }

    public LiveData<List<Rental>> getAllRentals()
    {
        return allRentals;
    }

    public LiveData<List<Rental>> getRentalsByAddress(String addr)
    {
        return apartmentDAO.findAddress(addr);
    }
    public LiveData<List<Rental>> getRentalsSortedByCost(String sort)
    {
        assert(sort.toLowerCase().equals("asc") || sort.toLowerCase().equals("desc"));
        if (sort.toLowerCase().equals("asc"))
            return apartmentDAO.getAllByCostAscending();
        return apartmentDAO.getAllByCostDescending();
    }
    public LiveData<List<Rental>> getRentalsSortedByCapacity(String sort)
    {
        assert(sort.toLowerCase().equals("asc") || sort.toLowerCase().equals("desc"));
        if (sort.toLowerCase().equals("asc"))
            return apartmentDAO.getAllByCapacityAscending();
        return apartmentDAO.getAllByCapacityDescending();
    }
    public LiveData<List<Rental>> findRentalsByCost(int cost, boolean upper)
    {
        if (upper)
            return apartmentDAO.findCostHigherCap(cost);
        return apartmentDAO.findCostLowerCap(cost);
    }
    public LiveData<List<Rental>> findRentalsByCapacity(int cap, int capp, boolean upper) {
        if (capp != 0 && cap <= capp) return apartmentDAO.findCapacityBetween(cap, capp);
        if (upper)
            return apartmentDAO.findCapacityHigherCap(cap);
        return apartmentDAO.findCapacityLowerCap(cap);
    }

    public void insert(Rental rental)
    {
        new insertRentalAsyncTask(apartmentDAO).execute(rental);
    }


    public void deleteRental(Rental rental)
    {
        new deleteRentalAsyncTask(apartmentDAO).execute(rental);
    }

    private static class insertRentalAsyncTask extends AsyncTask<Rental, Void, Void>
    {
        private ApartmentDAO asyncTaskApartmentDAO;
        public insertRentalAsyncTask(ApartmentDAO dao)
        {
            asyncTaskApartmentDAO = dao;
        }
        @Override
        protected Void doInBackground(Rental... rentals) {
            asyncTaskApartmentDAO.insert(rentals[0]);
            return null;
        }
    }
    private static class deleteRentalAsyncTask extends AsyncTask<Rental, Void, Void>
    {
        private ApartmentDAO asyncTaskApartmentDAO;
        public deleteRentalAsyncTask(ApartmentDAO dao)
        {
            asyncTaskApartmentDAO = dao;
        }
        @Override
        protected Void doInBackground(Rental... rentals) {
            asyncTaskApartmentDAO.deleteRental(rentals[0].apartment_id);
            return null;
        }
    }

}
