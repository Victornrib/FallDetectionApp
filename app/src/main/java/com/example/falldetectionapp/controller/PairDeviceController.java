package com.example.falldetectionapp.controller;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.falldetectionapp.model.Program;
import com.example.falldetectionapp.model.User;
import com.example.falldetectionapp.view.DeviceListActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class PairDeviceController {

    Context context;

    private static final String TAG = "Error";
    private Handler handler; // handler that gets info from Bluetooth service

    public static String MAC_ADDRESS = null;
    public UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    public BluetoothAdapter bluetoothAdapter = null;
    public BluetoothDevice bluetoothDevice = null;
    public BluetoothSocket bluetoothSocket = null;

    public boolean connected = false;


    public PairDeviceController(Context context) {
        this.context = context;
    }

    public boolean startBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter != null;
    }

    public boolean getDevice(Intent data) {
        MAC_ADDRESS = data.getExtras().getString("MAC_ADDRESS");
        bluetoothDevice = bluetoothAdapter.getRemoteDevice(MAC_ADDRESS);
        return bluetoothDevice != null;
    }

    public void connectDevice(Intent data) throws IOException {
        if (getDevice(data)) {
            if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                bluetoothSocket.connect();
                connected = true;

                String deviceName = data.getExtras().getString("DEVICE_NAME");
                addDevice(deviceName);

                Program program = Program.getInstance();
                program.startBluetoothConnectedThread(bluetoothSocket);
            }
        };
    }

    public void disconnectDevice() {
        //Adjust later
        try {
            connected = false;
            bluetoothSocket.close();
        }
        catch (IOException error) {
            Log.e(TAG, "An error has occurred:\n" + error, error);
        }
    }

    private void addDevice(String deviceName) {
        //Adding the device to the current user
        Program program = Program.getInstance();
        User currentUser = program.getCurrentUser();
        currentUser.addDevice(deviceName, MAC_ADDRESS);
    }
}
