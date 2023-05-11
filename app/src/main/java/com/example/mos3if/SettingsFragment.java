package com.example.mos3if;

import static com.example.mos3if.LoginActivity.PREFS_NAME;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SettingsFragment extends Fragment {

    static String PREFS_FILE = "MyPrefsFile";
    private boolean anonymous;
    private FirebaseUser user;
    private String userID;
    private DatabaseReference reference;
    private TextView displayName;
    private TextView displayStatus;
    private Button btnSwitch;
    private LinearLayout loginLayout;
    private LinearLayout logoutLayout;
    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        displayName = v.findViewById(R.id.tv_fullName);
        displayStatus = v.findViewById(R.id.tv_email);
        btnSwitch = v.findViewById(R.id.btn_switch);
        loginLayout = v.findViewById(R.id.loginLayout);
        logoutLayout = v.findViewById(R.id.logoutLayout);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(LoginActivity.PREFS_NAME,0);
        anonymous = sharedPreferences.getBoolean("anonymousUser",false);
        if (anonymous){
            setGuestLayout();
        }else {
            SharedPreferences sharedPreferences1 = getContext().getSharedPreferences(PREFS_FILE,0);
            String nameTxt = sharedPreferences1.getString("userName","");
            String statusTxt = sharedPreferences1.getString("userStatus","");

            if (nameTxt.equals("")){
                reference.child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User userProfile = snapshot.getValue(User.class);
                        if (userProfile!= null){
                            displayName.setText(userProfile.firstName);
                            setUserStatus(userProfile.status.toString());

                            SharedPreferences.Editor editor = sharedPreferences1.edit();
                            editor.putString("userName",userProfile.firstName);
                            editor.putString("userStatus",userProfile.status.toString());
                            editor.apply();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }else{
                displayName.setText(nameTxt);
                setUserStatus(statusTxt);
            }
        }

        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences1 = getContext().getSharedPreferences(PREFS_FILE,0);
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                if (displayStatus.getText().toString().equals("Available")){
                    reference.child(userID).child("status").setValue(User.Status.UNAVAILABLE);
                    editor.putString("userStatus","UNAVAILABLE");
                    editor.apply();
                }else{
                    reference.child(userID).child("status").setValue(User.Status.AVAILABLE);
                    editor.putString("userStatus","AVAILABLE");
                    editor.apply();
                }
            }
        });

        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to Log out ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Sign Out from the app
                                FirebaseAuth.getInstance().signOut();
                                signOutUser();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog

                            }
                        });
                builder.create().show();
            }
        });

        loginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to Login ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(getActivity(),LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog

                            }
                        });
                builder.create().show();

            }
        });


        return v;
    }

    private void setGuestLayout() {

        displayName.setText("Guest");
        displayStatus.setText("You are in Guest Mode!");
        btnSwitch.setVisibility(View.GONE);
        logoutLayout.setVisibility(View.GONE);
        loginLayout.setVisibility(View.VISIBLE);

    }

    private void setUserStatus(String status){
        if (status.equals("AVAILABLE")){
            displayStatus.setText("Available");
            displayStatus.setTextColor(getResources().getColor(R.color.green));
        }else {
            displayStatus.setText("Unavailable");
            displayStatus.setTextColor(getResources().getColor(R.color.primary_color));
        }
    }

    //methode sign out
    private void signOutUser() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(getActivity(),MainScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}