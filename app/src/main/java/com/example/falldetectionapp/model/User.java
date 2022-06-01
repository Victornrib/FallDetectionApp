package com.example.falldetectionapp.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;


class User {
    public int userID;
    public String name;
    public String telephone;
    public String email;
    public String password;
    public ArrayList<EmergencyContact> emContacts = new ArrayList<EmergencyContact>();
    public ArrayList<Device> pairedDevices = new ArrayList<Device>();
    public ArrayList<String> recordedFalls = new ArrayList<String>();

    public User() {}

    public User(String name, String telephone, String email, String password) {
        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.password = password;

        Random random = new Random();
        this.userID = random.nextInt(1000);
    }

    public void addRecordedFall(String time) {
        recordedFalls.add(time);
        storeUser();
    }

    public void addEmContact(String name, String telephone, String email) {
        EmergencyContact newEmContact = new EmergencyContact(name, telephone, email);
        emContacts.add(newEmContact);
        storeUser();
    }

    public void removeEmContact(String email) {
        for (int i = 0; i < this.emContacts.size(); i++) {
            if (emContacts.get(i).email.equals(email)) {
                emContacts.remove(i);
            }
        }
        storeUser();
    }

    public ArrayList<EmergencyContact> getEmContacts() {
        return emContacts;
    };

    public void alertEmContact(String emContactEmail) {};

    public void addDevice(String DeviceName, String MAC_ADDRESS) {
        if (this.pairedDevices.size() > 0) {
            for (int i = 0; i < this.pairedDevices.size(); i++) {
                if (MAC_ADDRESS.equals(pairedDevices.get(i).macAddress))
                    return;
            }
        }
        Device newDevice = new Device(DeviceName, MAC_ADDRESS);
        pairedDevices.add(newDevice);
        storeUser();
    }

    public ArrayList<Device> returnPairedDevices() { return pairedDevices; }

    public void storeUser() {
        //Getting reference to firebase
        DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference();
        //Adding to firebase (Replacing "." with "," because firebase doesn't allow child names with ".")
        firebaseReference.child("Users").child(this.email.replace(".",",")).setValue(this);
    }
}