package com.midterm.findrentals;

import androidx.room.*;

import java.util.List;

@Dao
public interface ApartmentDAO {
    //Data manipulation queries
    @Insert
    public void insert(Rental ap);
    @Update
    public void update(Rental... aps);

    //Get all data from db
    @Query("select * from Rental order by apartment_id asc")
    public List<Rental> getAll();
    @Query("select * from Rental order by cost asc")
    public List<Rental> getAllByCostAscending();
    @Query("select * from Rental order by cost desc")
    public List<Rental> getAllByCostDescending();
    @Query("select * from Rental order by capacity asc")
    public List<Rental> getAllByCapacityAscending();
    @Query("select * from Rental order by capacity desc")
    public List<Rental> getAllByCapacityDescending();


    //Search by address
    @Query("select * from Rental where address like :query")
    public List<Rental> findAddress(String query);

    //Search by cost
    @Query("select * from Rental where cost <= :cap")
    public List<Rental> findCostHigherCap(int cap);
    @Query("select * from Rental where cost >= :cap")
    public List<Rental> findCostLowerCap(int cap);
    @Query("select * from Rental where cost <= :hCap and cost >= :lCap")
    public List<Rental> findCostBetween(int lCap, int hCap);

    //Search by capacity
    @Query("select * from Rental where capacity <= :cap")
    public List<Rental> findCapacityHigherCap(int cap);
    @Query("select * from Rental where capacity >= :cap")
    public List<Rental> findCapacityLowerCap(int cap);
    @Query("select * from Rental where capacity <= :hCap and capacity >= :lCap")
    public List<Rental> findCapacityBetween(int lCap, int hCap);
    @Query("select * from Rental where capacity = :num")
    public List<Rental> findCapacity(int num);
}
