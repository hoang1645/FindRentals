package com.midterm.findrentals;

import androidx.annotation.NonNull;
import androidx.room.*;

import com.google.firebase.auth.FirebaseUser;

//NO LONGER USED AS A SQLITE3 DATABASE ENTITY
@Entity(tableName = "rental")
/* This is the database entity for the table Rental.
 * Database schema:
 * Rental
 *      |---------apartment_id: INT PRIMARY KEY     --Apartment's unique identification number
 *      |---------address: STRING NOT NULL          --Physical address
 *      |---------cost: INT NOT NULL                --Cost of living per month
 *      |---------capacity: INT NOT NULL            --How many people can ideally fit
 *      |---------homeowner_id: INT NOT NULL        --Homeowner's ID
 *      |---------pics: INT NOT NULL                --Number of pictures for demonstration
 *      |---------latitude: DOUBLE NOT  NULL        --Physical coordinate's latitude
 *      |---------longitude: DOUBLE NOT NULL        --Physical coordinate's longitude
 * */
public class Rental {
    //@PrimaryKey (autoGenerate = true)
    private String apartment_id;
    //@ColumnInfo (name = "address") @NonNull
    private String address;
    //@ColumnInfo (name = "cost")
    private int cost;
    //@ColumnInfo (name = "capacity")
    private int capacity;
    //@ColumnInfo (name = "homeowner_id")
    private String homeownerID;
    //@ColumnInfo (name = "pics")
    private int picsNum;
    //@ColumnInfo (name = "latitude")
    private double latitude;
    //@ColumnInfo (name = "longitude")
    private double longitude;

    public Rental() { new Rental("", "", 0, 0, "", 0, 0, 0); }
    
    public Rental(String apartment_id, @NonNull String address,
                  int cost, int capacity, String homeownerID,
                  int picsNum, double latitude, double longitude) {
        this.address = address;
        this.cost = cost;
        this.capacity = capacity;
        this.homeownerID = homeownerID;
        this.picsNum = picsNum;
        this.latitude = latitude;
        this.longitude = longitude;
        this.apartment_id = apartment_id;
    }

    public String getApartment_id() {
        return apartment_id;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull String address) {
        this.address = address;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getHomeownerID() {
        return homeownerID;
    }

    public void setApartment_id(String apartment_id)
    {
        this.apartment_id = apartment_id;
    }

    public void setHomeownerID(String homeownerID) {
        this.homeownerID = homeownerID;
    }

    public int getPicsNum() {
        return picsNum;
    }

    public void setPicsNum(int picsNum) {
        this.picsNum = picsNum;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
