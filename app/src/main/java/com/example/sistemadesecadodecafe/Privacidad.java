package com.example.sistemadesecadodecafe;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sistemadesecadodecafe.databinding.ActivityPrivacidadBinding;

public class Privacidad extends monitoreo {
    ActivityPrivacidadBinding privacidadBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        privacidadBinding = ActivityPrivacidadBinding.inflate(getLayoutInflater());
        setContentView(privacidadBinding.getRoot());


    }
}