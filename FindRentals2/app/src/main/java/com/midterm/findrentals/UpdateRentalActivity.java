package com.midterm.findrentals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class UpdateRentalActivity extends AppCompatActivity {

    private RentalViewModel rentalViewModel;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private ImageView imageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_rental);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        rentalViewModel = new ViewModelProvider(this).get(RentalViewModel.class);

        imageView = findViewById(R.id.imgView);
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
        Rental newRental = new Rental("0", address, cost, capacity, "", 0, 0, 0);
        Log.d("@@@ newRental: ", newRental.getAddress());
        Log.d("@@@ newRental: ", Integer.toString(newRental.getCost()));
        Log.d("@@@ newRental: ", Integer.toString(newRental.getCapacity()));
        try {
            Log.d("@@@: ", "before upload");
            rentalViewModel.uploadRental(newRental, mUser);
        } catch (NoSuchAlgorithmException e) {
            Log.d("@@@: ", "cant upload");
            e.printStackTrace();
        }
        Log.d("@@@: ", "finish");
        finish();
    }

    public void selectImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}