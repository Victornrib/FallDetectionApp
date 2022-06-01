package com.example.falldetectionapp.view;

import android.Manifest;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import java.util.Set;


public class DeviceListActivity extends ListActivity {

    private BluetoothAdapter bluetoothAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> arrayBluetooth = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    String deviceName = device.getName();
                    String deviceMacAddress = device.getAddress();
                    arrayBluetooth.add(deviceName + "\n" + deviceMacAddress);
                }
            }
            setListAdapter(arrayBluetooth);
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String generalInformation = ((TextView) v).getText().toString();

        String deviceName = generalInformation.substring(0, generalInformation.length() - 18);

        String macAddress = generalInformation.substring(generalInformation.length() - 17);

        Bundle bundleDeviceProperties = new Bundle();
        bundleDeviceProperties.putString("DEVICE_NAME", deviceName);
        bundleDeviceProperties.putString("MAC_ADDRESS", macAddress);

        Intent returnDeviceProperties = new Intent();
        returnDeviceProperties.putExtras(bundleDeviceProperties);

        setResult(RESULT_OK, returnDeviceProperties);
        finish();
    }
}
