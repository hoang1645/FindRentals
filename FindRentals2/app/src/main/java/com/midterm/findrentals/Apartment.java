package com.midterm.findrentals;

import androidx.annotation.NonNull;
import androidx.room.*;
import androidx.room.Entity;


@Entity(tableName = "apartments")
/* This is the database entity for the table Apartment.
 * Database schema:
 * Apartment
 *      |---------apartment_id: INT PRIMARY KEY     --Apartment's unique identification number
 *      |---------address: STRING NOT NULL          --Physical address
 *      |---------cost: INT NOT NULL                --Cost of living per month
 *      |---------capacity: INT NOT NULL            --How many people can ideally fit
 *      |---------homeowner_id: INT NOT NULL        --Homeowner's ID
 *      |---------pics: INT NOT NULL                --Number of pictures for demonstration
 * */
public class Apartment {
    @PrimaryKey (autoGenerate = true)
    public int apartment_id;
    @ColumnInfo (name = "address") @NonNull
    public String address;
    @ColumnInfo (name = "cost") @NonNull
    public int cost;
    @ColumnInfo (name = "capacity") @NonNull
    public int capacity;
    @ColumnInfo (name = "homeowner_id") @NonNull
    public int homeownerID;
    @ColumnInfo (name = "pics") @NonNull
    public int picsNum;
}
