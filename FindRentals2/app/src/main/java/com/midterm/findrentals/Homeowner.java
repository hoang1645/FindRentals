package com.midterm.findrentals;
import androidx.annotation.NonNull;
import androidx.room.*;

/* This is the database entity for the table Apartment.
 * Database schema:
 * Homeowner
 *      |---------homeowner_id: INT PRIMARY KEY     --Homeowner's unique identification number
 *      |---------name: STRING NOT NULL             --Homeowner's name
 *      |---------tel: STRING NOT NULL              --Homeowner's telephone number
 * */

@Entity(tableName = "homeowner")
public class Homeowner {
    @PrimaryKey (autoGenerate = true)
    public int homeowner_id;
    @ColumnInfo(name = "name") @NonNull
    public String name;
    @ColumnInfo(name = "tel") @NonNull
    public String telephoneNumber;
    public Homeowner(String name, String tel)
    {
        this.name = name;
        this.telephoneNumber = tel;
    }
}
