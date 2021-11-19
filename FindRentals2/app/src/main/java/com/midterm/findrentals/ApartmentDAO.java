package com.midterm.findrentals;

import androidx.room.*;

import java.util.ArrayList;

@Dao
public interface ApartmentDAO {
    //Data manipulation queries
    @Insert
    public void insert(Apartment ap);
    @Update
    public void update(Apartment... aps);

    //Get all data from db
    @Query("select * from apartments order by apartment_id asc")
    public ArrayList<Apartment> getAll();
    @Query("select * from apartments order by cost asc")
    public ArrayList<Apartment> getAllByCostAscending();
    @Query("select * from apartments order by cost desc")
    public ArrayList<Apartment> getAllByCostDescending();
    @Query("select * from apartments order by capacity asc")
    public ArrayList<Apartment> getAllByCapacityAscending();
    @Query("select * from apartments order by capacity desc")
    public ArrayList<Apartment> getAllByCapacityDescending();


    //Search by address
    @Query("select * from apartments where address like :query")
    public ArrayList<Apartment> findAddress(String query);

    //Search by cost
    @Query("select * from apartments where cost <= :cap")
    public ArrayList<Apartment> findCostHigherCap(int cap);
    @Query("select * from apartments where cost >= :cap")
    public ArrayList<Apartment> findCostLowerCap(int cap);
    @Query("select * from apartments where cost <= :hCap and cost >= :lCap")
    public ArrayList<Apartment> findCostBetween(int lCap, int hCap);

    //Search by capacity
    @Query("select * from apartments where capacity <= :cap")
    public ArrayList<Apartment> findCapacityHigherCap(int cap);
    @Query("select * from apartments where capacity >= :cap")
    public ArrayList<Apartment> findCapacityLowerCap(int cap);
    @Query("select * from apartments where capacity <= :hCap and capacity >= :lCap")
    public ArrayList<Apartment> findCapacityBetween(int lCap, int hCap);
    @Query("select * from apartments where capacity = :num")
    public ArrayList<Apartment> findCapacity(int num);
}
