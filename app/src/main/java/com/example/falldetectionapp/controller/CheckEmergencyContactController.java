package com.example.falldetectionapp.controller;

import com.example.falldetectionapp.model.Program;

import java.util.ArrayList;

public class CheckEmergencyContactController {

    public String currentUserEmContactName = null;
    public String currentUserEmContactTelephone = null;
    public String currentUserEmContactEmail = null;

    public CheckEmergencyContactController(int specifiedEmergencyContact) {
        Program program = Program.getInstance();
        currentUserEmContactName = program.getCurrentUserEmContactsNames().get(specifiedEmergencyContact);
        currentUserEmContactTelephone = program.getCurrentUserEmContactsTelephones().get(specifiedEmergencyContact);
        currentUserEmContactEmail = program.getCurrentUserEmContactsEmails().get(specifiedEmergencyContact);
    }

    public void deleteEmergencyContact() {
        Program program = Program.getInstance();
        program.removeEmergencyContactFromUser(currentUserEmContactEmail);
    }
}
