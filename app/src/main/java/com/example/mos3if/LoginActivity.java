package com.example.mos3if;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private ProgressBar progressBar;

    private TextView forgetP;  //forget password
    private Button btn_login, btn_signup;
    private EditText email, password;
    private FirebaseAuth mAuth;
    static String PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);

        linearLayout = findViewById(R.id.linearLayout);
        progressBar = findViewById(R.id.progressBar);
        btn_signup = findViewById(R.id.btn_signup);
        forgetP = findViewById(R.id.forget_Password);
        btn_login = findViewById(R.id.btn_login);
        email = findViewById(R.id.login_email);
        email.setHint("Email address");
        password = findViewById(R.id.login_password);
        password.setHint("Enter your password");



        mAuth = FirebaseAuth.getInstance();

        //Sign up
        btn_signup.setOnClickListener(new View.OnClickListener() {
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
                            editor.commit();

                            //redirect to main activity
                            Intent intent = new Intent(LoginActivity.this,TestActivity.class);
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
}