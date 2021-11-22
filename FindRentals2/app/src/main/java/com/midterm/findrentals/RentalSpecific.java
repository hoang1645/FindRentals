package com.midterm.findrentals;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RentalSpecific extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rental_specific);
        Intent intent = getIntent();
        String address = intent.getStringExtra("address");
        int cost = intent.getIntExtra("cost", 1000000);
        String homeOwner = intent.getStringExtra("homeOwnerName");
        String tel = intent.getStringExtra("homeOwnerTel");
        int capacity = intent.getIntExtra("capacity", 0);
        int picNum = intent.getIntExtra("picNum", -1);
        int apartment_id = intent.getIntExtra("apartment_id",1);
        setTextViewContent(R.id.rentalSpecificCost, "Cost", String.valueOf(cost) + " VND/month");
        setTextViewContent(R.id.rentalSpecificHomeOwnerName, "Homeowner's name", homeOwner);
        setTextViewContent(R.id.rentalSpecificHomeOwnerTel, "Telephone number", tel);
        setTextViewContent(R.id.rentalSpecificAddress, "Address", address);
        setTextViewContent(R.id.rentalSpecificCapacity, "Capacity", String.valueOf(capacity) + " m2");
        LinearLayout left = findViewById(R.id.wrapper_left);
        LinearLayout right = findViewById(R.id.wrapper_right);
        for(int i=1;i<=picNum;i++){
            ImageView temp = createImageView("house_"+apartment_id+"_"+i);
            if(i%2!=0) left.addView(temp);
            else right.addView(temp);
        }
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