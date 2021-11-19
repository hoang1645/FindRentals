package com.midterm.findrentals;

import android.content.Context;

import androidx.room.*;

@Database(entities = {Rental.class, Homeowner.class}, version = 1, exportSchema = false)
public abstract class RentalRoomDatabase extends RoomDatabase {
    public abstract ApartmentDAO apartmentDAO();
    public abstract HomeownerDAO homeownerDAO();
    private static RentalRoomDatabase instance;
    public static RentalRoomDatabase getDatabase(Context context)
    {
        if (instance == null)
        {
            synchronized (RentalRoomDatabase.class)
            {
                if (instance == null)
                {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            RentalRoomDatabase.class, "rentals")
                            .fallbackToDestructiveMigration().build();
                }
            }
        }
        return instance;
    }
}
