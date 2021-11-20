package com.midterm.findrentals;

import androidx.annotation.NonNull;
import androidx.room.*;

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
    @PrimaryKey (autoGenerate = true)
    public int apartment_id;
    @ColumnInfo (name = "address") @NonNull
    public String address;
    @ColumnInfo (name = "cost")
    public int cost;
    @ColumnInfo (name = "capacity")
    public int capacity;
    @ColumnInfo (name = "homeowner_id")
    public int homeownerID;
    @ColumnInfo (name = "pics")
    public int picsNum;
    @ColumnInfo (name = "latitude")
    public double latitude;
    @ColumnInfo (name = "longitude")
    public double longitude;

    public Rental(@NonNull String address, int cost, int capacity, int homeownerID, int picsNum, double latitude, double longitude) {
        this.address = address;
        this.cost = cost;
        this.capacity = capacity;
        this.homeownerID = homeownerID;
        this.picsNum = picsNum;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getApartment_id() {
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

    public int getHomeownerID() {
        return homeownerID;
    }

    public void setHomeownerID(int homeownerID) {
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
