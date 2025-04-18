package com.example.sistemadesecadodecafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public  class BaseNav extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_nav);
        ViewFlipper viewFlipper= findViewById(R.id.view_flipper);
        BottomNavigationView bottomNavigationView = findViewById(R.id.btn_Nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                ViewFlipper viewFlipper = findViewById(R.id.view_flipper);
                if (item.getItemId() == R.id.navigation_main) {
                    viewFlipper.setDisplayedChild(0);
                    return true;
                } else if (item.getItemId() == R.id.navigation_monitoreo) {
                    viewFlipper.setDisplayedChild(1);
                    return true;
                } else if (item.getItemId() == R.id.navigation_manual) {
                    viewFlipper.setDisplayedChild(2);
                    return true;
                } else if (item.getItemId() == R.id.navigation_automatico) {
                    viewFlipper.setDisplayedChild(3);
                    return true;
                }
                return false;
            }
        });

    }

}