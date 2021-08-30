package com.mobdeve.s11.pokeplan;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(() -> {
            setContentView(R.layout.activity_main);
            BottomNavigationView navView = findViewById(R.id.bottom_nav_view);
            NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_fragment);
            NavigationUI.setupWithNavController(navView, navController);
        }, 1000);
    }
}