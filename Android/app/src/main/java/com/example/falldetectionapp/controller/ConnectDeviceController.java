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

import com.example.falldetectionapp.model.Device;
import com.example.falldetectionapp.model.Program;

import java.io.IOException;
import java.util.UUID;

public class ConnectDeviceController {

    Context context;

    private static final String TAG = "Error";

    //this is a unique address which identifies a Bluetooth device
    public String deviceMacAddress = null;
    //Universal Unique Identifier which is used for uniquely identifying information
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

    public void connectDevice(String deviceName, String deviceMacAddress) throws IOException {

        bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceMacAddress);
        this.deviceMacAddress = deviceMacAddress;

        if (bluetoothDevice != null) {
            if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                //a socket can then be created and connected.
                bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                bluetoothSocket.connect();
                connected = true;

                Device newDevice = new Device(deviceName, deviceMacAddress);

                Program program = Program.getInstance();
                program.getCurrentUser().addDevice(newDevice);
                program.startBluetoothConnectedThread(bluetoothSocket);
            }
        }
    }

    public void disconnectDevice() {
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
