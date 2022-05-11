package com.example.falldetectionapp.controller;

import com.example.falldetectionapp.model.SharedPrefs;
import com.example.falldetectionapp.model.User;
import com.google.gson.Gson;


public class SignInController {

    String email;
    String password;
    User currentUser;

    private String alertDialogErrorMessage;


    public SignInController(String email, String password) {

        this.email = email;
        this.password = password;
    }


    public boolean checkSignInFields() {
        currentUser = getUser();

        //Will pass the current user to the 'Program' class

        return currentUser != null;
    }


    public String getAlertDialogErrorMessage() {
        return alertDialogErrorMessage;
    }


    private User getUser() {

        Gson gson = new Gson();
        String jsonRet = SharedPrefs.getString("User", email,null);

        if (jsonRet != null) {
            currentUser = gson.fromJson(jsonRet, User.class);

            if (currentUser.password.equals(password)) {
                return currentUser;
            }
            else {
                alertDialogErrorMessage = "Password wrong.";
                return null;
            }
        }
        else {
            alertDialogErrorMessage = "Email not found.";
            return null;
        }
    }

}
