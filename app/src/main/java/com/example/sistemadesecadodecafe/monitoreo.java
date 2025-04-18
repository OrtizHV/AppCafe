package com.example.sistemadesecadodecafe;


import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class monitoreo extends BaseNav {
    private TextView txtTemperatura;
    private TextView txtHumedad;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         txtTemperatura = findViewById(R.id.txtTemperatura);
         txtHumedad = findViewById(R.id.txtHumedad);
         listDatos();
    }
    private void listDatos() {
        new Thread(() -> {
            try {
                InputStream inputStream = BluetoothSocketHolder.getSocket().getInputStream();
                byte[] buffer = new byte[1024];
                int bytes;

                while ((bytes = inputStream.read(buffer)) != -1) {
                    String data = new String(buffer, 0, bytes);
                    runOnUiThread(() -> updateSensorDatos(data));
                }
            } catch (IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error al leer datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace(); // Para depuración
                });
            }
        }).start();
    }

    private void updateSensorDatos(String data) {
        String[] parts = data.split(",");
        for (String part : parts) {
            if (part.startsWith("Temperatura:")) {
                String temp = part.split(":")[1];
                txtTemperatura.setText("Temperatura: " + temp + " °C");
            } else if (part.startsWith("Humedad:")) {
                String hum = part.split(":")[1];
                txtHumedad.setText("Humedad: " + hum + " %");
            }
        }
    }
}