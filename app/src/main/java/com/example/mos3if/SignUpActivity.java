package com.example.mos3if;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private EditText email,firstName,lastName, password;
    private Button btn_signUp;
    private TextView loginTv;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);


        loginTv = findViewById(R.id.tvlogin);
        btn_signUp = findViewById(R.id.sign_up);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);

        firstName = findViewById(R.id.signup_firstName);
        lastName = findViewById(R.id.signup_lastName);
        progressBar = findViewById(R.id.progressBar);



        firstName.setHint("First Name");
        lastName.setHint("Last Name");
        email.setHint("Email Address");
        password.setHint("Password");


        mAuth = FirebaseAuth.getInstance();

        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            }
        });

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstNameTxt = firstName.getText().toString();
                String lastNameTxt = lastName.getText().toString();
                String emailTxt = email.getText().toString();
                String passwordTxt = password.getText().toString();

                if (firstNameTxt.isEmpty()){
                    firstName.setError("First name is required");
                    firstName.requestFocus();
                    return;
                }
                if (lastNameTxt.isEmpty()){
                    lastName.setError("Last name is required");
                    lastName.requestFocus();
                    return;
                }
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

                mAuth.createUserWithEmailAndPassword(emailTxt,passwordTxt)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    User user = new User(firstNameTxt,lastNameTxt,emailTxt);
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(SignUpActivity.this,"User has been registered successfully",Toast.LENGTH_LONG).show();
                                                        progressBar.setVisibility(View.GONE);

                                                        //saving login info
                                                        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME,0);
                                                        SharedPreferences.Editor editor =sharedPreferences.edit();
                                                        editor.putBoolean("hasLoggedIn",true);
                                                        editor.commit();

                                                        //redirect to Main activity
                                                        Intent intent = new Intent(SignUpActivity.this,TestActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }else{
                                                        Toast.makeText(SignUpActivity.this,"Failed to register! Try again!",Toast.LENGTH_LONG).show();
                                                        progressBar.setVisibility(View.GONE);
                                                    }
                                                }
                                            });

                                }else{
                                    Toast.makeText(SignUpActivity.this,"Failed to register! Try again!",Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                }

                            }
                        });


            }
        });
    }
}