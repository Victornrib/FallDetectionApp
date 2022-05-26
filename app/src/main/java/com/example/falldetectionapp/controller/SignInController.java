package com.example.falldetectionapp.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.falldetectionapp.model.SharedPrefs;
import com.example.falldetectionapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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


    public User getUser() {

        DatabaseReference firebaseUserReference = FirebaseDatabase.getInstance().getReference("Users").child(email.replace(".",","));
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                System.out.println(currentUser.email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error", databaseError.getMessage()); //Don't ignore errors!
            }
        };
        firebaseUserReference.addListenerForSingleValueEvent(valueEventListener);


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
