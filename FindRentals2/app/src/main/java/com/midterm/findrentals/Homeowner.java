package com.midterm.findrentals;
import androidx.annotation.NonNull;
import androidx.room.*;
//NO LONGER USED
/* This is the database entity for the table Apartment.
 * Database schema:
 * Homeowner
 *      |---------homeowner_id: INT PRIMARY KEY     --Homeowner's unique identification number
 *      |---------name: STRING NOT NULL             --Homeowner's name
 *      |---------tel: STRING NOT NULL              --Homeowner's telephone number
 * */

@Entity(tableName = "homeowner")
public class Homeowner extends User{
    @PrimaryKey
    public String homeowner_id;
    @ColumnInfo(name = "name") @NonNull
    public String name;
    @ColumnInfo(name = "tel") @NonNull
    public String telephoneNumber;
    public Homeowner(String homeowner_id, @NonNull String name, @NonNull String telephoneNumber)
    {
        this.homeowner_id = homeowner_id;
        this.name = name;
        this.telephoneNumber = telephoneNumber;
    }
}
