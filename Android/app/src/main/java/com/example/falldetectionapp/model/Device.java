package com.example.falldetectionapp.model;

public class Device {

    public String deviceName;
    public String macAddress;

    public Device() {}

    public Device(String deviceName, String macAddress){
        this.deviceName = deviceName;
        this.macAddress = macAddress;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getMacAddress() {
        return macAddress;
    }
}