package com.example.falldetectionapp.controller;

import com.example.falldetectionapp.model.Program;

import java.util.ArrayList;


public class SettingsController {

    public ArrayList<String> currentUserEmContactNames = null;

    public SettingsController() {
        Program program = Program.getInstance();
        currentUserEmContactNames = program.getCurrentUserEmContactsNames();
    }

    public void switchAlertMode() {
        Program program = Program.getInstance();
        program.switchAlertModeFromCurrentUser();
    }

    public String getCurrentUserAlertMode() {
        Program program = Program.getInstance();
        return program.getCurrentUserAlertMode();
    }
}
