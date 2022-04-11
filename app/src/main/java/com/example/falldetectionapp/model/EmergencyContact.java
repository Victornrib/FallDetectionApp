package com.example.falldetectionapp.model;

import java.util.Random;

public class EmergencyContact {
    String name;
    String telephone;
    String email;
    int emContactID;

    public EmergencyContact(String name, String telephone, String email){
        this.name = name;
        this.telephone = telephone;
        this.email = email;

        Random random = new Random();
        this.emContactID = random.nextInt(1000);
    }
}