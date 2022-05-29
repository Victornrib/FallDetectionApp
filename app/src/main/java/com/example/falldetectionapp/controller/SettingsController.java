package com.example.falldetectionapp.controller;

import com.example.falldetectionapp.model.Program;

import java.util.ArrayList;


public class SettingsController {

    public ArrayList<String> currentUserEmContactNames = null;

    public SettingsController() {
        Program program = Program.getInstance();
        currentUserEmContactNames = program.getCurrentUserEmContactsNames();
    }
}
