package com.example.falldetectionapp.controller;

//controller receives alert and then calls receiveAlert in program class
//handler handle message in activity called

//name variables needed here

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.falldetectionapp.model.EmergencyContact;
import com.example.falldetectionapp.model.Program;
import com.example.falldetectionapp.view.FallDetectedScreenActivity;

import java.time.LocalDateTime;

public class FallDetectedController {

    private LocalDateTime timeOfAlert;
    public boolean timeOfAlertValidated;
    public String timeOfAlertMessage;

    public FallDetectedController() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean checkTimeOfAlert() {
        Program program = Program.getInstance();
        timeOfAlert = program.receiveAlert();

        //not sure yet how to check /validate this so just not null for now.
        timeOfAlertValidated = timeOfAlert != null;
        return timeOfAlertValidated;
    }

    //needs certain version of API to run, may not work on older Android versions
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getTimeOfAlert(){
        if (timeOfAlertValidated) {
            timeOfAlertMessage = "A fall was detected at: " + timeOfAlert;
            }
        else timeOfAlertMessage = "No fall has been detected.";
        return timeOfAlertMessage;
    }



//    //code from https://www.youtube.com/watch?v=ofAL1C4jUJw
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            sendSMS();
//        } else {
//            //move to activity
//            Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
//        }
//    }

    //can you getInstance in each method or only once in the class? or is it even needed since it has already been called in checkAlert()
//    private void sendSMS() {
//        Program program = Program.getInstance();
//        SmsManager smsManager = SmsManager.getDefault();
//
//        for (int i = 0; i < program.getCurrentUser().getEmContacts().size(); i++) {
//            EmergencyContact currentEmContact = program.getCurrentUser().getEmContacts().get(i);
//            String emContactPhoneNumber = currentEmContact.telephone;
//            String message = "Attention " + currentEmContact.name + ": " + program.getCurrentUser().name + " has had a fall!\nPlease take action now!";
//            smsManager.sendTextMessage(emContactPhoneNumber, null, message, null, null);
//        }
//
//        //move this back to activity
//        Toast.makeText(this, "SMS sent to all emergency contacts", Toast.LENGTH_LONG).show();
//    }
}


//call function in controller that calls receiveAlert
//get time from receiveAlert function
//passes the time to the fall detected screen
//getTime(){call receiveAlert from program, program returns time to cont, cont pass time to view, view updated text}