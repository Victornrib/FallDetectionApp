package com.example.falldetectionapp.controller;

import com.example.falldetectionapp.model.SharedPrefs;
import com.example.falldetectionapp.model.User;
import com.google.gson.Gson;
import com.example.falldetectionapp.model.Program;

public class SignInController {

    public String email;
    public String password;
    public User currentUser;

    private String alertDialogErrorMessage;


    public SignInController(String email, String password) {

        this.email = email;
        this.password = password;
    }


    public boolean checkSignInFields() {
        currentUser = getUser();
        Program program = Program.getInstance();
        program.setCurrentUser(currentUser);
        //returns true if sign in was valid.
        return currentUser != null;
    }


    public String getAlertDialogErrorMessage() {
        return alertDialogErrorMessage;
    }


    private User getUser() {

        Gson gson = new Gson();
        String jsonRet = SharedPrefs.getString(email,null);

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
