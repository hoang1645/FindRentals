package com.midterm.findrentals;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class RentalSpecific extends RentalSpecificBaseActivity{

    private String ownerName;
    private String ownerTel;

    private TextView ownerNameTV;
    private TextView ownerTelTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Rental specific");
    }

    @Override
    public void setViewById(){
        addressTV = (TextView) findViewById(R.id.rentalSpecificAddress);
        costTV = (TextView) findViewById(R.id.rentalSpecificCost);
        capacityTV = (TextView) findViewById(R.id.rentalSpecificCapacity);
        ownerNameTV = (TextView) findViewById(R.id.rentalSpecificHomeOwnerName);
        ownerTelTV = (TextView) findViewById(R.id.rentalSpecificHomeOwnerTel);
        wrapperLeft = findViewById(R.id.wrapper_left);
        wrapperRight = findViewById(R.id.wrapper_right);
    }

    @Override
    public void setText2TV(){
        super.setText2TV();
        ownerName = getIntent().getStringExtra("owner_name");
        ownerTel = getIntent().getStringExtra("owner_tel");
        if (ownerName != null)
            ownerNameTV.setText(ownerName);
        if (ownerTel != null)
            ownerTelTV.setText(ownerTel);
    }

    @Override
    public void setViewLayout(){
        setContentView(R.layout.activity_rental_specific);
    }

    @Override
    public void setContext(){
        context = getApplicationContext();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rental_specific_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addFavorite:
                add2Favorite();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void add2Favorite() {
        if (localUser == null) {
            Toast.makeText(getApplicationContext(), "Login by Google account to use this service!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        String fav = localUser.getFavorites();
        if (!fav.equals("")){
            fav += ";";
        }
        localUser.setFavorites(fav + rentalId);
        Toast.makeText(getApplicationContext(), "Added to favorite list",
                Toast.LENGTH_LONG).show();
        putUser();
        Log.d("@@@ favorite", localUser.getFavorites());
    }

    private void setTextViewContent(int idTextView, String title, String content) {
        TextView t = (TextView) findViewById(idTextView);
        t.setTextSize(20);
        t.setText(title + ": " + content);
    }

}