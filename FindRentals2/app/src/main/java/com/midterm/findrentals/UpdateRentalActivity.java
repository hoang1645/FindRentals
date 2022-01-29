package com.midterm.findrentals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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

    private int rentalId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_rental);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        rentalViewModel = new ViewModelProvider(this).get(RentalViewModel.class);

        images = new ArrayList<>();

        rentalId = (int)getIntent().getIntExtra("apartment_id", -1);
        if (rentalId != -1) {
            // load info rental from rentalId
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.update_rental_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
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

    }

    public void saveRental(){
        String address = ((TextView) findViewById(R.id.rentalAddress)).getText().toString();
        int cost = Integer.parseInt(((TextView) findViewById(R.id.rentalCost)).getText().toString());
        int capacity = Integer.parseInt(((TextView) findViewById(R.id.rentalCapacity)).getText().toString());
        int picNum = images.size();
        Rental newRental = new Rental("0", address, cost, capacity, "", picNum, 0, 0);
        try {
            Log.d("@@@: ", "before upload");
            rentalViewModel.uploadRental(newRental, mUser);
            ImageView[] imageViewArr = {};
            imageViewArr = images.toArray(imageViewArr);
            Log.d("@@@ imageViewArr", Integer.toString(imageViewArr.length));
            Log.d("@@@ newRental", newRental.toString());
            rentalViewModel.uploadImages(mUser, imageViewArr, newRental);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Log.d("@@@: ", "finish");
        finish();
    }

    public void selectImage(View view) {
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
            Log.d("@@@", data.getClipData().toString());
            if(data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for(int i = 1; i <= count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i-1).getUri();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        LinearLayout left = findViewById(R.id.upload_wrapper_left);
                        LinearLayout right = findViewById(R.id.upload_wrapper_right);
                        ImageView imageView = new ImageView(this);
                        imageView.setImageBitmap(bitmap);
                        Log.d("@@@", imageView.toString());
                        if (i%2!=0) left.addView(imageView);
                        else right.addView(imageView);
                        images.add(imageView);
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