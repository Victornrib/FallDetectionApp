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
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.falldetectionapp.R;
import com.example.falldetectionapp.model.Program;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


public class PairDeviceScreenActivity extends AppCompatActivity {
    Button buttonBack;
    Button buttonPairDevice;
    Button buttonTestConnection;

    private static final int REQUEST_BLUETOOTH_ACTIVATION = 1;
    private static final int REQUEST_BLUETOOTH_CONNECTION = 2;

    ConnectedThread connectedThread;

    private static final String TAG = "Error";
    private Handler handler; // handler that gets info from Bluetooth service

    private static String MAC_ADDRESS = null;
    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    BluetoothAdapter bluetoothAdapter = null;
    BluetoothDevice bluetoothDevice = null;
    BluetoothSocket bluetoothSocket = null;

    AlertDialog alertDialog;
    boolean connected = false;

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

                if (!connected) {
                    Intent openPairedDevicesList = new Intent(PairDeviceScreenActivity.this, DeviceListActivity.class);
                    startActivityForResult(openPairedDevicesList, REQUEST_BLUETOOTH_CONNECTION);
                } else {
                    try {
                        Toast.makeText(getApplicationContext(), "Bluetooth disconnected", Toast.LENGTH_LONG).show();
                        connected = false;
                        buttonPairDevice.setText("Pair");
                        bluetoothSocket.close();
                    } catch (IOException error) {
                        Toast.makeText(getApplicationContext(), "An error has occurred:\n" + error, Toast.LENGTH_LONG).show();
                    }
                }
                //openAddDeviceScreenActivity();
            }
        });

        buttonTestConnection = (Button) findViewById(R.id.buttonTestConnection);
        buttonTestConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (connected) {
                    connectedThread.write("Alert");
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
                //Toast.makeText(getApplicationContext(), "Fall Detected", Toast.LENGTH_LONG).show();

                //should only call the fall detected activity instead of receiveAlart
                //Program program = Program.getInstance();
                //program.receiveAlert();
                openFallDetectedScreenActivity();
            }
        };

        startBluetooth();
    }

    private void openAddDeviceScreenActivity() {
        Intent intent = new Intent(this, AddDeviceScreenActivity.class);
        startActivity(intent);
    }

    private void openFallDetectedScreenActivity() {
        try {
            //Toast.makeText(getApplicationContext(), "Going to change screen", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, FallDetectedScreenActivity.class);
            startActivity(intent);
        } catch (NullPointerException error) {
            Log.e(TAG, "Error changing screen", error);
        }
    }


    private void startBluetooth() {

        //Create alert dialog
        alertDialog = new AlertDialog.Builder(PairDeviceScreenActivity.this).create();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Your device doesn't have bluetooth");
            alertDialog.show();
        } else {
            Intent activateBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(activateBluetooth, REQUEST_BLUETOOTH_ACTIVATION);
            }
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
                } else {
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Bluetooth wasn't activated");
                    alertDialog.show();
                }
                break;


            case REQUEST_BLUETOOTH_CONNECTION:
                if (resultCode == Activity.RESULT_OK) {

                    MAC_ADDRESS = data.getExtras().getString(DeviceListActivity.MAC_ADDRESS);

                    //Toast.makeText(getApplicationContext(), "Success obtaining MAC ADDRESS:\n" + MAC_ADDRESS, Toast.LENGTH_LONG).show();

                    bluetoothDevice = bluetoothAdapter.getRemoteDevice(MAC_ADDRESS);
                    try {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                            bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
                            connected = true;
                            bluetoothSocket.connect();
                            buttonPairDevice.setText("Unpair");

                            connectedThread = new ConnectedThread(bluetoothSocket);
                            connectedThread.start();

                            Toast.makeText(getApplicationContext(), "You have been connected with:\n" + MAC_ADDRESS, Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException error) {
                        connected = false;
                        Toast.makeText(getApplicationContext(), "An error has occurred:\n" + error, Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Didn't receive MAC ADDRESS", Toast.LENGTH_LONG).show();
                }
        }
    }

    private interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_WRITE = 1;
        public static final int MESSAGE_TOAST = 2;

        // ... (Add other message types here as needed.)
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);
                    // Send the obtained bytes to the UI activity.
                    Message readMsg = handler.obtainMessage(
                            MessageConstants.MESSAGE_READ, numBytes, -1,
                            mmBuffer);
                    readMsg.sendToTarget();
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break;
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        public void write(String msg) {
            try {
                byte[] bytesMsg = msg.getBytes();
                mmOutStream.write(bytesMsg);

                // Share the sent message with the UI activity.
                Message writtenMsg = handler.obtainMessage(
                        MessageConstants.MESSAGE_WRITE, -1, -1, mmBuffer);
                writtenMsg.sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);

                // Send a failure message back to the activity.
                Message writeErrorMsg =
                        handler.obtainMessage(MessageConstants.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("toast",
                        "Couldn't send data to the other device");
                writeErrorMsg.setData(bundle);
                handler.sendMessage(writeErrorMsg);
            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

}