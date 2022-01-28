package com.midterm.findrentals;


import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseHelper {
    public static final String TAG = FirebaseHelper.class.getName();
    public static final String COLLECTION_USERS = "users";
    public static final String COLLECTION_RENTALS = "rentals";

    public static void putUser(FirebaseUser user, User user_local) {
        if (user == null) return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_USERS).document(user.getUid()).set(user_local)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "User info successfully uploaded");
                    else Log.w(TAG, "Error uploading User info");
                });
    }

    public static void getUser(FirebaseUser user,
                               OnCompleteListener<DocumentSnapshot> onCompleteListener) {
        if (user == null) return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_USERS).document(user.getUid())
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    public static void changeRentalDocument(FirebaseUser user, Rental rental,
                                            OnCompleteListener<Void> onCompleteListener) {
        if (user == null) return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference rentalReference = db.collection(COLLECTION_RENTALS);
        WriteBatch batch = db.batch();
        rentalReference.get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot doc : task.getResult())
                        {
                            Rental inData = doc.toObject(Rental.class);
                            if (inData.getApartment_id() == rental.getApartment_id())
                            {
                                batch.delete(doc.getReference());
                                putRental(user, rental);
                                break;
                            }
                        }
                        batch.commit().addOnCompleteListener(onCompleteListener);
                    }
                    else Log.w(TAG, "Replacement failed");
                }
        );
    }
    public static void changeUserDocument(FirebaseUser user, User currentUser,
                                            OnCompleteListener<Void> onCompleteListener) {
        if (user == null) return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userReference = db.collection(COLLECTION_USERS);
        WriteBatch batch = db.batch();
        userReference.get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot doc : task.getResult())
                        {
                            User inData = doc.toObject(User.class);
                            if (inData.getUid() == currentUser.getUid())
                            {
                                batch.delete(doc.getReference());
                                putUser(user, currentUser);
                                break;
                            }
                        }
                        batch.commit().addOnCompleteListener(onCompleteListener);
                    }
                    else Log.w(TAG, "Replacement failed");
                }
        );
    }
    public static void deleteRentalDocument(FirebaseUser user, Rental rental,
                                            OnCompleteListener<Void> onCompleteListener)
    {
        if (user == null) return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference rentalReference = db.collection(COLLECTION_RENTALS);
        WriteBatch batch = db.batch();
        rentalReference.get().addOnCompleteListener(
                task -> {
                    if (task.isSuccessful())
                    {
                        for (QueryDocumentSnapshot doc : task.getResult())
                        {
                            Rental inData = doc.toObject(Rental.class);
                            if (inData.getApartment_id() == rental.getApartment_id())
                            {
                                batch.delete(doc.getReference());
                                break;
                            }
                        }
                        batch.commit().addOnCompleteListener(onCompleteListener);
                    }
                    else Log.w(TAG, "Replacement failed");
                }
        );
    }

    public static <T> void putDocument(FirebaseUser user, T object, String collection) {
        if (user == null) return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        db.collection(collection)
//                .add(object)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) Log.d(TAG, collection + " successfully uploaded");
//                    else Log.w(TAG, "Error uploading");
//                });
    }

    public static void putRental(FirebaseUser user, Rental rental)
    {
        if (user == null) return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_RENTALS).document(rental.getApartment_id()).set(rental)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, "Rental successfully uploaded");
                    else Log.w(TAG, "Error uploading rental");
                });
    }


    public static <T> void getCollection(FirebaseUser user, String collection, Class<T> type,
                                         OnCompleteListener<QuerySnapshot> onCompleteListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collection)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    public static <T> void putCollection(FirebaseUser user, String collection, ArrayList<T> objects,
                                         OnCompleteListener<Void> onCompleteListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection(collection);
        WriteBatch batch = db.batch();
        collectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Clear collection before upload
                for (QueryDocumentSnapshot document : task.getResult()) {
                    batch.delete(document.getReference());
                }
                // Upload new objects
                for (T object : objects) {
                    DocumentReference documentRef = collectionRef.document();
                    batch.set(documentRef, object);
                }
                batch.commit().addOnCompleteListener(onCompleteListener);
            } else Log.d(TAG, "Error getting documents: ", task.getException());
        });
    }
}