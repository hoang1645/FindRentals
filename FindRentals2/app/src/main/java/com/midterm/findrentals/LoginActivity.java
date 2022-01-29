package com.midterm.findrentals;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    public static final String LOGIN_TYPE = "login_type_code";
    public static final String FIRST_TIME_LOGIN = "first_time_login";
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private Button btnGoogleAuth;
    private Button btnNoAuth;

    private RentalViewModel viewModel;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        btnGoogleAuth = findViewById(R.id.btn_google_auth);
        btnGoogleAuth.setOnClickListener(v -> logIn());
        btnNoAuth = findViewById(R.id.btn_no_auth);
        btnNoAuth.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(LOGIN_TYPE, "no_auth");
            startActivity(intent);
            finish();
        });

        createRequest();
        mAuth = FirebaseAuth.getInstance();

        viewModel = new ViewModelProvider(this).get(RentalViewModel.class);
        mUser = new User();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        onLoginComplete(currentUser, false);
    }

    private void createRequest() {
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void logIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this, "Google sign in failed",
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, "ApiException:" + e.toString());
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            onLoginComplete(user, true);
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            onLoginComplete(null, false);
                        }
                    }
                });
    }

    private void onLoginComplete(FirebaseUser user, boolean isFirstLogin) {
        if (user != null) {
            /*viewModel.getUser(user, new ThisIsACallback<User>() {
                @Override
                public void onCallback(User value) {
                    mUser = value;
                    Log.d("@@@ user", mUser.toString());
                }
            });*/
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            if (isFirstLogin) intent.putExtra(LOGIN_TYPE, FIRST_TIME_LOGIN);
            startActivity(intent);
            finish();
        }
    }
}