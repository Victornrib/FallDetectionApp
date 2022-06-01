package com.example.falldetectionapp.view;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.falldetectionapp.R;
import com.example.falldetectionapp.controller.ConnectDeviceController;
import com.example.falldetectionapp.model.Program;

import java.io.IOException;
import java.util.Set;

public class ConnectDeviceActivity extends AppCompatActivity {

    private Button buttonBack;
    private Button buttonConnectDevice;
    private Button buttonTestConnection;
    private ListView listViewPairedDevices;

    private AlertDialog alertDialog;

    private static final int REQUEST_BLUETOOTH_ACTIVATION = 1;
    private static final int REQUEST_BLUETOOTH_CONNECTION = 2;

    private ConnectDeviceController connectDeviceController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_device);

        alertDialog = new AlertDialog.Builder(ConnectDeviceActivity.this).create();

        connectDeviceController = new ConnectDeviceController(this);

        if (connectDeviceController.startBluetooth()) {
            Intent activateBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(activateBluetooth, REQUEST_BLUETOOTH_ACTIVATION);
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Your device doesn't have bluetooth", Toast.LENGTH_LONG).show();
        }

        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDeviceActivity();
            }
        });

        buttonConnectDevice = (Button) findViewById(R.id.buttonConnectDevice);
        buttonConnectDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!connectDeviceController.connected) {
                    Intent openPairedDevicesList = new Intent(ConnectDeviceActivity.this, DeviceListActivity.class);
                    startActivityForResult(openPairedDevicesList, REQUEST_BLUETOOTH_CONNECTION);
                } else {
                    connectDeviceController.disconnectDevice();
                    Toast.makeText(getApplicationContext(), "Bluetooth disconnected", Toast.LENGTH_LONG).show();
                    buttonConnectDevice.setText("Connect");
                }
            }
        });

        buttonTestConnection = (Button) findViewById(R.id.buttonTestConnection);
        buttonTestConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (connectDeviceController.connected) {
                    Program.getInstance().writeToBluetoothConnectedThread("Start");
                }
                else {
                    Toast.makeText(getApplicationContext(), "Bluetooth is not connected", Toast.LENGTH_LONG).show();
                }
            }
        });

        listViewPairedDevices = (ListView) findViewById(R.id.listViewPairedDevices);

        ArrayAdapter<String> arrayBluetooth = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Set<BluetoothDevice> pairedDevices = connectDeviceController.bluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    String deviceName = device.getName();
                    arrayBluetooth.add(deviceName);
                }
            }
            listViewPairedDevices.setAdapter(arrayBluetooth);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume()
    {
        super.onResume();
        Program program = Program.getInstance();
        program.setCurrentActivity(this);
        program.setScreenVisibility(true);
        program.checkFallDetectedActivity();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Program.getInstance().setScreenVisibility(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_BLUETOOTH_ACTIVATION:
                if (resultCode != Activity.RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Bluetooth wasn't activated", Toast.LENGTH_LONG).show();
                }
                break;

            case REQUEST_BLUETOOTH_CONNECTION:

                if (resultCode == Activity.RESULT_OK) {
                    try {
                        connectDeviceController.connectDevice(data);
                        buttonConnectDevice.setText("Disconnect");
                        Toast.makeText(getApplicationContext(), "You have been connected with:\n" + connectDeviceController.macAddress, Toast.LENGTH_LONG).show();
                    } catch (IOException error) {
                        connectDeviceController.connected = false;
                        Toast.makeText(getApplicationContext(), "An error has occurred:\n" + error, Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Didn't receive MAC ADDRESS", Toast.LENGTH_LONG).show();
                }
        }
    }


    private void openAddDeviceActivity() {
        Intent intent = new Intent(this, AddDeviceActivity.class);
        startActivity(intent);
    }
}