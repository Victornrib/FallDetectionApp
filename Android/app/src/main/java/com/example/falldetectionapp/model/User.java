package com.example.falldetectionapp.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.falldetectionapp.model.fallStats.RecordedFall;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;


class User {
    public int userID;
    public String name;
    public String telephone;
    public String email;
    public String password;

    public String sex;
    public String birthDate;
    public Integer age;
    public ArrayList<String> movementDisorders;

    public ArrayList<EmergencyContact> emContacts = new ArrayList<EmergencyContact>();
    public ArrayList<Device> pairedDevices = new ArrayList<Device>();
    public ArrayList<RecordedFall> recordedFalls = new ArrayList<RecordedFall>();
    public String alertMode = "SMS";

    public User() {}

    public User(String name, String telephone, String email, String password) {
        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.password = password;

        Random random = new Random();
        this.userID = random.nextInt(1000);
    }

    private Integer generateFallId() {
        ArrayList usedIds = new ArrayList<>();
        for(int i = 0; i < recordedFalls.size(); i++)
            usedIds.add(recordedFalls.get(i).fallId);

        int sortedId;
        Random random = new Random();
        do { sortedId = random.nextInt(100000);
        } while (usedIds.contains(sortedId));

        return (Integer) sortedId;
    }

    public void addInfo(String sex, String birthDate, Integer age, ArrayList<String> movementDisorders) {
        this.sex = sex;
        this.birthDate = birthDate;
        this.age = age;
        this.movementDisorders = movementDisorders;
        storeUser();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addRecordedFall(LocalDateTime fallDateTime, LatLng latLng) {
        Integer fallId = generateFallId();
        RecordedFall recordedFall = new RecordedFall(fallId, fallDateTime, latLng);
        recordedFalls.add(recordedFall);
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

    public void switchAlertMode() {
        if (Objects.equals(this.alertMode, "SMS")) {
            this.alertMode = "Call";
        }
        else if (Objects.equals(this.alertMode, "Call")) {
            this.alertMode = "SMS";
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
        DatabaseReference firebaseReference = FirebaseDatabase.getInstance("https://fall-detection-83eed-default-rtdb.europe-west1.firebasedatabase.app").getReference();
        //Adding to firebase (Replacing "." with "," because firebase doesn't allow child names with ".")
        firebaseReference.child("Users").child(this.email.replace(".",",")).setValue(this);
    }
}