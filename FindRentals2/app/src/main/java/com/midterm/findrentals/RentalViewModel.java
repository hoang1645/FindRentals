package com.midterm.findrentals;

import android.app.Application;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.lang.reflect.Array;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;

public class RentalViewModel extends AndroidViewModel {
    public RentalViewModel(@NonNull Application application) {
        super(application);
        rentalCollection = new RentalCollection();
    }



    public class RentalCollection {
        public ArrayList<Rental> allRentals;
        public HashMap<String, User> allCorrelatedUsers;
        public RentalCollection()
        {
            allRentals = new ArrayList<>();
            allCorrelatedUsers = new HashMap<>();
        }
    }

    private RentalCollection rentalCollection;

    //REMEMBER TO CHECK IF THE USER OBJECT RETURNED FROM CALLBACK IS NULL OR NOT.
    public void getUser(FirebaseUser user, ThisIsACallback<User> callback)
    {
        if (user != null)
        {
            FirebaseHelper.getUser(user, task -> {
                if (task.isSuccessful()) {
                    User fromDB = task.getResult().toObject(User.class);
                    callback.onCallback(fromDB);
                }
                else
                {
                    Log.w(FirebaseHelper.TAG, "get failed");
                }
            });
        }
    }

    public void putUser(FirebaseUser user, User localUser)
    {
        FirebaseHelper.putUser(user, localUser);
    }



    public void uploadRental(@NonNull Rental rental, FirebaseUser user)
            throws NoSuchAlgorithmException {
        rental.setHomeownerID(user.getUid());
        String time = Long.toString(System.currentTimeMillis());
        rental.setApartment_id(SimplifiedSHA256HexDigest.hexadecimalDigest(user.getUid() + time));

        FirebaseHelper.putRental(user, rental);
    }

    public void changeRental(Rental rental, FirebaseUser user)
    {
        rental.setHomeownerID(user.getUid());
        FirebaseHelper.putRental(user, rental);
    }

    public void deleteRental(Rental rental, FirebaseUser user)
    {
        rental.setHomeownerID(user.getUid());
        FirebaseHelper.deleteRentalDocument(user, rental, task -> {
            if (task.isSuccessful()) Log.d(FirebaseHelper.TAG, "Update successful");
            else Log.w(FirebaseHelper.TAG, "Update failed");
        });
    }

    public void getRentalByID(String rentalID, ThisIsACallback<Rental> callback)
    {
        FirebaseHelper.getOneRental(rentalID, task -> {
            if (task.isSuccessful())
            {
                ArrayList<Rental> results = (ArrayList<Rental>) task.getResult().toObjects(Rental.class);
                if (results != null && results.size() > 0) {
                    callback.onCallback((Rental)
                            task.getResult().toObjects(Rental.class).get(0));
                    Log.d(FirebaseHelper.TAG, "Object extraction successful");
                }
                else Log.w(FirebaseHelper.TAG, "No objects extracted");
            }
        });
    }

    public void uploadImages(FirebaseUser user, ImageView[] imageViews, Rental rental)
    {
        FirebaseHelper.uploadImage(user, imageViews, rental);
    }

    public void downloadImage(FirebaseUser user, Rental rental, int idx,
                                            ThisIsACallback<byte[]> callback)
    {
        FirebaseHelper.downloadImage(user, rental, idx, task -> {
            if (task.isSuccessful())
                callback.onCallback(task.getResult());
            else Log.w(FirebaseHelper.TAG, "image download failed");
        });
    }

    public void downloadRentals(FirebaseUser user, ThisIsACallback<ArrayList<Rental>> callback) {
        FirebaseHelper.getCollection(user, FirebaseHelper.COLLECTION_RENTALS, Rental.class,
                task -> {

                    if (task.isSuccessful()) {
                        callback.onCallback((ArrayList<Rental>)
                                task.getResult().toObjects(Rental.class));
                        rentalCollection.allRentals = (ArrayList<Rental>)
                                task.getResult().toObjects(Rental.class);
                    }
                }
        );
    }
    public void getAllCorrelatedUsers(FirebaseUser user, List<Rental> rentals,
                                      ThisIsACallback<HashMap<String, User>> callback)
    {
        List<String> allAvailableUsers = new ArrayList<>();
        for (Rental rental : rentals)
            if (!allAvailableUsers.contains(rental.getHomeownerID()))
                allAvailableUsers.add(rental.getHomeownerID());
        FirebaseHelper.getCollection(user, FirebaseHelper.COLLECTION_USERS, User.class,
                task -> {

                    if (task.isSuccessful()) {
                        ArrayList<User> allUsers = (ArrayList<User>)
                                task.getResult().toObjects(User.class);
                        HashMap<String, User> results = new HashMap<>();
                        for (User user1 : allUsers)
                        {
                            if (allAvailableUsers.contains(user1.getUid()))
                                results.put(user1.getUid(), user1);
                        }
                        rentalCollection.allCorrelatedUsers = results;
                        callback.onCallback(results);
                    }
                }
        );
    }
    public ArrayList<Rental> getAllUserRentals(FirebaseUser user, List<Rental> rentals)
    {
        ArrayList<Rental> returnResult = new ArrayList<>();
        for (Rental rental: rentals)
        {
            if (rental.getHomeownerID() == user.getUid())
                returnResult.add(rental);
        }
        return returnResult;
    }
    public User getHomeownerUserInformationFromRental(Rental rental)
    {
        return rentalCollection.allCorrelatedUsers.get(rental.getHomeownerID());
    }
    public void getFavorites(User localUser, FirebaseUser user,
                                          ThisIsACallback<ArrayList<Rental>> callback)
    {
        ArrayList<String> favoritesList = localUser.favoritesAsArray();
        ArrayList<Rental> rentals = new ArrayList<>();
        FirebaseHelper.getCollection(user, FirebaseHelper.COLLECTION_RENTALS, Rental.class,
                task -> {
                    if (task.isSuccessful())
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Rental rental = document.toObject(Rental.class);
                            if (!rentals.contains(rental)
                                    && favoritesList.contains(rental.getApartment_id())) {
                                rentals.add(rental);
                            }
                        }
                    ArrayList<String> newFavoritesList = new ArrayList<>();
                    for (Rental rental : rentals)
                        newFavoritesList.add(rental.getApartment_id());
                    String[] newFavoriteArr = {};
                    newFavoriteArr = newFavoritesList.toArray(newFavoriteArr);
                    localUser.putFavoritesAsArray(newFavoriteArr);
                    callback.onCallback(rentals);
                });
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
