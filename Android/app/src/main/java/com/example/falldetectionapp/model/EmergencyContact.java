package com.example.falldetectionapp.model;

import java.util.Random;

public class EmergencyContact {

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

    public String getName() {
        return name;
    }
    public String getTelephone() {
        return telephone;
    }
    public String getEmail() {
        return email;
    }
    public int getEmContactID() {
        return emContactID;
    }
}