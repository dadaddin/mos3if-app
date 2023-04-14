package com.example.mos3if;

import static android.content.ContentValues.TAG;



import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class TestActivity extends AppCompatActivity {

    Button btn_sos;
    private FusedLocationProviderClient fusedLocationClient;



    private DatabaseReference databaseReference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);



        btn_sos = findViewById(R.id.btn_sos);
        btn_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


        storeToken();
        getLocation();

        getNearbyUsers();
    }

    private void getLocation() {
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

    private void storeLocation(Location location) {
        DatabaseReference locationRef = databaseReference.child(userId);
        GeoLocation geoLocation = new GeoLocation(location.getLatitude(), location.getLongitude());
        locationRef.child("location").setValue(geoLocation);
    }



    private void getNearbyUsers() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }
        Location currentUserLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (currentUserLocation != null) {
            double maxDistanceInMeters = 1000;
            double currentUserLatitude = currentUserLocation.getLatitude();
            double currentUserLongitude = currentUserLocation.getLongitude();

            Query query = usersRef.orderByChild("location/latitude")
                    .startAt(currentUserLatitude - maxDistanceInMeters / 111320)
                    .endAt(currentUserLatitude + maxDistanceInMeters / 111320);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        Double latitude = userSnapshot.child("location").child("latitude").getValue(Double.class);
                        Double longitude = userSnapshot.child("location").child("longitude").getValue(Double.class);

                        if (latitude != null && longitude != null) {
                            Location nearbyUserLocation = new Location("");
                            nearbyUserLocation.setLatitude(latitude);
                            nearbyUserLocation.setLongitude(longitude);
                            float distance = currentUserLocation.distanceTo(nearbyUserLocation);


                            if (distance <= maxDistanceInMeters) {
                                // Send notification to nearby user
                                Log.e(TAG,"user found  "+userSnapshot.child("firstName").getValue(String.class));
                                String deviceToken = userSnapshot.child("deviceToken").getValue().toString();
                                sendNotification(deviceToken);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e(TAG, "Database error: " + databaseError.getMessage());
                }
            });
        } else {
            Log.e(TAG, "Failed to get current location");
        }
    }
    private void storeToken(){
        FirebaseMessaging.getInstance().getToken()
            .addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();
                    //store token in database

                    // Get the current user ID
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    // Save the device token to the Firebase Realtime Database
                    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                    database.child("Users").child(userId).child("deviceToken").setValue(token);

                }
            });
    }
    private void sendNotification(String deviceToken) {
        // Create the message payload
        Map<String, String> messagePayload = new HashMap<>();
        messagePayload.put("title", "Help needed");
        messagePayload.put("message", "A person nearby needs your help !");

        // Create the RemoteMessage builder
        RemoteMessage.Builder builder = new RemoteMessage.Builder(deviceToken);
        builder.setData(messagePayload);

        // Send the message using the FirebaseMessaging service
        FirebaseMessaging.getInstance().send(builder.build());

    }


}






