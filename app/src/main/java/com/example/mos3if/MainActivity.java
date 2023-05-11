package com.example.mos3if;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser user;
    private String userID;
    private DatabaseReference reference;
    static BottomNavigationView bottomNavigationView;
    static HomeFragment homeFragment = new HomeFragment();
    static FirstAidFragment firstAidFragment = new FirstAidFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //updating user location to the database
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
         reference.child(userID).addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 User userProfile = snapshot.getValue(User.class);
                 //check if current user is registered in app
                 if (userProfile!= null){
                     getLocation();
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.getMenu().findItem(R.id.item_home).setChecked(true);

        getSupportFragmentManager().beginTransaction().remove(homeFragment).commit();
        getSupportFragmentManager().beginTransaction().remove(firstAidFragment).commit();
        getSupportFragmentManager().beginTransaction().remove(settingsFragment).commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container , homeFragment)
                .add(R.id.fragment_container , firstAidFragment)
                .add(R.id.fragment_container , settingsFragment)
                .commit();

        setTabStateFragment(TabState.HOME).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_home:
                        setTabStateFragment(TabState.HOME).commit();
                        return true;
                    case R.id.item_firstaid:
                        setTabStateFragment(TabState.FIRSTAID).commit();
                        return true;
                    case R.id.item_settings:
                        setTabStateFragment(TabState.SETTINGS).commit();
                        return true;
                }
                return false;
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0 || !homeFragment.isHidden()) {
            super.onBackPressed();
        } else {
            setTabStateFragment(TabState.HOME).commit();
            bottomNavigationView.getMenu().findItem(R.id.item_home).setChecked(true);
        }
    }


    public FragmentTransaction setTabStateFragment(TabState state){
        getSupportFragmentManager().popBackStack(null , FragmentManager.POP_BACK_STACK_INCLUSIVE);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (state){
            case HOME:
                transaction.show(homeFragment);
                transaction.hide(firstAidFragment);
                transaction.hide(settingsFragment);
                break;
            case FIRSTAID:
                transaction.show(firstAidFragment);
                transaction.hide(homeFragment);
                transaction.hide(settingsFragment);
                break;
            case SETTINGS:
                transaction.show(settingsFragment);
                transaction.hide(firstAidFragment);
                transaction.hide(homeFragment);
                break;
        }
        return transaction;
    }

    protected  void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        }


        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Store location in Realtime Database
                    storeLocation(location);

                }
            }
        });
    }

    protected  void storeLocation(Location location) {
        DatabaseReference locationRef = reference.child(userID);
        GeoLocation geoLocation = new GeoLocation(location.getLatitude(), location.getLongitude());
        locationRef.child("location").setValue(geoLocation);
    }
    enum  TabState {
        HOME,
        FIRSTAID,
        SETTINGS,
    }

}