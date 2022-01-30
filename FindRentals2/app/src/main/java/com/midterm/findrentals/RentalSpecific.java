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

public class RentalSpecific extends RentalSpecificBaseActivity implements View.OnClickListener {

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

    ImageView createImageView(String name){
        ImageView img = new ImageView(this);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        int drawableId = this.getResources().getIdentifier(name, "drawable", this.getPackageName());
        img.setImageResource(drawableId);
        img.setAdjustViewBounds(true);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.setMargins(0,0,0,20);
        img.setLayoutParams(p);
        img.setContentDescription(name);
        img.setOnClickListener(this);
        return img;
    }

    private void setTextViewContent(int idTextView, String title, String content) {
        TextView t = (TextView) findViewById(idTextView);
        t.setTextSize(20);
        t.setText(title + ": " + content);
    }


    @Override
    public void onClick(View view) {
        Dialog settingsDialog = new Dialog(this);
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.specific_rental_img
                , null));
        ImageView img = (ImageView) settingsDialog.findViewById(R.id.img_modal);
        int drawableId = this.getResources().getIdentifier(String.valueOf(view.getContentDescription()), "drawable", this.getPackageName());
        img.setImageResource(drawableId);
        settingsDialog.show();
    }
}