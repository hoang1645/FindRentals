package com.midterm.findrentals;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RentalRepository {
    private ApartmentDAO apartmentDAO;
    private HomeownerDAO homeownerDAO;
    private LiveData<List<Rental>> allRentals;
    private LiveData<List<Homeowner>> allHomeowners;

    public RentalRepository(Application application)
    {
        RentalRoomDatabase rrd = RentalRoomDatabase.getDatabase(application);
        apartmentDAO = rrd.apartmentDAO();
        homeownerDAO = rrd.homeownerDAO();
        allRentals = apartmentDAO.getAll();
        allHomeowners = homeownerDAO.getAllAscending();
    }

    public LiveData<List<Rental>> getAllRentals()
    {
        return allRentals;
    }
    public LiveData<List<Homeowner>> getAllHomeowners()
    {
        return allHomeowners;
    }
    public List<Homeowner> getHomeowner(int id)
    {
        return homeownerDAO.getID(id);
    }
    public List<Homeowner> getHomeowner(Rental rental)
    {
        return homeownerDAO.getID(rental.homeownerID);
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

    public void insert(Homeowner homeowner)
    {
        new insertHomeownerAsyncTask(homeownerDAO).execute(homeowner);
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
    private static class insertHomeownerAsyncTask extends AsyncTask<Homeowner, Void, Void>
    {
        private HomeownerDAO asyncTaskHomeownerDAO;
        public insertHomeownerAsyncTask(HomeownerDAO dao)
        {
            asyncTaskHomeownerDAO = dao;
        }
        @Override
        protected Void doInBackground(Homeowner... rentals) {
            asyncTaskHomeownerDAO.insert(rentals[0]);
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