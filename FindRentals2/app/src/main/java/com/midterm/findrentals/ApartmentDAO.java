package com.midterm.findrentals;

import androidx.room.*;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ApartmentDAO {
    //Data manipulation queries
    @Insert
    public void insert(Apartment ap);
    @Update
    public void update(Apartment... aps);

    //Get all data from db
    @Query("select * from apartments order by apartment_id asc")
    public List<Apartment> getAll();
    @Query("select * from apartments order by cost asc")
    public List<Apartment> getAllByCostAscending();
    @Query("select * from apartments order by cost desc")
    public List<Apartment> getAllByCostDescending();
    @Query("select * from apartments order by capacity asc")
    public List<Apartment> getAllByCapacityAscending();
    @Query("select * from apartments order by capacity desc")
    public List<Apartment> getAllByCapacityDescending();


    //Search by address
    @Query("select * from apartments where address like :query")
    public List<Apartment> findAddress(String query);

    //Search by cost
    @Query("select * from apartments where cost <= :cap")
    public List<Apartment> findCostHigherCap(int cap);
    @Query("select * from apartments where cost >= :cap")
    public List<Apartment> findCostLowerCap(int cap);
    @Query("select * from apartments where cost <= :hCap and cost >= :lCap")
    public List<Apartment> findCostBetween(int lCap, int hCap);

    //Search by capacity
    @Query("select * from apartments where capacity <= :cap")
    public List<Apartment> findCapacityHigherCap(int cap);
    @Query("select * from apartments where capacity >= :cap")
    public List<Apartment> findCapacityLowerCap(int cap);
    @Query("select * from apartments where capacity <= :hCap and capacity >= :lCap")
    public List<Apartment> findCapacityBetween(int lCap, int hCap);
    @Query("select * from apartments where capacity = :num")
    public List<Apartment> findCapacity(int num);
}
