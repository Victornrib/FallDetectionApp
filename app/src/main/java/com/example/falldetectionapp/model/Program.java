package com.example.falldetectionapp.model;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.example.falldetectionapp.view.FallDetectedActivity;
import com.example.falldetectionapp.view.SignInActivity;
import com.example.falldetectionapp.view.SignUpActivity;
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
import java.util.ArrayList;


public class Program {

    private static Program program;
    private User currentUser = null;
    private boolean fallDetected = false;
    private boolean devicePaired = false;
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

    public boolean isFallDetected() {
        return fallDetected;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public ArrayList<String> getCurrentUserEmContactsNames() {
        ArrayList<String> currentUserEmContactsNames = new ArrayList<String>();
        for (int i = 0; i < currentUser.emContacts.size(); i++) {
            currentUserEmContactsNames.add(currentUser.emContacts.get(i).name);
        }
        return currentUserEmContactsNames;
    }

    public ArrayList<String> getCurrentUserEmContactsEmails() {
        ArrayList<String> currentUserEmContactsEmails = new ArrayList<String>();
        for (int i = 0; i < currentUser.emContacts.size(); i++) {
            currentUserEmContactsEmails.add(currentUser.emContacts.get(i).email);
        }
        return currentUserEmContactsEmails;
    }

    public ArrayList<String> getCurrentUserEmContactsTelephones() {
        ArrayList<String> currentUserEmContactsTelephones = new ArrayList<String>();
        for (int i = 0; i < currentUser.emContacts.size(); i++) {
            currentUserEmContactsTelephones.add(currentUser.emContacts.get(i).telephone);
        }
        return currentUserEmContactsTelephones;
    }


    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public void signIn(String email, String password) {

        DatabaseReference firebaseUserReference = FirebaseDatabase.getInstance().getReference("Users").child(email.replace(".",","));
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User potentialUser = dataSnapshot.getValue(User.class);
                SignInActivity signInActivity = (SignInActivity) currentActivity;

                if (potentialUser == null) {
                    signInActivity.generateErrorDialog("Email not found.");
                }
                else {
                    if (potentialUser.password.equals(password)) {
                        program.setCurrentUser(potentialUser);
                        signInActivity.openAddDeviceActivity();
                    }
                    else {
                        signInActivity.generateErrorDialog("Password is wrong.");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error", databaseError.getMessage()); //Don't ignore errors!
            }
        };
        firebaseUserReference.addListenerForSingleValueEvent(valueEventListener);
    }

    public void storeNewUser(String name, String telephone, String email, String password) {
        User newUser = new User(name, telephone, email, password);
        newUser.storeUser();
    }

    public void checkExistingUser(String email) {
        DatabaseReference firebaseUserReference = FirebaseDatabase.getInstance().getReference("Users").child(email.replace(".",","));
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User potentialUser = dataSnapshot.getValue(User.class);
                SignUpActivity signUpActivity = (SignUpActivity) currentActivity;

                if (potentialUser == null) {
                    signUpActivity.generateUserCheckDialogMessage(null);
                }
                else {
                    signUpActivity.generateUserCheckDialogMessage("Registration invalid. User with this email already exists.\n");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error", databaseError.getMessage()); //Don't ignore errors!
            }
        };
        firebaseUserReference.addListenerForSingleValueEvent(valueEventListener);
    }

    public void addEmergencyContactToCurrentUser(String name, String telephone, String email) {
        currentUser.addEmContact(name, telephone, email);
    }

    public void addDeviceToCurrentUser(String deviceName, String MAC_ADDRESS) {
        currentUser.addDevice(deviceName, MAC_ADDRESS);
    }

    public void startBluetoothConnectedThread(BluetoothSocket bluetoothSocket) {
        if (bluetoothSocket != null) {
            if (connectedThread == null) {
                connectedThread = new ConnectedThread(bluetoothSocket);
                connectedThread.start();
            }
        }
    }

    public void closeBluetoothConnectedThread() {
        connectedThread.cancel();
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

        //Make a future check to know what is the type of contact
        // if contactType == "SMS"
        if (ContextCompat.checkSelfPermission(currentActivity, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            sendSMS();
        }
        //Add checks if the activity is not on to choose if open the FallDetectedActivity
        openFallDetectedActivity();
    };

    private void sendSMS() {
        SmsManager smsManager = SmsManager.getDefault();

        for (int i = 0; i < currentUser.getEmContacts().size(); i++) {
            EmergencyContact currentEmContact = currentUser.getEmContacts().get(i);
            String emContactPhoneNumber = currentEmContact.telephone;
            String message = "Attention " + currentEmContact.name + ": " + currentUser.name + " felt!\nPlease take action now!";
            smsManager.sendTextMessage(emContactPhoneNumber, null, message, null, null);
        }
    }

    private void openFallDetectedActivity() {
        Intent intent = new Intent(currentActivity, FallDetectedActivity.class);
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