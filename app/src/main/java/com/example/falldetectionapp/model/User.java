package com.example.falldetectionapp.model;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;
import com.example.falldetectionapp.model.SharedPrefs;
import com.google.gson.Gson;

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
    //ArrayList<Integer> EmContactsIDs;
    public ArrayList<String> emContactEmails;

    public User(String name, String telephone, String email, String password) {
        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.password = password;

        //need to add check in the system all ID's and guarantee it is a unique ID
        Random random = new Random();
        this.userID = random.nextInt(1000);
    }

    //activated when fall is detected
    //activated by controller right? -Gwen
    public void receiveAlert(Time timeOfFall) {
        //if (Device.fallDetected == true) { putString(Device, date, time) }
    };

    //connects specific EmContact to specific user
    public void setEmContact(String name, String telephone, String email) {
        //adds emContact to user arraylist
        //returns new list?
    };

    //calls all methods needed to send data to firebase
    public void generateLog(Time timeOfFall) {};

    //connects app to device
    public void connectToDevice() {};

    //check the list of emcontacts for specific user
//    public ArrayList<EmergencyContact> checkEmContacts() {
//        ArrayList<EmergencyContact> emContactsList = new ArrayList<EmergencyContact>();
//        Gson gson = new Gson();
//
//        for (int i; i < emContactEmails.size(); i ++) {
//            String jsonRet = SharedPrefs.getString("EmergencyContact", emContactEmails.get(i), null);
//            if (jsonRet != null) {
//                EmergencyContact currentEmContact = gson.fromJson(jsonRet, EmergencyContact.class);
//                emContactsList.add(currentEmContact);
//            }
//        }
//
//        return emContactsList;
//
//    };

    //getter???
    //need this in order to contact the emContact
    public void EmContactDetails() {};

    //calls/texts the specific emcontact of a specific user
    //Could be EMAIL?? is that easier? -gwen
    public void alertEmContact() {};














}