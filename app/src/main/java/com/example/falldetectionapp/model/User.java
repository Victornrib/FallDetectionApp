package com.example.falldetectionapp.model;
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
    String wifiSSID;
    String wifiPassword;
    Time timeOfFall;
    boolean isConnected;
    boolean isEmContactVerified; //
    private ArrayList<EmergencyContact> emContacts;

    public User(String name, String telephone, String email, String password) {
        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.password = password;
        this.emContacts = new ArrayList<EmergencyContact>();

        //need to add check in the system all ID's and guarantee it is a unique ID
        Random random = new Random();
        this.userID = random.nextInt(1000);
    }

    //connects specific EmContact to specific user
    public void addEmContact(String name, String telephone, String email) {
        EmergencyContact newEmContact = new EmergencyContact(name, telephone, email);
        emContacts.add(newEmContact);

        //Passing values to Shared Preferences
        Gson gson = new Gson();
        String json = gson.toJson(this);
        SharedPrefs.putString(this.email,json);
    };

    //check the list of emContacts for specific user
    public ArrayList<EmergencyContact> getEmContacts() {
        return emContacts;
    };


    public void alertEmContact() {};














}