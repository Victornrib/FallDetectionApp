package com.example.falldetectionapp.controller;

import com.example.falldetectionapp.model.EmergencyContact;
import com.example.falldetectionapp.model.Program;

import java.util.ArrayList;

public class CheckEmergencyContactController {

    public String currentUserEmContactName;
    public String currentUserEmContactTelephone;
    public String currentUserEmContactEmail;

    public CheckEmergencyContactController(int specifiedEmergencyContact) {
        Program program = Program.getInstance();
        ArrayList<EmergencyContact> currentUserEmContacts = program.getCurrentUser().getEmContacts();

        currentUserEmContactName = currentUserEmContacts.get(specifiedEmergencyContact).getName();
        currentUserEmContactTelephone = currentUserEmContacts.get(specifiedEmergencyContact).getTelephone();
        currentUserEmContactEmail = currentUserEmContacts.get(specifiedEmergencyContact).getEmail();
    }

    public void deleteEmergencyContact() {
        Program program = Program.getInstance();
        program.getCurrentUser().removeEmContact(currentUserEmContactEmail);
    }
}
