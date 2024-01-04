package com.example.falldetectionapp.view;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.falldetectionapp.R;
import com.example.falldetectionapp.controller.ConnectDeviceController;
import com.example.falldetectionapp.model.Program;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class ConnectDeviceActivity extends AppCompatActivity {

    private ImageButton buttonBack;

    //private Button buttonTestConnection;
    private ListView listViewPairedDevices;
    private AlertDialog alertDialog;
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
                startActivity(activateBluetooth);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Your device doesn't have bluetooth", Toast.LENGTH_LONG).show();
        }

        buttonBack = (ImageButton) findViewById(R.id.buttonBackToHomeFromConnectDevice);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHomeActivity();
            }
        });

/*        buttonTestConnection = (Button) findViewById(R.id.buttonTestConnection);
        buttonTestConnection.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                Program.getInstance().receiveAlert();
                *//*
                if (connectDeviceController.connected) {
                    Program.getInstance().writeToBluetoothConnectedThread("Start");
                }
                else {
                    Toast.makeText(getApplicationContext(), "Bluetooth is not connected", Toast.LENGTH_LONG).show();
                }
                 *//*
            }
        });*/

        listViewPairedDevices = (ListView) findViewById(R.id.listViewPairedDevices);
        Set<BluetoothDevice> boundedDevicesSet = connectDeviceController.bluetoothAdapter.getBondedDevices();
        ArrayList<BluetoothDevice> pairedDevices = new ArrayList<BluetoothDevice>(boundedDevicesSet);

        if (pairedDevices.size() > 0) {
            PairedDeviceAdapter pairedDeviceAdapter = new PairedDeviceAdapter(this, pairedDevices);
            listViewPairedDevices.setAdapter(pairedDeviceAdapter);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        Program program = Program.getInstance();
        program.setCurrentActivity(this);
        program.setScreenVisibility(true);
        program.checkFallDetectedActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Program.getInstance().setScreenVisibility(false);
    }

    private void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private class PairedDeviceAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<BluetoothDevice> pairedDevices;
        private LayoutInflater inflater;
        private ViewHolder lastConnectedViewHolder = null;
        int devicePositionConnected;

        private PairedDeviceAdapter(Context context, ArrayList<BluetoothDevice> pairedDevices) {
            this.context = context;
            this.pairedDevices = pairedDevices;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public class ViewHolder {
            LinearLayout linearLayoutPairedDevice;
            TextView textViewPairedDevice;
            TextView textViewConnectionStatus;
        }

        @Override
        public int getCount() {
            return pairedDevices.size();
        }

        @Override
        public Object getItem(int position) {
            return pairedDevices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder;

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                if (view == null) {
                    view = inflater.inflate(R.layout.list_item_paired_device, null);
                    holder = new ViewHolder();
                    holder.linearLayoutPairedDevice = view.findViewById(R.id.linearLayoutPairedDevice);
                    holder.textViewPairedDevice = view.findViewById(R.id.textViewPairedDevice);
                    holder.textViewConnectionStatus = view.findViewById(R.id.textViewConnectionStatus);
                    view.setTag(holder);

                    int connectedPairedDevicePosition = Program.getInstance().getConnectedPairedDevicePosition();
                    if (connectedPairedDevicePosition == position) {
                        holder.textViewConnectionStatus.setText("Connected");
                        holder.textViewConnectionStatus.setTextColor(ContextCompat.getColor(context, R.color.standard_orange));
                    }

                } else {
                    holder = (ViewHolder) view.getTag();
                }

                String pairedDeviceName = pairedDevices.get(position).getName();
                String pairedDeviceMacAddress = pairedDevices.get(position).getAddress();
                Context viewContext = holder.linearLayoutPairedDevice.getContext();

                holder.linearLayoutPairedDevice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!connectDeviceController.connected) {
                            connectDevice(holder, pairedDeviceName, pairedDeviceMacAddress, position, viewContext);
                        } else {
                            if (lastConnectedViewHolder != null && lastConnectedViewHolder != holder) {
                                disconnectDevice(lastConnectedViewHolder, viewContext);
                            }

                            if (devicePositionConnected != position) {
                                connectDevice(holder, pairedDeviceName, pairedDeviceMacAddress, position, viewContext);
                            } else {
                                disconnectDevice(holder, viewContext);
                            }
                        }
                    }
                });

                holder.textViewPairedDevice.setText(pairedDeviceName);
                return view;
            } else {
                return convertView;
            }
        }

        private void connectDevice(ViewHolder holder, String pairedDeviceName, String pairedDeviceMacAddress, int position, Context context) {
            try {
                disconnectLastConnectedDevice();
                connectDeviceController.connectDevice(pairedDeviceName, pairedDeviceMacAddress);
                holder.textViewConnectionStatus.setText("Connected");
                holder.textViewConnectionStatus.setTextColor(ContextCompat.getColor(context, R.color.standard_orange));
                devicePositionConnected = position;
                lastConnectedViewHolder = holder;
                Program.getInstance().setConnectedPairedDevicePosition(position);
                Toast.makeText(context, "You have been connected with:\n" + pairedDeviceName, Toast.LENGTH_LONG).show();
            } catch (IOException error) {
                connectDeviceController.connected = false;
                holder.textViewConnectionStatus.setText("Not connected");
                holder.textViewConnectionStatus.setTextColor(ContextCompat.getColor(context, R.color.grey));
                Program.getInstance().setConnectedPairedDevicePosition(-1);
                devicePositionConnected = -1;
                Toast.makeText(context, "The connection was not possible:\n" + error, Toast.LENGTH_LONG).show();
            }
        }

        private void disconnectDevice(ViewHolder holder, Context context) {
            connectDeviceController.disconnectDevice();
            holder.textViewConnectionStatus.setText("Not connected");
            holder.textViewConnectionStatus.setTextColor(ContextCompat.getColor(context, R.color.grey));
            Program.getInstance().setConnectedPairedDevicePosition(-1);
            devicePositionConnected = -1;
            lastConnectedViewHolder = null;
            Toast.makeText(context, "Device disconnected", Toast.LENGTH_LONG).show();
        }

        private void disconnectLastConnectedDevice() {
            if (lastConnectedViewHolder != null) {
                disconnectDevice(lastConnectedViewHolder, lastConnectedViewHolder.linearLayoutPairedDevice.getContext());
            }
        }
    }
}