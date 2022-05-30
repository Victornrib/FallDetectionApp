package com.example.falldetectionapp.model;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;


//Remove public after and see if it works
class User {
    public int userID; //would be nice to have a string so it is specific "user4053"
    public String name; //may need to make these variables more specific? fx userName
    public String telephone;
    public String email;
    public String password;
    //boolean isEmContactVerified;
    public ArrayList<EmergencyContact> emContacts = new ArrayList<EmergencyContact>();
    public ArrayList<Device> pairedDevices = new ArrayList<Device>();
    public ArrayList<String> recordedFalls = new ArrayList<String>();

    public User() {}

    public User(String name, String telephone, String email, String password) {
        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.password = password;

        //need to add check in the system all ID's and guarantee it is a unique ID
        Random random = new Random();
        this.userID = random.nextInt(1000);
    }

    //-----Fall-----
    public void addRecordedFall(String time) {
        recordedFalls.add(time);
        storeUser();
    }

    //-----Emergency Contact-----

    //connects specific EmContact to specific user
    public void addEmContact(String name, String telephone, String email) {
        EmergencyContact newEmContact = new EmergencyContact(name, telephone, email);
        emContacts.add(newEmContact);
        storeUser();
    };

    public void removeEmContact(String email) {
        for (int i = 0; i < this.emContacts.size(); i++) {
            if (emContacts.get(i).email.equals(email)) {
                emContacts.remove(i);
            }
        }
        storeUser();
    }

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
    }
}