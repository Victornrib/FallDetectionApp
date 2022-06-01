package com.example.falldetectionapp.model;

class Device {

    public String deviceName;
    public String macAddress;

    public Device() {}

    public Device(String deviceName, String macAddress){
        this.deviceName = deviceName;
        this.macAddress = macAddress;
    }
}