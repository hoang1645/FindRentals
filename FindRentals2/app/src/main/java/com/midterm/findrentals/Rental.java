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
 *      |---------pics: INT NOT NULL                --Number of pictures for demonstration
 *      |---------latitude: DOUBLE NOT  NULL        --Physical coordinate's latitude
 *      |---------longitude: DOUBLE NOT NULL        --Physical coordinate's longitude
 *      |---------homeowner: STRING NOT NULL        --Homeowner's name
 *      |---------tel:STRING NOT NULL               --Homeowner's telephone number
 * */
public class Rental {
    @PrimaryKey (autoGenerate = true)
    public int apartment_id = 0;
    @ColumnInfo (name = "address") @NonNull
    public String address;
    @ColumnInfo (name = "cost")
    public int cost;
    @ColumnInfo (name = "capacity")
    public int capacity;
    @ColumnInfo (name = "pics")
    public int picsNum;
    @ColumnInfo (name = "latitude")
    public double latitude;
    @ColumnInfo (name = "longitude")
    public double longitude;
    @ColumnInfo (name = "homeowner")
    public String homeowner;
    @ColumnInfo (name = "tel")
    public String tel;

    public Rental(int apartment_id, @NonNull String address, int cost, int capacity,
                  int picsNum, double latitude, double longitude, @NonNull  String homeowner,
                  @NonNull String tel) {
        this.address = address;
        this.cost = cost;
        this.capacity = capacity;
        this.picsNum = picsNum;
        this.latitude = latitude;
        this.longitude = longitude;
        this.apartment_id = apartment_id;
        this.homeowner = homeowner;
        this.tel = tel;
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
