package com.example.falldetectionapp.view;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.falldetectionapp.R;
import com.example.falldetectionapp.controller.PairDeviceController;
import com.example.falldetectionapp.model.Program;

import java.io.IOException;


public class PairDeviceScreenActivity extends AppCompatActivity {

    Button buttonBack;
    Button buttonPairDevice;
    Button buttonTestConnection;

    //Create alert dialog
    AlertDialog alertDialog = new AlertDialog.Builder(PairDeviceScreenActivity.this).create();

    private static final int REQUEST_BLUETOOTH_ACTIVATION = 1;
    private static final int REQUEST_BLUETOOTH_CONNECTION = 2;

    //Create handler for dealing with messages
    Handler handler;

    PairDeviceController pairDeviceController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_device_screen);

        pairDeviceController = new PairDeviceController(this, handler);

        if (pairDeviceController.startBluetooth()) {
            Intent activateBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(activateBluetooth, REQUEST_BLUETOOTH_ACTIVATION);
            }
        }
        else {
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Your device doesn't have bluetooth");
            alertDialog.show();
        }

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

                if (!pairDeviceController.connected) {
                    Intent openPairedDevicesList = new Intent(PairDeviceScreenActivity.this, DeviceListActivity.class);
                    startActivityForResult(openPairedDevicesList, REQUEST_BLUETOOTH_CONNECTION);
                } else {
                    pairDeviceController.disconnectDevice();
                    Toast.makeText(getApplicationContext(), "Bluetooth disconnected", Toast.LENGTH_LONG).show();
                    buttonPairDevice.setText("Pair");
                }
            }
        });

        buttonTestConnection = (Button) findViewById(R.id.buttonTestConnection);
        buttonTestConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (pairDeviceController.connected) {
                    pairDeviceController.connectedThread.write("Alert");
                }
                else {
                    Toast.makeText(getApplicationContext(), "Bluetooth is not connected", Toast.LENGTH_LONG).show();
                }
            }
        });

        handler = new Handler(Looper.getMainLooper()) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void handleMessage(Message message) {
                Program program = Program.getInstance();
                program.receiveAlert();
                openFallDetectedScreenActivity();
            }
        };
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
                } else {
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Bluetooth wasn't activated");
                    alertDialog.show();
                }
                break;

            case REQUEST_BLUETOOTH_CONNECTION:
                if (resultCode == Activity.RESULT_OK) {
                    pairDeviceController.getDevice(data);

                    try {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            //All of this functions need to be in the view and not in controller, because they need the permission above, which has to be done in a view
                            pairDeviceController.connectDevice();
                            buttonPairDevice.setText("Unpair");
                            pairDeviceController.bluetoothSocket = pairDeviceController.bluetoothDevice.createRfcommSocketToServiceRecord(pairDeviceController.uuid);
                            Toast.makeText(getApplicationContext(), "You have been connected with:\n" + pairDeviceController.MAC_ADDRESS, Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException error) {
                        pairDeviceController.connected = false;
                        Toast.makeText(getApplicationContext(), "An error has occurred:\n" + error, Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Didn't receive MAC ADDRESS", Toast.LENGTH_LONG).show();
                }
        }
    }


    private void openAddDeviceScreenActivity() {
        Intent intent = new Intent(this, AddDeviceScreenActivity.class);
        startActivity(intent);
    }


    private void openFallDetectedScreenActivity() {
        Intent intent = new Intent(this, FallDetectedScreenActivity.class);
        startActivity(intent);
    }
}