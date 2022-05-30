package com.example.falldetectionapp.controller;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.falldetectionapp.model.Program;

import java.io.IOException;
import java.util.UUID;

public class ConnectDeviceController {

    Context context;

    private static final String TAG = "Error";

    public static String macAddress = null;
    public UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    public BluetoothAdapter bluetoothAdapter = null;
    public BluetoothDevice bluetoothDevice = null;
    public BluetoothSocket bluetoothSocket = null;

    public boolean connected = false;


    public ConnectDeviceController(Context context) {
        this.context = context;
    }

    public boolean startBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter != null;
    }

    public boolean getDevice(Intent data) {
        macAddress = data.getExtras().getString("MAC_ADDRESS");
        bluetoothDevice = bluetoothAdapter.getRemoteDevice(macAddress);
        return bluetoothDevice != null;
    }

    public void connectDevice(Intent data) throws IOException {
        if (getDevice(data)) {
            if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                bluetoothSocket.connect();
                connected = true;

                String deviceName = data.getExtras().getString("DEVICE_NAME");

                Program program = Program.getInstance();
                program.addDeviceToCurrentUser(deviceName, macAddress);
                program.startBluetoothConnectedThread(bluetoothSocket);
            }
        };
    }

    public void disconnectDevice() {
        //Adjust later
        try {
            connected = false;
            bluetoothSocket.close();
            Program program = Program.getInstance();
            program.closeBluetoothConnectedThread();
        }
        catch (IOException error) {
            Log.e(TAG, "An error has occurred:\n" + error, error);
        }
    }
}
