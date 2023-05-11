package com.example.mos3if;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    static BottomNavigationView bottomNavigationView;
    static HomeFragment homeFragment = new HomeFragment();
    static FirstAidFragment firstAidFragment = new FirstAidFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    enum  TabState {
        HOME,
        FIRSTAID,
        SETTINGS,
    }

}