package com.example.sistemadesecadodecafe;



import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends BaseNav {
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_LOCATION_PERMISSION = 2;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;
    private List<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    private ListView lvDevices;
    private Button btnEncenderMotor;
    private Button btnApagarMotor;
    private boolean motorEncendido = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btnEncenderMotor = findViewById(R.id.btnInicio);
        btnApagarMotor = findViewById(R.id.btnApagarMotor);

        lvDevices = findViewById(R.id.lv_devices);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Toast.makeText(this, "El dispositivo no soporta Bluetooth", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        checkBluetoothPermissions();
        lvDevices.setOnItemClickListener((parent, view, position, id) -> {
            BluetoothDevice selectedDevice = bluetoothDevices.get(position);
            new ConnectTask().execute(selectedDevice);
        });
        // Configurar botones


        btnEncenderMotor.setOnClickListener(v -> {
            if (!motorEncendido) {
                if (sendData("1")) { // Solo cambia el estado si el comando se envía
                    motorEncendido = true;
                    Toast.makeText(this, "El motor se ha encendido.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "El motor ya está en ejecución.", Toast.LENGTH_SHORT).show();
            }
        });

        btnApagarMotor.setOnClickListener(v -> {
            if (motorEncendido) {
                if (sendData("0")) {
                    motorEncendido = false;
                    Toast.makeText(this, "El motor se ha apagado.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "El motor ya está apagado.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean sendData(String data) {
        try {
            OutputStream outputStream = BluetoothSocketHolder.getSocket().getOutputStream();
            outputStream.write(data.getBytes());
            Toast.makeText(this, "Comando enviado: " + data, Toast.LENGTH_SHORT).show();
            return true;
        } catch (IOException e) {
            Toast.makeText(this, "Error al enviar datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }
    private void checkBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
            } else {
                showPairedDevices();
            }
        } else {
            showPairedDevices();
        }
    }

    private void showPairedDevices() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            bluetoothDevices.clear();
            if (!pairedDevices.isEmpty()) {
                bluetoothDevices.addAll(pairedDevices);
                ArrayAdapter<BluetoothDevice> adapter = new DeviceAdapter(this, bluetoothDevices);
                lvDevices.setAdapter(adapter);
            } else {
                Toast.makeText(this, "No hay dispositivos emparejados", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ConnectTask extends AsyncTask<BluetoothDevice, Void, Boolean> {
        @Override
        protected Boolean doInBackground(BluetoothDevice... devices) {
            try {
                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // UUID del dispositivo Bluetooth
                socket = devices[0].createRfcommSocketToServiceRecord(uuid);
                socket.connect();
                return true;
            } catch (IOException e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isConnected) {
            if (isConnected) {
                Toast.makeText(MainActivity.this, "Conexión exitosa", Toast.LENGTH_SHORT).show();
                BluetoothSocketHolder.setSocket(socket);
            } else {
                Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeConnection();
    }

    private void closeConnection() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error al cerrar conexión", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                showPairedDevices();
            } else {
                Toast.makeText(this, "Bluetooth no habilitado, la aplicación se cerrará", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showPairedDevices();
            } else {
                Toast.makeText(this, "Se requieren permisos de ubicación para acceder a dispositivos Bluetooth", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}