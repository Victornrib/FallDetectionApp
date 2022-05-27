package com.example.falldetectionapp.model;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.falldetectionapp.view.AddDeviceScreenActivity;
import com.example.falldetectionapp.view.FallDetectedScreenActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Program {

    private static Program program;
    private User currentUser = null;
    private boolean fallDetected = false;
    private Context currentActivity;
    private static ConnectedThread connectedThread;


    //Create handler for dealing with messages
    public Handler handler;

    private Program() {

        handler = new Handler(Looper.getMainLooper()) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void handleMessage(Message message) {
                receiveAlert();
            }
        };
    }

    public static Program getInstance(){
        if (program == null) {
            program = new Program();
        }
        return program;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }


    public User getCurrentUser() {
        return currentUser;
    }


    public void setCurrentActivity(Context currentActivity) {
        this.currentActivity = currentActivity;
    }

    public void signIn(String email, String password) throws InterruptedException {

        DatabaseReference firebaseUserReference = FirebaseDatabase.getInstance().getReference("Users").child(email.replace(".",","));
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User potentialUser = dataSnapshot.getValue(User.class);
                if (potentialUser.password.equals(password)) {
                    program.setCurrentUser(potentialUser);
                    openAddDeviceScreenActivity();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error", databaseError.getMessage()); //Don't ignore errors!
            }
        };
        firebaseUserReference.addListenerForSingleValueEvent(valueEventListener);
        /*
        while (program.getCurrentUser() == null) {
            Thread.sleep(500);
        }
        /*
         */

        /*
        Gson gson = new Gson();
        String jsonRet = SharedPrefs.getString(email,null);

        if (jsonRet != null) {
            currentUser = gson.fromJson(jsonRet, User.class);

            if (currentUser.password.equals(password)) {
                return currentUser;
            }
            else {
                alertDialogErrorMessage = "Password wrong.";
                return null;
            }
        }
        else {
            alertDialogErrorMessage = "Email not found.";
            return null;
        }

         */
    }

    public void storeNewUser(String name, String telephone, String email, String password) {
        User newUser = new User(name, telephone, email, password);
        newUser.storeUser();
    }

    public void startBluetoothConnectedThread(BluetoothSocket bluetoothSocket) {
        if (bluetoothSocket != null) {
            if (connectedThread == null) {
                connectedThread = new ConnectedThread(bluetoothSocket);
                connectedThread.start();
            }
        }
    }

    public void writeToBluetoothConnectedThread(String message) {
        connectedThread.write(message);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void receiveAlert() {
        fallDetected = true;

        //Get time
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime currentTime = LocalDateTime.now();
        System.out.println(dtf.format(currentTime));

        //Add checks if the activity is not on to choose if open the FallDetectedScreenActivity
        openFallDetectedScreenActivity();
    };

    private void openFallDetectedScreenActivity() {
        Intent intent = new Intent(currentActivity, FallDetectedScreenActivity.class);
        currentActivity.startActivity(intent);
    }

    private void openAddDeviceScreenActivity() {
        Intent intent = new Intent(currentActivity, AddDeviceScreenActivity.class);
        currentActivity.startActivity(intent);
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
        private static final String TAG = "Error";

        private ConnectedThread(BluetoothSocket socket) {
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
                } catch (IOException | NullPointerException e) {
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