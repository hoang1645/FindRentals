package com.midterm.findrentals;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.*;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

@Database(entities = {Rental.class, Homeowner.class}, version = 2, exportSchema = false)
public abstract class RentalRoomDatabase extends RoomDatabase {
    public abstract ApartmentDAO apartmentDAO();
    public abstract HomeownerDAO homeownerDAO();
    public static int apaMount = 0;
    public static int homMount = 0;
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
                            .addCallback(onOpenCallback)
                            .fallbackToDestructiveMigration().build();
                }
            }
        }
        return instance;
    }
    private static RoomDatabase.Callback onOpenCallback = new RoomDatabase.Callback()
    {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };
    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private ApartmentDAO apartmentDAO;
        private HomeownerDAO homeownerDAO;

        PopulateDBAsyncTask(RentalRoomDatabase db)
        {
            apartmentDAO = db.apartmentDAO();
            homeownerDAO = db.homeownerDAO();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            apartmentDAO.deleteAllRentals();
            homeownerDAO.deleteAllHomeowners();
            ArrayList<Rental> initRentals = new ArrayList<Rental>();
            ArrayList<Homeowner> initHomeowners = new ArrayList<Homeowner>();
            initHomeowners.add(new Homeowner(++homMount, "Phạm Văn Dương","0888379586"));
            initHomeowners.add(new Homeowner(++homMount, "Trần Thị Thu Hằng","0373592680"));
            initHomeowners.add(new Homeowner(++homMount, "Lan Hương","0908076490"));
            initHomeowners.add(new Homeowner(++homMount, "Trịnh Việt Vân","0399978755"));
            initHomeowners.add(new Homeowner(++homMount, "Lê Trung Sơn","0938455546"));
            for (Homeowner homeowner: initHomeowners) {
                homeownerDAO.insert(homeowner);
            }
            initRentals.add(new Rental(++apaMount, "740 Đường Sư Vạn Hạnh Phường 12 Quận 10 Tp Hồ Chí Minh",3500000,35,1,12,10.77234,106.669526));
            initRentals.add(new Rental(++apaMount, "Đường Nguyễn Thị Định Phường Thạnh Mỹ Lợi Quận 2 Tp Hồ Chí Minh",2500000,25,2,6,10.777299,106.766025));
            initRentals.add(new Rental(++apaMount, "36/32 Đường Nguyễn Gia Trí Phường 25 Quận Bình Thạnh Tp Hồ Chí Minh",2500000,20,3,12,10.804,106.71622));
            initRentals.add(new Rental(++apaMount, "274/19 Đường Nam Kỳ Khởi Nghĩa Phường 8 Quận 3 Tp Hồ Chí Minh",3500000,30,4,5,10.788639,106.686534));
            for (Rental rental : initRentals)
                apartmentDAO.insert(rental);
            return null;
        }
    }
}
