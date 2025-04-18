package com.example.sistemadesecadodecafe;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;

public class Automatico extends BaseNav {
    private Button btnEncenderMotor;
    private Button btnApagarMotor;
    private boolean motorEncendido = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automatico);

        // Configurar botones
        btnEncenderMotor = findViewById(R.id.btnInicio);
        btnApagarMotor = findViewById(R.id.btnApagarMotor);
        
    }


}