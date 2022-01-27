package com.midterm.findrentals;

//NO LONGER USED
import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RentalRepository {
//    private ApartmentDAO apartmentDAO;
//    private HomeownerDAO homeownerDAO;
//    private LiveData<List<Rental>> allRentals;
//    private LiveData<List<Homeowner>> allHomeowners;
//
//    public RentalRepository(Application application)
//    {
//        RentalRoomDatabase rrd = RentalRoomDatabase.getDatabase(application);
//        apartmentDAO = rrd.apartmentDAO();
//        homeownerDAO = rrd.homeownerDAO();
//        allRentals = apartmentDAO.getAll();
//        allHomeowners = homeownerDAO.getAllAscending();
//    }
//
//    public LiveData<List<Rental>> getAllRentals()
//    {
//        return allRentals;
//    }
//    public LiveData<List<Homeowner>> getAllHomeowners()
//    {
//        return allHomeowners;
//    }
//    public List<Homeowner> getHomeowner(String id)
//    {
//        return homeownerDAO.getID(id);
//    }
//    public List<Homeowner> getHomeowner(Rental rental)
//    {
//        return homeownerDAO.getID(rental.getHomeownerID());
//    }
//    public LiveData<List<Rental>> getRentalsByAddress(String addr)
//    {
//        return apartmentDAO.findAddress(addr);
//    }
//    public LiveData<List<Rental>> getRentalsSortedByCost(String sort)
//    {
//        assert(sort.toLowerCase().equals("asc") || sort.toLowerCase().equals("desc"));
//        if (sort.toLowerCase().equals("asc"))
//            return apartmentDAO.getAllByCostAscending();
//        return apartmentDAO.getAllByCostDescending();
//    }
//    public LiveData<List<Rental>> getRentalsSortedByCapacity(String sort)
//    {
//        assert(sort.toLowerCase().equals("asc") || sort.toLowerCase().equals("desc"));
//        if (sort.toLowerCase().equals("asc"))
//            return apartmentDAO.getAllByCapacityAscending();
//        return apartmentDAO.getAllByCapacityDescending();
//    }
//    public LiveData<List<Rental>> findRentalsByCost(int cost, boolean upper)
//    {
//        if (upper)
//            return apartmentDAO.findCostHigherCap(cost);
//        return apartmentDAO.findCostLowerCap(cost);
//    }
//    public LiveData<List<Rental>> findRentalsByCapacity(int cap, int capp, boolean upper) {
//        if (capp != 0 && cap <= capp) return apartmentDAO.findCapacityBetween(cap, capp);
//        if (upper)
//            return apartmentDAO.findCapacityHigherCap(cap);
//        return apartmentDAO.findCapacityLowerCap(cap);
//    }
//
//    public void insert(Rental rental)
//    {
//        RentalRoomDatabase.databaseWriteExecutor.execute(()-> {
//            apartmentDAO.insert(rental);
//        });
//    }
//
//    public void insert(Homeowner homeowner)
//    {
//        RentalRoomDatabase.databaseWriteExecutor.execute(()->{
//            homeownerDAO.insert(homeowner);
//        });
//    }
//    public void deleteRental(Rental rental)
//    {
//        RentalRoomDatabase.databaseWriteExecutor.execute(()->{
//            apartmentDAO.deleteRental(rental.getApartment_id());
//        });
//    }
//    public void deleteAllRentals()
//    {
//        RentalRoomDatabase.databaseWriteExecutor.execute(()->{
//            apartmentDAO.deleteAllRentals();
//        });
//    }
}
