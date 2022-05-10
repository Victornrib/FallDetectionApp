package com.example.falldetectionapp.controller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import com.example.falldetectionapp.model.SharedPrefs;
import com.example.falldetectionapp.model.User;
import com.example.falldetectionapp.view.InitialScreenActivity;
import com.example.falldetectionapp.view.SignUpScreenActivity;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController {

    public String name;
    public String telephone;
    public String email;
    public String password;
    public String repeatedPassword;

    boolean signUpValidated;

    public SignUpController(String name, String telephone, String email, String password, String repeatedPassword ) {

        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.password = password;
        this.repeatedPassword = repeatedPassword;

    }

    public void checkSignUpFields() {

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

        String errorMessage = "";

        //we should make this a boolean method and call it :)
        signUpValidated = true;

        if (name.equals("")) {
            errorMessage = errorMessage + "Registration invalid. Invalid name.\n";
            signUpValidated = false;
        }
        if (!telephoneMatcher.matches()) {
            errorMessage = errorMessage + "Registration invalid. Invalid telephone.\n";
            signUpValidated = false;
        }
        if (!emailMatcher.matches()) {
            errorMessage = errorMessage + "Registration invalid. Invalid email.\n";
            signUpValidated = false;
        }
        if (SharedPrefs.getString("User", email,null) != null) {
            errorMessage = errorMessage + "Registration invalid. User with this email already exists.\n";
            signUpValidated = false;
        }
        if (password.equals("")) {
            errorMessage = errorMessage + "Registration invalid. Invalid password.\n";
            signUpValidated = false;
        }
        if (!repeatedPassword.equals(password)) {
            errorMessage = errorMessage + "Registration invalid. Passwords do not match.\n";
            signUpValidated = false;
        }

        //Send signal to view. Find a way to communicate both ways.
        //Displaying success alert
        if (signUpValidated) {
            SignUpScreenActivity.alertDialog.setTitle("Registration successful");
            SignUpScreenActivity.alertDialog.setMessage("The user " + name + " has been successfully registered on the system.");
        }
        //if cannot create user, error alert should show up
        else {
            SignUpScreenActivity.alertDialog.setTitle("Error");
            SignUpScreenActivity.alertDialog.setMessage(errorMessage);
        }

        //If all fields are valid, the "Ok" button on the alertDialog will call the 'storeNewUser' function
        SignUpScreenActivity.alertDialog.show();
    }


    //Send signal to view. Find a way to communicate both ways.
    public static void storeNewUser() {
        User newUser = new User(name, telephone, email, password);

        //Passing values to Shared Preferences
        Gson gson = new Gson();
        String json = gson.toJson(newUser);

        //Passing the userID as the key value from the 'user' field inside the json
        SharedPrefs.putString("User", newUser.email, json);

        //Switch to next activity
        SignUpScreenActivity.displayDialog(); // how do we call the view
    }



}
