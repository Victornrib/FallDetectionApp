package com.example.falldetectionapp.model;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;


public class User {
    public int userID; //would be nice to have a string so it is specific "user4053"
    public String name; //may need to make these variables more specific? fx userName
    public String telephone;
    public String email;
    public String password;
    boolean isConnected;
    boolean isEmContactVerified;
    public ArrayList<EmergencyContact> emContacts = new ArrayList<EmergencyContact>();
    public ArrayList<Device> pairedDevices = new ArrayList<Device>();

    public User(String name, String telephone, String email, String password) {
        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.password = password;

        //need to add check in the system all ID's and guarantee it is a unique ID
        Random random = new Random();
        this.userID = random.nextInt(1000);
    }


    //-----Emergency Contact-----

    //connects specific EmContact to specific user
    public void addEmContact(String name, String telephone, String email) {
        EmergencyContact newEmContact = new EmergencyContact(name, telephone, email);
        emContacts.add(newEmContact);
        storeUser();
    };

    //check the list of emContacts for specific user
    public ArrayList<EmergencyContact> getEmContacts() {
        return emContacts;
    };

    public void alertEmContact(String emContactEmail) {};


    //-----Device-----

    public void addDevice(String DeviceName, String MAC_ADDRESS) {
        if (this.pairedDevices.size() > 0) {
            for (int i = 0; i < this.pairedDevices.size(); i++) {
                if (MAC_ADDRESS.equals(pairedDevices.get(i).MAC_ADDRESS))
                    return;
            }
        }
        Device newDevice = new Device(DeviceName, MAC_ADDRESS);
        pairedDevices.add(newDevice);
        storeUser();
    }

    public ArrayList<Device> returnPairedDevices() { return pairedDevices; }


    //-----User-----

    public void storeUser() {
        //Getting reference to firebase
        DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference();

        //Adding to firebase (Replacing "." with "," because firebase doesn't allow child names with ".")
        firebaseReference.child("Users").child(this.email.replace(".",",")).setValue(this);

        //Passing values to Shared Preferences
        Gson gson = new Gson();
        String json = gson.toJson(this);
        SharedPrefs.putString(this.email,json);
    }
}