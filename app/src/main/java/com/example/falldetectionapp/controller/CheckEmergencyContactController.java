package com.example.falldetectionapp.controller;

import com.example.falldetectionapp.model.Program;

import java.util.ArrayList;

public class CheckEmergencyContactController {

    public ArrayList<String> currentUserEmContactNames = null;
    public ArrayList<String> currentUserEmContactTelephones = null;
    public ArrayList<String> currentUserEmContactEmails = null;

    public CheckEmergencyContactController() {
        Program program = Program.getInstance();
        currentUserEmContactNames = program.getCurrentUserEmContactsNames();
        currentUserEmContactTelephones = program.getCurrentUserEmContactsTelephones();
        currentUserEmContactEmails = program.getCurrentUserEmContactsEmails();
    }
}
