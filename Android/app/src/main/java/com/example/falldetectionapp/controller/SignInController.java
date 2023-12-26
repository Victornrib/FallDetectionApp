package com.example.falldetectionapp.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.falldetectionapp.model.Program;
import com.example.falldetectionapp.model.User;
import com.example.falldetectionapp.view.SignInActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInController {

    public String email;
    public String password;

    public SignInController(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void signIn() {

        DatabaseReference firebaseUserReference = FirebaseDatabase.getInstance("https://fall-detection-83eed-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Users").child(email.replace(".",","));
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Program program = Program.getInstance();
                User potentialUser = dataSnapshot.getValue(User.class);
                SignInActivity signInActivity = (SignInActivity) program.getCurrentActivity();

                if (potentialUser == null) {
                    signInActivity.generateErrorDialog("Email not found.");
                }
                else {
                    if (potentialUser.password.equals(password)) {
                        program.setCurrentUser(potentialUser);
                        signInActivity.openHomeActivity();
                    }
                    else {
                        signInActivity.generateErrorDialog("Password is wrong.");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Error", databaseError.getMessage()); //Don't ignore errors!
            }
        };
        firebaseUserReference.addListenerForSingleValueEvent(valueEventListener);
    }
}
