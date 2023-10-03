package com.example.falldetectionapp.model;

import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.falldetectionapp.view.AddEmergencyContactActivity;
import com.example.falldetectionapp.view.FallDetectedActivity;
import com.example.falldetectionapp.view.SignInActivity;
import com.example.falldetectionapp.view.SignUpActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.android.gms.maps.model.LatLng;

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
    private boolean sendingMessage = false;
    private boolean deviceConnected = false;
    private boolean screenVisibility = false;
    private String fallTime = "";
    private Context currentActivity;
    private static ConnectedThread connectedThread;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 5;

    private LatLng latLng;

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

    public void setFallDetected(boolean fallDetected) {
        this.fallDetected = fallDetected;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public ArrayList<String> getCurrentUserEmContactsNames() {
        ArrayList<String> currentUserEmContactsNames = new ArrayList<String>();
        if (currentUser.emContacts != null) {
            for (int i = 0; i < currentUser.emContacts.size(); i++) {
                currentUserEmContactsNames.add(currentUser.emContacts.get(i).name);
            }
        }
        return currentUserEmContactsNames;
    }

    public ArrayList<String> getCurrentUserEmContactsEmails() {
        ArrayList<String> currentUserEmContactsEmails = new ArrayList<String>();
        if (currentUser.emContacts != null) {
            for (int i = 0; i < currentUser.emContacts.size(); i++) {
                currentUserEmContactsEmails.add(currentUser.emContacts.get(i).email);
            }
        }
        return currentUserEmContactsEmails;
    }

    public ArrayList<String> getCurrentUserEmContactsTelephones() {
        ArrayList<String> currentUserEmContactsTelephones = new ArrayList<String>();
        if (currentUser.emContacts != null) {
            for (int i = 0; i < currentUser.emContacts.size(); i++) {
                currentUserEmContactsTelephones.add(currentUser.emContacts.get(i).telephone);
            }
        }
        return currentUserEmContactsTelephones;
    }

    public String getCurrentUserAlertMode() {
        return currentUser.alertMode;
    }

    public String getFallTime() {
        return fallTime;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public void setScreenVisibility(boolean screenVisibility) {
        this.screenVisibility = screenVisibility;
    }

    public void signIn(String email, String password) {

        DatabaseReference firebaseUserReference = FirebaseDatabase.getInstance("https://fall-detection-83eed-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(email.replace(".",","));
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
        DatabaseReference firebaseUserReference = FirebaseDatabase.getInstance("https://fall-detection-83eed-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(email.replace(".",","));
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

    public void checkExistingEmergencyContact(String email) {
        DatabaseReference firebaseUserReference = FirebaseDatabase.getInstance("https://fall-detection-83eed-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(currentUser.email.replace(".",",")).child("emContacts");
        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AddEmergencyContactActivity addEmergencyContactActivity = (AddEmergencyContactActivity) currentActivity;
                boolean hasChildren = false;

                for (DataSnapshot dataValues : dataSnapshot.getChildren()){
                    hasChildren = true;
                    EmergencyContact emergencyContact = dataValues.getValue(EmergencyContact.class);

                    if (emergencyContact.email.equals(email)) {
                        addEmergencyContactActivity.generateEmContactCheckDialog("Registration invalid. Emergency Contact with this email already exists.\n");
                    }
                    else {
                        addEmergencyContactActivity.generateEmContactCheckDialog(null);
                    }
                }
                if (!hasChildren) {
                    addEmergencyContactActivity.generateEmContactCheckDialog(null);
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

    public void removeEmergencyContactFromUser(String email) {
        currentUser.removeEmContact(email);
    }

    public void addDeviceToCurrentUser(String deviceName, String MAC_ADDRESS) {
        currentUser.addDevice(deviceName, MAC_ADDRESS);
    }

    public void switchAlertModeFromCurrentUser() {
        currentUser.switchAlertMode();
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
        sendingMessage = true;

        //Get time
        LocalDateTime currentTime = LocalDateTime.now();

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        fallTime = timeFormatter.format(currentTime);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss");
        currentUser.addRecordedFall(dateTimeFormatter.format((currentTime)));

        getCurrentLocation();

        if (screenVisibility) {
            //Add checks if the activity is not on to choose if open the FallDetectedActivity
            checkFallDetectedActivity();
        }
        else {
            if (currentUser.alertMode.equals("Call")) {
                if (ContextCompat.checkSelfPermission(currentActivity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    callEmContacts();
                }
                // For permission not granted
                // ActivityCompat.requestPermissions((Activity) currentActivity, new String[] {Manifest.permission.CALL_PHONE}, 1);
            }
        }
    };

    public void callEmContacts() {
        for (int i = 0; i < currentUser.getEmContacts().size(); i++) {
            currentActivity.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + currentUser.getEmContacts().get(i).telephone)));
        }
        /*
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + currentUser.getEmContacts().get(0).telephone));
        currentActivity.startActivity(intent);
        */

    }

    //code from https://www.youtube.com/watch?v=ofAL1C4jUJw
    public void sendSmsToEmContacts() {
        SmsManager smsManager = SmsManager.getDefault();

        String latitude = String.valueOf(latLng.latitude).replace(",", ".");
        String longitude = String.valueOf(latLng.longitude).replace(",", ".");
        String location = "\nhttp://maps.google.com/maps?q=loc:"+latitude+","+longitude;

        for (int i = 0; i < currentUser.getEmContacts().size(); i++) {
            EmergencyContact currentEmContact = currentUser.getEmContacts().get(i);
            String emContactPhoneNumber = currentEmContact.telephone;
            String message = "Attention " + currentEmContact.name + ": " + currentUser.name + " fell!\nPlease take action now!\n"+location;
            smsManager.sendTextMessage(emContactPhoneNumber, null, message, null, null);
        }
        sendingMessage = false;
    }

    public void getCurrentLocation() {

        ActivityCompat.requestPermissions((Activity) currentActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions((Activity) currentActivity, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());

                //Probably will have errors when receiving alerts from different activities
                if (currentUser.alertMode.equals("SMS") && sendingMessage) {
                    if (ContextCompat.checkSelfPermission(currentActivity, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                        sendSmsToEmContacts();
                    }
                }
            }
        };
        locationManager = (LocationManager) currentActivity.getSystemService(LOCATION_SERVICE);
        try {
            if (ActivityCompat.checkSelfPermission(currentActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(currentActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME,MIN_DIST,locationListener);
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void checkFallDetectedActivity() {
        if (fallDetected) {
            if (currentActivity != null) {
                Intent intent = new Intent(currentActivity, FallDetectedActivity.class);
                currentActivity.startActivity(intent);
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