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

    //check all fields
    public boolean emergencyContactFieldsValid() {

        //Regular expression to check telephone. String must be between 8-11 characters, only certain numbers/symbols allowed.
        //  "^[0-9]{8,9}$" -- For standard format (ex: 12345678 or 123456789)
        //  "^\+(?:[0-9] ?){6,14}[0-9]$" -- For international format (ex: +0133557799)
        String telephoneRegex = "(^\\+(?:[0-9] ?){6,14}[0-9]$)|(^[0-9]{8,9}$)";
        Pattern telephonePattern = Pattern.compile(telephoneRegex);
        Matcher telephoneMatcher = telephonePattern.matcher(telephone);

        //Checks that format of email is  "XXX@YYY.ZZZ", in order to validate email.
        String emailRegex = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher emailMatcher = emailPattern.matcher(email);

        emergencyContactFieldsValid = true;

        //series of if statements to check each field, and concatenate the alertDialogMessage
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

        //will return false if any field was not valid, will return true if all fields valid.
        return emergencyContactFieldsValid;
    }

    public void checkExistingEmergencyContact() {
        Program program = Program.getInstance();
        program.checkExistingEmergencyContact(email);
    }

    //checks boolean to determine the alertDialogMessage
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
