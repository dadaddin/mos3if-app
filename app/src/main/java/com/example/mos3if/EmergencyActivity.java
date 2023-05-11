package com.example.mos3if;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmergencyActivity extends AppCompatActivity implements AiderAdapter.OnItemClickListener {

    private RecyclerView aidersRv;
    private ArrayList<Contact> aiders;
    private AiderAdapter aiderAdapter;
    private FusedLocationProviderClient fusedLocationClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        aidersRv = findViewById(R.id.rv_aiders);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        aiders = new ArrayList<>();
        aiderAdapter = new AiderAdapter(this,aiders,this);
        getNearbyUsers(1000);

        aidersRv.setAdapter(aiderAdapter);
        aidersRv.setLayoutManager(new LinearLayoutManager(this));
    }



    @Override
    public void onCallClick(Contact contact) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + contact.getPhone()));
        startActivity(intent);
    }

    private void getNearbyUsers(double maxDistanceInMeters) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");

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
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
               if (location != null){
                   Log.e(TAG, "Current location is "+location);
                   double currentUserLatitude = location.getLatitude();
                   Query query = usersRef.orderByChild("location/latitude")
                           .startAt(currentUserLatitude - maxDistanceInMeters / 111320)
                           .endAt(currentUserLatitude + maxDistanceInMeters / 111320);
                   query.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                               if (userSnapshot.child("firstName").getValue() != null) {

                                   Double latitude = userSnapshot.child("location").child("latitude").getValue(Double.class);
                                   Double longitude = userSnapshot.child("location").child("longitude").getValue(Double.class);

                                   if (latitude != null && longitude != null) {
                                       Location nearbyUserLocation = new Location("");
                                       nearbyUserLocation.setLatitude(latitude);
                                       nearbyUserLocation.setLongitude(longitude);
                                       float distance = location.distanceTo(nearbyUserLocation);

                                       if (distance <= maxDistanceInMeters) {
                                           //check if user is available
                                          if (userSnapshot.child("status").getValue().toString().equals(User.Status.AVAILABLE.toString()) ){
                                           // Add user to the list
                                           Contact aider = new Contact(userSnapshot.child("firstName").getValue().toString(),userSnapshot.child("phone").getValue().toString());
                                           aiders.add(aider);
                                           aiderAdapter.notifyDataSetChanged();
                                       }
                                       }
                                   }
                               }
                           }
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {
                           Log.e(TAG, "Database error: " + databaseError.getMessage());
                       }
                   });
               }
               else{ Log.e(TAG, "Failed to get current location");
               Toast.makeText(EmergencyActivity.this, "Unable to retrieve location. Please check your location settings and try again.", Toast.LENGTH_LONG).show();}
            }
        });

    }
}