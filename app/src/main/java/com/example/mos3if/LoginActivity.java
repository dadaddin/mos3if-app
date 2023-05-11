package com.example.mos3if;

import static android.content.ContentValues.TAG;
import static com.example.mos3if.SettingsFragment.PREFS_FILE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginActivity extends AppCompatActivity {


    private ProgressBar progressBar;

    private TextView forgetP , signup;
    private Button btn_login;
    private EditText email, password;
    private FirebaseAuth mAuth;
    static String PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);

        progressBar = findViewById(R.id.progressBar);
        signup = findViewById(R.id.tv_signup);
        forgetP = findViewById(R.id.forget_Password);
        btn_login = findViewById(R.id.btn_login);
        email = findViewById(R.id.login_email);
        email.setHint("Email address");
        password = findViewById(R.id.login_password);
        password.setHint("Enter your password");



        mAuth = FirebaseAuth.getInstance();

        //Sign up
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailTxt = email.getText().toString();
                String passwordTxt = password.getText().toString();

                if (emailTxt.isEmpty()){
                    email.setError("Email Address is required");
                    email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches()){
                    email.setError("Please provide a valid email");
                    email.requestFocus();
                    return;
                }
                if (passwordTxt.isEmpty()){
                    password.setError("Password is required");
                    password.requestFocus();
                    return;
                }
                if (passwordTxt.length() < 6){
                    password.setError("Min password length should be 6 characters ");
                    password.requestFocus();
                    return;
                }
                progressBar.setVisibility(view.VISIBLE);

                mAuth.signInWithEmailAndPassword(emailTxt,passwordTxt).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressBar.setVisibility(View.GONE);

                            //saving login info
                            SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME,0);
                            SharedPreferences.Editor editor =sharedPreferences.edit();
                            editor.putBoolean("hasLoggedIn",true);
                            editor.apply();

                            //Clearing previous saved info
                            SharedPreferences sharedPreferences1 = getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor1 =sharedPreferences1.edit();
                            editor1.clear();
                            editor1.apply();

                            //Store Device Token in Firebase
                            storeToken();
                            // Store location in Realtime Database
                            getLocation();

                            //redirect to main activity
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this,"Failed to login! Please check your information!",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });
    }

    protected static void storeToken(){
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
        DatabaseReference locationRef = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        GeoLocation geoLocation = new GeoLocation(location.getLatitude(), location.getLongitude());
        locationRef.child("location").setValue(geoLocation);
    }
}