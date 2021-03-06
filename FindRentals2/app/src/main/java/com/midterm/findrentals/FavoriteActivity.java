package com.midterm.findrentals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    private List<Rental> rentals;
    private RecyclerView rcvRentals;
    private RentalRecyclerAdapter recyclerAdapter;
    private static final int SPEECH_REQUEST_CODE = 0;
    private RentalViewModel rentalViewModel;
    private SearchView searchView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private User localUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        this.setTitle("Favorite rentals");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        rentalViewModel = new ViewModelProvider(this).get(RentalViewModel.class);

        loadRentalsFromViewModel();
        initializeViews();
    }

    public void loadRentalsFromViewModel(){
        if (mUser == null){
            Toast.makeText(getApplicationContext(), "Login by Google account to use this service!",
                    Toast.LENGTH_LONG).show();
            FavoriteActivity.this.finish();
            return;
        }
        rentalViewModel.getUser(mUser, new ThisIsACallback<User>() {
            @Override
            public void onCallback(User value) {
                localUser = value;
                if (localUser == null) {
                    Toast.makeText(getApplicationContext(), "Login by Google account to use this service!",
                            Toast.LENGTH_LONG).show();
                    FavoriteActivity.this.finish();
                    return;
                }

                rentalViewModel.getFavorites(localUser, mUser, new ThisIsACallback<ArrayList<Rental>>() {
                    @Override
                    public void onCallback(ArrayList<Rental> value) {
                        rentals = value;
                        initializeViews();
                    }
                });
                Log.d("@@@ favorite", localUser.getFavorites());
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionVoiceSearch:
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                startActivityForResult(intent, SPEECH_REQUEST_CODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            CharSequence spokenText = results.get(0);
            searchView.setQuery(spokenText,true);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private List<Rental> filter(String text) {
        List<Rental> filteredList = new ArrayList<>();
        for (Rental item : rentals) {
            if (item.getAddress().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rental_view_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                recyclerAdapter.filterList(filter(s));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                recyclerAdapter.filterList(filter(s));
                return false;
            }
        });
        return true;
    }

    private void initializeViews() {
        rcvRentals=findViewById(R.id.rcvFavorite);
        if (rcvRentals!=null){
            recyclerAdapter = new RentalRecyclerAdapter(this, rentals, null,
                    recyclerAdapter.FAVORITE_RENTALS, rentalViewModel, mUser, localUser);
            rcvRentals.setAdapter(recyclerAdapter);
            rcvRentals.setLayoutManager(new LinearLayoutManager(this));
            rcvRentals.addItemDecoration(new SimpleDividerItemDecoration(this));
        }
    }
}