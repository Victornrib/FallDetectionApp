package com.example.falldetectionapp.model;


public class Device {
    public String DeviceName;
    public String MAC_ADDRESS;

    public Device() {}

    public Device(String DeviceName, String MAC_ADDRESS){    //parameters missing?
        this.DeviceName = DeviceName;
        this.MAC_ADDRESS = MAC_ADDRESS;
    }
}