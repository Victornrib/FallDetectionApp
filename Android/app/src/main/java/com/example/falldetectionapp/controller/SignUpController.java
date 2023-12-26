package com.example.falldetectionapp.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.falldetectionapp.model.Program;
import com.example.falldetectionapp.model.User;
import com.example.falldetectionapp.view.SignUpActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController {

    public String name;
    public String telephone;
    public String email;
    public String password;
    public String repeatedPassword;

    private boolean signUpFieldsValid;

    private String alertDialogMessage = "";

    public SignUpController(String name, String telephone, String email, String password, String repeatedPassword) {

        this.name = name;
        this.telephone = telephone;
        this.email = email;
        this.password = password;
        this.repeatedPassword = repeatedPassword;
    }

    //this method checks all registration fields
    public boolean signUpFieldsValid() {

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

        signUpFieldsValid = true;

        //series of if statements to check each field, and concatenate the alertDialogMessage
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

        //will return false if any field was not valid, will return true if all fields valid.
        return signUpFieldsValid;
    }

    public void checkExistingUser() {
        DatabaseReference firebaseUserReference = FirebaseDatabase.getInstance("https://fall-detection-83eed-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(email.replace(".",","));
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Program program = Program.getInstance();
                User potentialUser = dataSnapshot.getValue(User.class);
                SignUpActivity signUpActivity = (SignUpActivity) program.getCurrentActivity();

                if (potentialUser == null) {
                    signUpActivity.generateUserCheckDialogMessage(null);
                }
                else {
                    signUpActivity.generateUserCheckDialogMessage("Registration invalid. User with this email already exists.\n");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error", databaseError.getMessage()); //Don't ignore errors!
            }
        };
        firebaseUserReference.addListenerForSingleValueEvent(valueEventListener);
    }

    public String getAlertDialogMessage() {
        return alertDialogMessage;
    }

   public void storeNewUser() {
        User newUser = new User(name, telephone, email, password);
        Program.getInstance().setCurrentUser(newUser);
        newUser.storeUser();
    }

}
