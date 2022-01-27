package com.midterm.findrentals;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;

public class RentalViewModel extends AndroidViewModel {
    private ArrayList<Rental> allRentals;
    private HashMap<String, User> allCorrelatedUsers;

    public RentalViewModel(@NonNull Application application) {
        super(application);
    }

    public void uploadRental(@NonNull Rental rental, FirebaseUser user)
            throws NoSuchAlgorithmException {
        rental.setHomeownerID(user.getUid());
        String time = Long.toString(System.currentTimeMillis());
        rental.setRentalID(SimplifiedSHA256HexDigest.hexadecimalDigest(user.getUid() + time));

        FirebaseHelper.putDocument(user, rental, FirebaseHelper.COLLECTION_RENTALS);
    }

    public void changeRental(Rental rental, FirebaseUser user)
    {
        rental.setHomeownerID(user.getUid());
        FirebaseHelper.changeRentalDocument(user, rental, task -> {
            if (task.isSuccessful()) Log.d(FirebaseHelper.TAG, "Update successful");
            else Log.w(FirebaseHelper.TAG, "Update failed");
        });
    }

    public void deleteRental(Rental rental, FirebaseUser user)
    {
        rental.setHomeownerID(user.getUid());
        FirebaseHelper.deleteRentalDocument(user, rental, task -> {
            if (task.isSuccessful()) Log.d(FirebaseHelper.TAG, "Update successful");
            else Log.w(FirebaseHelper.TAG, "Update failed");
        });
    }


    public void downloadRentals(FirebaseUser user) {
        allRentals = null;
        allCorrelatedUsers = null;
        ArrayList<String> userIDList = new ArrayList<>();
        FirebaseHelper.getCollection(user, FirebaseHelper.COLLECTION_RENTALS, Rental.class,
                task -> {
                    if (task.isSuccessful())
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Rental rental = document.toObject(Rental.class);
                            if (!allRentals.contains(rental) && rental.available) {
                                allRentals.add(rental);
                                if (!userIDList.contains(rental.getHomeownerID()))
                                    userIDList.add(rental.getHomeownerID());
                            }
                        }
                });

        FirebaseHelper.getCollection(user, FirebaseHelper.COLLECTION_USERS, User.class,
                task -> {
                    if (task.isSuccessful())
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user1 = document.toObject(User.class);
                            if (userIDList.contains(user1.getUid()))
                                allCorrelatedUsers.put(user1.getUid(), user1);
                        }
                });
    }
    public ArrayList<Rental> getAllRentals()
    {
        return allRentals;
    }
    public HashMap<String, User> getAllCorrelatedUsers()
    {
        return allCorrelatedUsers;
    }
    public ArrayList<Rental> getAllUserRentals(FirebaseUser user)
    {
        ArrayList<Rental> returnResult = new ArrayList<>();
        for (Rental rental: allRentals)
        {
            if (rental.getHomeownerID() == user.getUid())
                returnResult.add(rental);
        }
        return returnResult;
    }
    public User getHomeownerUserInformationFromRental(Rental rental)
    {
        return allCorrelatedUsers.get(rental.getHomeownerID());
    }
    //
//
//    public LiveData<List<Rental>> getAllRentals()
//    {
//        return allRentals;
//    }
//
//    public void deleteRental(Rental rental)
//    {
//        repository.deleteRental(rental);
//        allRentals = repository.getAllRentals();
//    }
//    public LiveData<List<Homeowner>> getAllHomeowners()
//    {
//        return allHomeowners;
//    }
//    public List<Homeowner> getHomeowner(String id)
//    {
//        return repository.getHomeowner(id);
//    }
//    public List<Homeowner> getHomeowner(Rental rental)
//    {
//        return repository.getHomeowner(rental);
//    }
//    public void insert(Rental rental)
//    {
//        repository.insert(rental);
//    }
//    public void insert(Homeowner homeowner)
//    {
//        repository.insert(homeowner);
//    }
//    public LiveData<List<Rental>> getRentalsByAddress(String addr)
//    {
//        return repository.getRentalsByAddress(addr);
//    }
//    public LiveData<List<Rental>> getRentalsSortedByCost(String sort)
//    {
//        return repository.getRentalsSortedByCost(sort);
//    }
//    public LiveData<List<Rental>> getRentalsSortedByCapacity(String sort)
//    {
//        return repository.getRentalsSortedByCapacity(sort);
//    }
//    public LiveData<List<Rental>> findRentalsByCost(int cost, boolean upper)
//    {
//        return repository.findRentalsByCost(cost, upper);
//    }
//    public LiveData<List<Rental>> findRentalsByCapacity(int cap, int capp, boolean upper) {
//        return repository.findRentalsByCapacity(cap, capp, upper);
//    }
}
