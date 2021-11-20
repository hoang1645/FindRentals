package com.midterm.findrentals;

import androidx.lifecycle.LiveData;
import androidx.room.*;

import java.util.List;

@Dao
public interface ApartmentDAO {
    //Data manipulation queries
    @Insert
    public void insert(Rental ap);
    @Update
    public void update(Rental... aps);
    @Query("delete from rental")
    public void deleteAllRentals();
    @Query("delete from rental where apartment_id=:id")
    public void deleteRental(int id);

    //Get all data from db
    @Query("select * from Rental order by apartment_id asc")
    public LiveData<List<Rental>> getAll();
    @Query("select * from Rental order by cost asc")
    public LiveData<List<Rental>> getAllByCostAscending();
    @Query("select * from Rental order by cost desc")
    public LiveData<List<Rental>> getAllByCostDescending();
    @Query("select * from Rental order by capacity asc")
    public LiveData<List<Rental>> getAllByCapacityAscending();
    @Query("select * from Rental order by capacity desc")
    public LiveData<List<Rental>> getAllByCapacityDescending();


    //Search by address
    @Query("select * from Rental where address like :query")
    public LiveData<List<Rental>> findAddress(String query);

    //Search by cost
    @Query("select * from Rental where cost <= :cap")
    public LiveData<List<Rental>> findCostHigherCap(int cap);
    @Query("select * from Rental where cost >= :cap")
    public LiveData<List<Rental>> findCostLowerCap(int cap);
    @Query("select * from Rental where cost <= :hCap and cost >= :lCap")
    public LiveData<List<Rental>> findCostBetween(int lCap, int hCap);

    //Search by capacity
    @Query("select * from Rental where capacity <= :cap")
    public LiveData<List<Rental>> findCapacityHigherCap(int cap);
    @Query("select * from Rental where capacity >= :cap")
    public LiveData<List<Rental>> findCapacityLowerCap(int cap);
    @Query("select * from Rental where capacity <= :hCap and capacity >= :lCap")
    public LiveData<List<Rental>> findCapacityBetween(int lCap, int hCap);
    @Query("select * from Rental where capacity = :num")
    public LiveData<List<Rental>> findCapacity(int num);
}
