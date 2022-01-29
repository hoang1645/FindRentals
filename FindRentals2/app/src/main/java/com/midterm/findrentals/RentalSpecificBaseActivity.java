package com.midterm.findrentals;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public abstract class RentalSpecificBaseActivity extends AppCompatActivity {

    protected RentalViewModel rentalViewModel;
    protected FirebaseAuth mAuth;
    protected FirebaseUser mUser;
    protected Context context;

    protected String rentalId;
    protected Rental currentRental;

    protected TextView addressTV;
    protected TextView costTV;
    protected TextView capacityTV;
    protected LinearLayout wrapperLeft;
    protected LinearLayout wrapperRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setViewLayout();
        setViewById();
        setContext();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        rentalViewModel = new ViewModelProvider(this).get(RentalViewModel.class);

        rentalId = getIntent().getStringExtra("apartment_id");
        if (rentalId != null) {
            Log.d("@@@ id", rentalId);
            loadCurrentRentalFromId(rentalId);
        }

    }

    public abstract void setViewLayout();
    public abstract void setViewById();
    public abstract void setContext();

    public void loadCurrentRentalFromId(String id){
        rentalViewModel.getRentalByID(id, new ThisIsACallback<Rental>() {
            @Override
            public void onCallback(Rental value) {
                currentRental = value;
                // download image
                for (int i=0;i<currentRental.getPicsNum();i++){
                    ImageView img = new ImageView(context);
                    rentalViewModel.downloadImage(mUser, currentRental, i, new ThisIsACallback<byte[]>() {
                        @Override
                        public void onCallback(byte[] value) {
                            setImageViewWithByteArray(img, value);
                        }
                    });
                    putImageToWrapperView(img, i+1);
                }
                addressTV.setText(currentRental.getAddress());
                costTV.setText(Integer.toString(currentRental.getCost()));
                capacityTV.setText(Integer.toString(currentRental.getCapacity()));
            }
        });
    }

    public void setImageViewWithByteArray(ImageView view, byte[] data) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        view.setImageBitmap(bitmap);
    }

    public void putImageToWrapperView(ImageView img, int i){
        if (i%2!=0) wrapperLeft.addView(img);
        else wrapperRight.addView(img);
    }

}
