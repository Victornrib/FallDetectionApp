package com.example.falldetectionapp.controller;

import com.example.falldetectionapp.model.EmergencyContact;
import com.example.falldetectionapp.model.Program;

import java.util.ArrayList;


public class SettingsController {

    public ArrayList<String> currentUserEmContactNames = null;

    public SettingsController() {
        Program program = Program.getInstance();
        ArrayList<EmergencyContact> currentUserEmContacts = program.getCurrentUser().getEmContacts();
        currentUserEmContactNames = getEmContactsNames(currentUserEmContacts);
    }

    private ArrayList<String> getEmContactsNames(ArrayList<EmergencyContact> userEmContacts) {
        ArrayList<String> currentUserEmContactsNames = new ArrayList<String>();
        if (userEmContacts != null) {
            for (int i = 0; i < userEmContacts.size(); i++) {
                currentUserEmContactsNames.add(userEmContacts.get(i).getName());
            }
        }
        return currentUserEmContactsNames;
    }

    public void switchCurrentUserAlertMode() {
        Program.getInstance().getCurrentUser().switchAlertMode();
    }

    public String getCurrentUserAlertMode() {
        Program program = Program.getInstance();
        return program.getCurrentUser().getAlertMode();
    }
}
