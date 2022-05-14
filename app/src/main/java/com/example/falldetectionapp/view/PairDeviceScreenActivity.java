package com.example.falldetectionapp.view;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.falldetectionapp.R;


public class PairDeviceScreenActivity extends AppCompatActivity {
    Button buttonBack;
    Button buttonPairDevice;

    //BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_BLUETOOTH_ACTIVATION = 1;
    BluetoothAdapter bluetoothAdapter;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_device_screen);

        //click back --> goes back to add device
        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDeviceScreenActivity();
            }
        });

        //click pair --> also goes back to add device.
        // This is not the intention right? -gwen
        buttonPairDevice = (Button) findViewById(R.id.buttonPairDevice);
        buttonPairDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDeviceScreenActivity();
            }
        });

        startBluetooth();
    }

    private void openAddDeviceScreenActivity() {
        Intent intent = new Intent(this, AddDeviceScreenActivity.class);
        startActivity(intent);
    }



    private void startBluetooth() {

        //Create alert dialog
        alertDialog = new AlertDialog.Builder(PairDeviceScreenActivity.this).create();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Your device doesn't have bluetooth");
            alertDialog.show();
        }
        else {
            System.out.println("\n\n\n\n\nFOI\n\n\n\n\n");
            Intent activateBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                //
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivityForResult(activateBluetooth, REQUEST_BLUETOOTH_ACTIVATION);
            alertDialog.setTitle("Teste");
            alertDialog.setMessage("Foi ate aqui");
            alertDialog.show();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_BLUETOOTH_ACTIVATION:
                if (resultCode == Activity.RESULT_OK) {
                    alertDialog.setTitle("Success");
                    alertDialog.setMessage("Bluetooth activated");
                    alertDialog.show();
                }
                else {
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Bluetooth wasn't activated");
                    alertDialog.show();
                }
                break;
        }
    }



    //we will need an AlertDialog here to say if connection is successful or not.
}