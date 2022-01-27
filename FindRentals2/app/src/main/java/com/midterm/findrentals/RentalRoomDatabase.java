package com.midterm.findrentals;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;

@Database(entities = {Rental.class, Homeowner.class}, version = 2, exportSchema = false)
public abstract class RentalRoomDatabase extends RoomDatabase {
    public abstract ApartmentDAO apartmentDAO();
    public abstract HomeownerDAO homeownerDAO();
    public static int apaMount = 0;
    public static int homMount = 0;
    private static RentalRoomDatabase INSTANCE;
    public static RentalRoomDatabase getDatabase(Context context)
    {
        if (INSTANCE == null)
        {
            synchronized (RentalRoomDatabase.class)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RentalRoomDatabase.class, "rentals")
                            .addCallback(onOpenCallback)
                            .fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
    private static RoomDatabase.Callback onOpenCallback = new RoomDatabase.Callback()
    {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDBAsyncTask(INSTANCE).execute();
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
            initHomeowners.add(new Homeowner(++homMount, "Đỗ Yến", "0981685426"));
            for (Homeowner homeowner: initHomeowners) {
                homeownerDAO.insert(homeowner);
            }
            initRentals.add(new Rental(++apaMount, "740 Đường Sư Vạn Hạnh Phường 12 Quận 10 Tp Hồ Chí Minh",3500000,35,1,12,10.77234,106.669526));
            initRentals.add(new Rental(++apaMount, "Đường Nguyễn Thị Định Phường Thạnh Mỹ Lợi Quận 2 Tp Hồ Chí Minh",2500000,25,3,6,10.777299,106.766025));
            initRentals.add(new Rental(++apaMount, "36/32 Đường Nguyễn Gia Trí Phường 25 Quận Bình Thạnh Tp Hồ Chí Minh",2500000,20,4,12,10.804,106.71622));
            initRentals.add(new Rental(++apaMount, "274/19 Đường Nam Kỳ Khởi Nghĩa Phường 8 Quận 3 Tp Hồ Chí Minh",3500000,30,5,5,10.788639,106.686534));
            initRentals.add(new Rental(++apaMount, "Đường Số 6, Phường Bình Trưng Đông, Quận 2, Tp Hồ Chí Minh",2200000,20,3,4,10.785532,106.775883));
            initRentals.add(new Rental(++apaMount, "88/12, Đường Bạch Đằng, Phường 24, Quận Bình Thạnh, Tp Hồ Chí Minh",3000000,25,6,3,10.803285,106.709515));
            for (Rental rental : initRentals)
                apartmentDAO.insert(rental);
            return null;
        }
    }
}
