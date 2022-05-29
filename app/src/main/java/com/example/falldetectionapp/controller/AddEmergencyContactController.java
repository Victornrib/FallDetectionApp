package com.example.falldetectionapp.controller;

import com.example.falldetectionapp.model.Program;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class AddEmergencyContactController {

    public String name;
    public String telephone;
    public String email;

    private boolean emergencyContactFieldsValid;
    public String alertDialogMessage = "";


    public AddEmergencyContactController(String name, String telephone, String email) {
        this.name = name;
        this.telephone = telephone;
        this.email = email;
    }


    public boolean emergencyContactFieldsValid() {

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

        emergencyContactFieldsValid = true;

        if (name.equals("")) {
            alertDialogMessage = alertDialogMessage + "Invalid name.\n";
            emergencyContactFieldsValid = false;
        }
        if (!telephoneMatcher.matches()) {
            alertDialogMessage = alertDialogMessage + "Invalid telephone.\n";
            emergencyContactFieldsValid = false;
        }
        if (!emailMatcher.matches()) {
            alertDialogMessage = alertDialogMessage + "Invalid email.\n";
            emergencyContactFieldsValid = false;
        }

        return emergencyContactFieldsValid;
    }

    public void checkExistingEmergencyContact() {
        Program program = Program.getInstance();
        program.checkExistingEmergencyContact(email);
    }


    public String getAlertDialogMessage() {
        if (emergencyContactFieldsValid) {
            alertDialogMessage = "The emergency contact " + name + " has been successfully registered on the system.";
        }
        return alertDialogMessage;
    }


    public void storeNewEmergencyContact() {
        Program program = Program.getInstance();
        program.addEmergencyContactToCurrentUser(name, telephone, email);
    }


}
