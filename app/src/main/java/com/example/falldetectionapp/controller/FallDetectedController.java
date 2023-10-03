package com.example.falldetectionapp.controller;

import com.example.falldetectionapp.model.Program;

public class FallDetectedController {

    public String fallTime;

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
