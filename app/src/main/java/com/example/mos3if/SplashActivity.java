package com.example.mos3if;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    Runnable runnable;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        runnable = new Runnable() {
            @Override
            public void run() {

                SharedPreferences welcome = getSharedPreferences("welcome",MODE_PRIVATE);
                boolean isFirstTime = welcome.getBoolean("firstTime", true);

                if (isFirstTime) {
                    SharedPreferences.Editor editor = welcome.edit();
                    editor.putBoolean("firstTime",false);
                    editor.commit();
                    startActivity(new Intent(getApplicationContext(), OnboardingActivity.class));
                    finish();
                }else{
                    SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFS_NAME,0);
                    boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn",false);
                    if (hasLoggedIn){
                           startActivity(new Intent(SplashActivity.this,TestActivity.class));
                           finish();
                    } else {
                           startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
                           finish();
                    }
                }

            }
        };

        handler =new Handler();
        handler.postDelayed(runnable,2000 );
    }
}