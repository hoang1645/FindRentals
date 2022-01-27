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

    public static <T> void putDocument(FirebaseUser user, T object, String collection) {
        if (user == null) return;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_USERS).document(user.getUid())
                .collection(collection)
                .add(object)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) Log.d(TAG, collection + " successfully uploaded");
                    else Log.w(TAG, "Error uploading");
                });
    }

    public static <T> void getCollection(FirebaseUser user, String collection, Class<T> type,
                                         OnCompleteListener<QuerySnapshot> onCompleteListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(COLLECTION_USERS).document(user.getUid())
                .collection(collection)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    public static <T> void putCollection(FirebaseUser user, String collection, ArrayList<T> objects,
                                         OnCompleteListener<Void> onCompleteListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection(COLLECTION_USERS).document(user.getUid())
                .collection(collection);
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