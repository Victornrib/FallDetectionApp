package com.example.falldetectionapp.controller;

import android.util.Log;

import com.example.falldetectionapp.model.Program;
import com.example.falldetectionapp.model.SharedPrefs;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController {

    private String name;
    private String telephone;
    private String email;
    private String password;
    private String repeatedPassword;

    private boolean signUpFieldsValid;

    private String alertDialogMessage = "";



    public SignUpController(String name, String telephone, String email, String password, String repeatedPassword) {

        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.password = password;
        this.repeatedPassword = repeatedPassword;
    }


    public boolean signUpFieldsValid() {

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

        //we should make this a boolean method and call it :)
        signUpFieldsValid = true;

        if (name.equals("")) {
            alertDialogMessage = alertDialogMessage + "Registration invalid. Invalid name.\n";
            signUpFieldsValid = false;
        }
        if (!telephoneMatcher.matches()) {
            alertDialogMessage = alertDialogMessage + "Registration invalid. Invalid telephone.\n";
            signUpFieldsValid = false;
        }
        if (!emailMatcher.matches()) {
            alertDialogMessage = alertDialogMessage + "Registration invalid. Invalid email.\n";
            signUpFieldsValid = false;
        }
        if (password.equals("")) {
            alertDialogMessage = alertDialogMessage + "Registration invalid. Invalid password.\n";
            signUpFieldsValid = false;
        }
        if (!repeatedPassword.equals(password)) {
            alertDialogMessage = alertDialogMessage + "Registration invalid. Passwords do not match.\n";
            signUpFieldsValid = false;
        }

        return signUpFieldsValid;
    }

    public void checkExistingUser() {
        Program program = Program.getInstance();
        program.checkExistingUser(email);
    }


    public String getAlertDialogMessage() {
        return alertDialogMessage;
    }


    public void storeNewUser() {
        Program program = Program.getInstance();
        program.storeNewUser(name, telephone, email, password);
    }

}
