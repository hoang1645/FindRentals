package com.midterm.findrentals;
//NO LONGER USED
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@Database(entities = {Rental.class, Homeowner.class}, version = 2, exportSchema = false)
public abstract class RentalRoomDatabase extends RoomDatabase {
//    public abstract ApartmentDAO apartmentDAO();
//    public abstract HomeownerDAO homeownerDAO();
//    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(2);
////    public static int apaMount = 0;
////    public static int homMount = 0;
//    private static RentalRoomDatabase INSTANCE = null;
//    public static RentalRoomDatabase getDatabase(Context context)
//    {
//        if (INSTANCE == null)
//        {
//            synchronized (RentalRoomDatabase.class)
//            {
//                if (INSTANCE == null)
//                {
//                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
//                            RentalRoomDatabase.class, "rentals")
//                            .addCallback(onOpenCallback)
//                            .fallbackToDestructiveMigration().build();
//                }
//            }
//        }
//        return INSTANCE;
//    }
//    private static RoomDatabase.Callback onOpenCallback = new RoomDatabase.Callback()
//    {
//        @Override
//        public void onOpen(@NonNull SupportSQLiteDatabase db) {
//            super.onOpen(db);
//            new PopulateDBAsyncTask(INSTANCE).execute();
//        }
//    };
//    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void>
//    {
//        private ApartmentDAO apartmentDAO;
//        private HomeownerDAO homeownerDAO;
//
//        PopulateDBAsyncTask(RentalRoomDatabase db)
//        {
//            apartmentDAO = db.apartmentDAO();
//            homeownerDAO = db.homeownerDAO();
//        }
//        @Override
//        protected Void doInBackground(Void... voids) {
//            apartmentDAO.deleteAllRentals();
//            homeownerDAO.deleteAllHomeowners();
//            ArrayList<Rental> initRentals = new ArrayList<Rental>();
//            ArrayList<Homeowner> initHomeowners = new ArrayList<Homeowner>();
//            initHomeowners.add(new Homeowner(++homMount, "Ph???m V??n D????ng","0888379586"));
//            initHomeowners.add(new Homeowner(++homMount, "Tr???n Th??? Thu H???ng","0373592680"));
//            initHomeowners.add(new Homeowner(++homMount, "Lan H????ng","0908076490"));
//            initHomeowners.add(new Homeowner(++homMount, "Tr???nh Vi???t V??n","0399978755"));
//            initHomeowners.add(new Homeowner(++homMount, "L?? Trung S??n","0938455546"));
//            initHomeowners.add(new Homeowner(++homMount, "????? Y???n", "0981685426"));
//            for (Homeowner homeowner: initHomeowners) {
//                homeownerDAO.insert(homeowner);
//            }
//            initRentals.add(new Rental(++apaMount, "740 ???????ng S?? V???n H???nh Ph?????ng 12 Qu???n 10 Tp H??? Ch?? Minh",3500000,35,1,12,10.77234,106.669526));
//            initRentals.add(new Rental(++apaMount, "???????ng Nguy???n Th??? ?????nh Ph?????ng Th???nh M??? L???i Qu???n 2 Tp H??? Ch?? Minh",2500000,25,3,6,10.777299,106.766025));
//            initRentals.add(new Rental(++apaMount, "36/32 ???????ng Nguy???n Gia Tr?? Ph?????ng 25 Qu???n B??nh Th???nh Tp H??? Ch?? Minh",2500000,20,4,12,10.804,106.71622));
//            initRentals.add(new Rental(++apaMount, "274/19 ???????ng Nam K??? Kh???i Ngh??a Ph?????ng 8 Qu???n 3 Tp H??? Ch?? Minh",3500000,30,5,5,10.788639,106.686534));
//            initRentals.add(new Rental(++apaMount, "???????ng S??? 6, Ph?????ng B??nh Tr??ng ????ng, Qu???n 2, Tp H??? Ch?? Minh",2200000,20,3,4,10.785532,106.775883));
//            initRentals.add(new Rental(++apaMount, "88/12, ???????ng B???ch ?????ng, Ph?????ng 24, Qu???n B??nh Th???nh, Tp H??? Ch?? Minh",3000000,25,6,3,10.803285,106.709515));
//            for (Rental rental : initRentals)
//                apartmentDAO.insert(rental);
//            return null;
//        }
//    }
}
