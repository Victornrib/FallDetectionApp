package com.example.falldetectionapp.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.falldetectionapp.model.SharedPrefs;
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


    public SignInController(String email, String password) {
        this.email = email;
        this.password = password;
    }

    //Singleton method
    public void signIn() {
        Program program = Program.getInstance();
        program.signIn(email, password);
    }
}
