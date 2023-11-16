package com.example.falldetectionapp.model;

import java.util.Random;

class EmergencyContact {

    public EmergencyContact() {}

    public String name;
    public String telephone;
    public String email;
    public int emContactID;

    public EmergencyContact(String name, String telephone, String email){
        this.name = name;
        this.telephone = telephone;
        this.email = email;

        Random random = new Random();
        this.emContactID = random.nextInt(1000);
    }
}