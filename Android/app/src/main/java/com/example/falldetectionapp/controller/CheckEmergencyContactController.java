package com.example.falldetectionapp.controller;

import com.example.falldetectionapp.model.Program;

public class CheckEmergencyContactController {

    public String currentUserEmContactName;
    public String currentUserEmContactTelephone;
    public String currentUserEmContactEmail;

    //this constructor basically gets an Instance, and sets the details of the current User's EmergencyContacts
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
