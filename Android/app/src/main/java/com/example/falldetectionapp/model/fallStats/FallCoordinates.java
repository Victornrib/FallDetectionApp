package com.example.falldetectionapp.model.fallStats;

import com.google.android.gms.maps.model.LatLng;

public class FallCoordinates {

    public double latitude;
    public double longitude;

    public FallCoordinates(){}

    public FallCoordinates(LatLng latLng) {
        this.latitude = latLng.latitude;
        this.longitude = latLng.longitude;
    }
}
