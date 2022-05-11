package com.example.falldetectionapp.controller;

import com.example.falldetectionapp.model.SharedPrefs;
import com.example.falldetectionapp.model.User;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController {

    private String name;
    private String telephone;
    private String email;
    private String password;
    private String repeatedPassword;

    private boolean signUpValidated;

    private String alertDialogMessage = "";



    public SignUpController(String name, String telephone, String email, String password, String repeatedPassword) {

        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.password = password;
        this.repeatedPassword = repeatedPassword;
    }


    public boolean checkSignUpFields() {

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
        signUpValidated = true;

        if (name.equals("")) {
            alertDialogMessage = alertDialogMessage + "Registration invalid. Invalid name.\n";
            signUpValidated = false;
        }
        if (!telephoneMatcher.matches()) {
            alertDialogMessage = alertDialogMessage + "Registration invalid. Invalid telephone.\n";
            signUpValidated = false;
        }
        if (!emailMatcher.matches()) {
            alertDialogMessage = alertDialogMessage + "Registration invalid. Invalid email.\n";
            signUpValidated = false;
        }
        if (SharedPrefs.getString(email,null) != null) {
            alertDialogMessage = alertDialogMessage + "Registration invalid. User with this email already exists.\n";
            signUpValidated = false;
        }
        if (password.equals("")) {
            alertDialogMessage = alertDialogMessage + "Registration invalid. Invalid password.\n";
            signUpValidated = false;
        }
        if (!repeatedPassword.equals(password)) {
            alertDialogMessage = alertDialogMessage + "Registration invalid. Passwords do not match.\n";
            signUpValidated = false;
        }

        return signUpValidated;
    }


    public String getAlertDialogMessage() {

        if (signUpValidated) {
            alertDialogMessage = "The user " + name + " has been successfully registered on the system.";
        }
        return alertDialogMessage;
    }


    //Send signal to view. Find a way to communicate both ways.
    public void storeNewUser() {
        User newUser = new User(name, telephone, email, password);

        //Passing values to Shared Preferences
        Gson gson = new Gson();
        String json = gson.toJson(newUser);

        //Passing the user email as the key value from the 'user' field inside the json
        SharedPrefs.putString(newUser.email, json);
    }

}
