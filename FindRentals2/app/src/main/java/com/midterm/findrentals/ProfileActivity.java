package com.midterm.findrentals;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private RentalViewModel viewModel;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private User localUser;

    private ImageView avatarView;
    private TextView emailView;
    private EditText nameView;
    private EditText telView;

    private String name;
    private String tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.setTitle("Profile");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        viewModel = new ViewModelProvider(this).get(RentalViewModel.class);

        avatarView = findViewById(R.id.AvatarImageView);
        emailView = findViewById(R.id.EmailTextView);
        nameView = findViewById(R.id.NameEditText);
        telView = findViewById(R.id.TelEditText);

        viewModel.getUser(mUser, new ThisIsACallback<User>() {
            @Override
            public void onCallback(User value) {
                localUser = value;
                name = localUser.getName();
                tel = localUser.getTel();
                setView();
            }
        });
    }

    public void setView(){
        setAvatar();
        emailView.setText(localUser.getEmail());
        nameView.setText(name);
        telView.setText(tel);
    }

    public void setAvatar() {
        Drawable defaultAvatar = getResources().getDrawable(R.drawable.profile);
        if (mUser != null) {
            Uri photoUri = mUser.getPhotoUrl();
            if (photoUri != null)
                Glide.with(this).load(photoUri.toString()).into(avatarView);
            else
                avatarView.setImageDrawable(defaultAvatar);
        }
        else
            avatarView.setImageDrawable(defaultAvatar);
    }

    public void updateInfo(View view){
        name = nameView.getText().toString();
        tel = telView.getText().toString();
        localUser.setName(name);
        localUser.setTel(tel);
        if (mUser != null)
            viewModel.putUser(mUser, localUser);
        finish();
    }

    public void logOut(View view){
        AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(task -> backToLoginScreen());
    }

    public void backToLoginScreen() {
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}