package com.example.falldetectionapp.controller;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.falldetectionapp.model.Program;

public class FallDetectedController {

    public String fallTime;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public FallDetectedController() {
        Program program = Program.getInstance();
        fallTime = program.getFallTime();
    }

    public String getCurrentUserAlertMode() {
        Program program = Program.getInstance();
        return program.getCurrentUserAlertMode();
    }

    public void callEmergencyContactsFromUser() {
        Program program = Program.getInstance();
        program.callEmContacts();
    }

}
