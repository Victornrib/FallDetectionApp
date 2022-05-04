package com.example.falldetectionapp.model;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;


public class User {
    public int userID;
    String name;
    String telephone;
    String email;
    String password;
    String wifiSSID;
    String wifiPassword;
    Time timeOfFall;
    boolean isConnected;
    boolean isEmContactVerified; //

    public User(String name, String telephone, String email, String password) {
        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.password = password;

        //Check in the system all ID's and guarantee it is a unique ID
        Random random = new Random();
        this.userID = random.nextInt(1000);
    }

    //activated when fall is detected
    public void receiveAlert(Time timeOfFall) {};

    //connects specific EmContact to specific user
    public void setEmContact(String name, String telephone, String email) {};

    //calls all methods needed to send data to firebase
    public void generateLog(Time timeOfFall) {};

    //connects app to device
    public void connectToDevice() {};

    //check the list of emcontacts for specific user
    public ArrayList<EmergencyContact> checkEmContacts() {
        return null;
    };

    //getter???
    public void EmContactDetails() {};

    //calls/texts the specific emcontact of a specific user
    public void alertEmContact() {};














}