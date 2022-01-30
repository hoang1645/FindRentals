package com.midterm.findrentals;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ortiz.touchview.TouchImageView;

public abstract class RentalSpecificBaseActivity extends AppCompatActivity  implements View.OnClickListener {

    protected RentalViewModel rentalViewModel;
    protected FirebaseAuth mAuth;
    protected FirebaseUser mUser;
    protected Context context;

    protected String rentalId;
    protected Rental currentRental;
    protected User localUser;

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

        rentalViewModel.getUser(mUser, new ThisIsACallback<User>() {
            @Override
            public void onCallback(User value) {
                localUser = value;
            }
        });

        rentalId = getIntent().getStringExtra("apartment_id");
        if (rentalId != null) {
            Log.d("@@@ id", rentalId);
            loadCurrentRentalFromId(rentalId);
        }

    }

    public abstract void setViewLayout();
    public abstract void setViewById();
    public abstract void setContext();

    public void putUser(){
        if (localUser == null){
            localUser = new User(mUser.getUid(), mUser.getDisplayName(),
                    mUser.getEmail(), mUser.getPhoneNumber(), "");
        }
        rentalViewModel.putUser(mUser, localUser);
    }

    public void loadCurrentRentalFromId(String id){
        rentalViewModel.getRentalByID(id, new ThisIsACallback<Rental>() {
            @Override
            public void onCallback(Rental value) {
                currentRental = value;
                // download image
                for (int i=0;i<currentRental.getPicsNum();i++){
                    ImageView img = new TouchImageView(context);
                    rentalViewModel.downloadImage(mUser, currentRental, i, new ThisIsACallback<byte[]>() {
                        @Override
                        public void onCallback(byte[] value) {
                            setImageViewWithByteArray(img, value);
                        }
                    });
                    putImageToWrapperView(img, i+1);
                }
                setText2TV();
            }
        });
    }

    public void setText2TV(){
        addressTV.setText(currentRental.getAddress());
        costTV.setText(Integer.toString(currentRental.getCost()));
        capacityTV.setText(Integer.toString(currentRental.getCapacity()));
    }

    public void setImageViewWithByteArray(ImageView view, byte[] data) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        view.setImageBitmap(bitmap);
    }


    public void putImageToWrapperView(ImageView img, int i){
        img.setScaleType(ImageView.ScaleType.CENTER);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.setMargins(0,0,0,20);
        img.setLayoutParams(p);
        img.setOnClickListener(this);
        if (i%2!=0) wrapperLeft.addView(img);
        else wrapperRight.addView(img);
    }

    @Override
    public void onClick(View view) {
        Dialog settingsDialog = new Dialog(this, R.style.CustomDialog);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.specific_rental_img
                , null));
        ImageView img = settingsDialog.findViewById(R.id.img_modal);
        Bitmap bitmap=((BitmapDrawable)(((ImageView)view).getDrawable())).getBitmap();
        img.setImageBitmap(bitmap);
        settingsDialog.show();
    }

}
