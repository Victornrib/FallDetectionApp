package com.example.falldetectionapp.controller;


import com.example.falldetectionapp.model.EmergencyContact;
import com.example.falldetectionapp.model.Program;
import com.example.falldetectionapp.model.SharedPrefs;
import com.example.falldetectionapp.model.User;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class EmergencyContactController {

    public String name;
    public String telephone;
    public String email;

    public boolean emergencyContactValidated;
    public String alertDialogMessage = "";


    public EmergencyContactController(String name, String telephone, String email) {
        this.name = name;
        this.telephone = telephone;
        this.email = email;
    }


    public boolean checkContactFields() {

        //Regular expression to validate telephone
        //  "^[0-9]{8,9}$" -- For standard format (ex: 12345678 or 123456789)
        //  "^\+(?:[0-9] ?){6,14}[0-9]$" -- For international format (ex: +0133557799)
        String telephoneRegex = "(^\\+(?:[0-9] ?){6,14}[0-9]$)|(^[0-9]{8,9}$)";
        Pattern telephonePattern = Pattern.compile(telephoneRegex);
        Matcher telephoneMatcher = telephonePattern.matcher(telephone);

        //Regular expression to validate email
        //Format "XXX@YYY.ZZZ"
        String emailRegex = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher emailMatcher = emailPattern.matcher(email);

        emergencyContactValidated = true;

        if (name.equals("")) {
            //cant it just be alertDialogMessage = "invalid name \n"
            alertDialogMessage = alertDialogMessage + "Invalid name.\n";
            emergencyContactValidated = false;
        }
        if (!telephoneMatcher.matches()) {
            alertDialogMessage = alertDialogMessage + "Invalid telephone.\n";
            emergencyContactValidated = false;
        }
        /*
        if (SharedPrefs.getString("EmergencyContact", email,null) != null) {
            alertDialogMessage = alertDialogMessage + "Registration invalid. Emergency Contact with this email already exists.\n";
            emergencyContactValidated = false;
        }
        /*
         */
        if (!emailMatcher.matches()) {
            alertDialogMessage = alertDialogMessage + "Invalid email.\n";
            emergencyContactValidated = false;
        }

        return emergencyContactValidated;
    }


    public String getAlertDialogMessage() {
        if (emergencyContactValidated) {
            alertDialogMessage = "The emergency contact " + name + " has been successfully registered on the system.";
        }
        return alertDialogMessage;
    }


    public void addNewEmergencyContact() {
        Program program = Program.getInstance();
        User currentUser = program.getCurrentUser();
        currentUser.addEmContact(name, telephone, email);
    }


}
