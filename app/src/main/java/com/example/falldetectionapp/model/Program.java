package com.example.falldetectionapp.model;

import com.google.gson.Gson;

import java.sql.Time;

public class Program {
    static Program program;
    private User currentUser;

    private Program() {

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

//    private User getUser(String email, String password) {
//
//        Gson gson = new Gson();
//        String jsonRet = SharedPrefs.getString("User", email,null);
//
//        if (jsonRet != null) {
//            currentUser = gson.fromJson(jsonRet, User.class);
//
//            if (currentUser.password.equals(password)) {
//                return currentUser;
//            }
//            else {
//                alertDialogErrorMessage = "Password wrong.";
//                return null;
//            }
//        }
//        else {
//            alertDialogErrorMessage = "Email not found.";
//            return null;
//        }
//    }


    //calls all methods needed to send data to firebase
    //public void generateLog(Time timeOfFall) {};

    //connects app to device
    //public void connectToDevice() {};

    //activated when fall is detected
    //activated by controller right? -Gwen
    public void receiveAlert(Time timeOfFall) {
        //if (Device.fallDetected == true) { putString(Device, date, time) }
    };
}
