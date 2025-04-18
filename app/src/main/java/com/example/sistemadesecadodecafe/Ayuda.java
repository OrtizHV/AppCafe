package com.example.sistemadesecadodecafe;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sistemadesecadodecafe.databinding.ActivityAyudaBinding;

public class Ayuda extends AppCompatActivity{
    ActivityAyudaBinding ayudaBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ayudaBinding = ActivityAyudaBinding.inflate(getLayoutInflater());
        setContentView(ayudaBinding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbarAyuda);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}