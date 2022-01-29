package com.midterm.findrentals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class UpdateRentalActivity extends AppCompatActivity {

    private RentalViewModel rentalViewModel;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private List<ImageView> images;
    private final int PICK_IMAGE_REQUEST = 22;

    private String rentalId;
    private Rental currentRental;

    private TextView addressTV;
    private TextView costTV;
    private TextView capacityTV;
    private LinearLayout wrapperLeft;
    private LinearLayout wrapperRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_rental);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        rentalViewModel = new ViewModelProvider(this).get(RentalViewModel.class);

        images = new ArrayList<>();

        addressTV = (TextView) findViewById(R.id.rentalAddress);
        costTV = (TextView) findViewById(R.id.rentalCost);
        capacityTV = (TextView) findViewById(R.id.rentalCapacity);
        wrapperLeft = findViewById(R.id.upload_wrapper_left);
        wrapperRight = findViewById(R.id.upload_wrapper_right);

        rentalId = getIntent().getStringExtra("apartment_id");
        if (rentalId != null) {
            Log.d("@@@ id", rentalId);
            loadCurrentRentalFromId(rentalId);
        }
    }

    public void loadCurrentRentalFromId(String id){
        rentalViewModel.getRentalByID(id, new ThisIsACallback<Rental>() {
            @Override
            public void onCallback(Rental value) {
                currentRental = value;
                // download image
                for (int i=0;i<currentRental.getPicsNum();i++){
                    ImageView img = new ImageView(UpdateRentalActivity.this);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.update_rental_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (rentalId == null) {
            menu.findItem(R.id.actionDelete).setEnabled(false);
            menu.findItem(R.id.actionDelete).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionSave:
                saveRental();
                return true;
            case R.id.actionDelete:
                deleteRental();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deleteRental(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Do you really want to delete?");
        builder.setTitle("Alert!");
        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rentalViewModel.deleteRental(currentRental, mUser);
                Toast.makeText(getApplicationContext(), "Rental deleted",
                        Toast.LENGTH_LONG).show();
                UpdateRentalActivity.this.finish();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void saveRental(){
        String address = addressTV.getText().toString();
        int cost = Integer.parseInt(costTV.getText().toString());
        int capacity = Integer.parseInt(capacityTV.getText().toString());
        int picNum = images.size();
        if (rentalId == null) {
            Rental newRental = new Rental("0", address, cost, capacity, "", picNum, 0, 0);
            try {
                rentalViewModel.uploadRental(newRental, mUser);
                ImageView[] imageViewArr = {};
                imageViewArr = images.toArray(imageViewArr);
                rentalViewModel.uploadImages(mUser, imageViewArr, newRental);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        else{
            currentRental.setAddress(address);
            currentRental.setCost(cost);
            currentRental.setCapacity(capacity);
            currentRental.setPicsNum(picNum);
            rentalViewModel.changeRental(currentRental, mUser);

            ImageView[] imageViewArr = {};
            imageViewArr = images.toArray(imageViewArr);
            rentalViewModel.uploadImages(mUser, imageViewArr, currentRental);
        }
        finish();
    }

    public void selectImage(View view) {
        wrapperLeft.removeAllViews();
        wrapperRight.removeAllViews();

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if(data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for(int i = 1; i <= count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i-1).getUri();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        ImageView imageView = new ImageView(this);
                        imageView.setImageBitmap(bitmap);
                        images.add(imageView);
                        putImageToWrapperView(imageView, i);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(data.getData() != null) {
                Uri imageUri = data.getData();
                Bitmap bitmap = null;
                /*try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    ImageView imageView = findViewById(R.id.imgView);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        }

    }

}