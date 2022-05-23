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
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.Set;


public class DeviceListActivity extends ListActivity {

    private BluetoothAdapter bluetoothAdapter = null;
    public static String MAC_ADDRESS = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> arrayBluetooth = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
        //Toast.makeText(getApplicationContext(), "Info:\n" + generalInformation, Toast.LENGTH_LONG).show();

        String macAddress = generalInformation.substring(generalInformation.length() - 17);
        //Toast.makeText(getApplicationContext(), "MAC ADDRESS:\n" + macAddress, Toast.LENGTH_LONG).show();

        Intent returnMacAddress = new Intent();
        returnMacAddress.putExtra(MAC_ADDRESS, macAddress);

        setResult(RESULT_OK, returnMacAddress);
        finish();
    }
}
