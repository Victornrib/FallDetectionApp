package com.example.falldetectionapp.controller;

import com.example.falldetectionapp.model.Program;

import java.util.ArrayList;


public class SettingsController {

    public ArrayList<String> currentUserEmContactNames = null;

    //This constructor gets an Instance of the Program and fills the ArrayList by calling a method in the program.
    public SettingsController() {
        Program program = Program.getInstance();
        currentUserEmContactNames = program.getCurrentUserEmContactsNames();
    }
}
