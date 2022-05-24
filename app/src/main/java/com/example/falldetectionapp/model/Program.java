package com.example.falldetectionapp.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import java.sql.Time;

public class Program {
    private static Program program;
    private User currentUser;
    private boolean fallDetected = false;

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

    //returns date and time of alert
    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDateTime receiveAlert() {
        fallDetected = true;

        //Get time
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime currentTime = LocalDateTime.now();
        System.out.println(dtf.format(currentTime));
        return currentTime;
    };
}
