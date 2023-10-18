package com.example.falldetectionapp.model.fallStats;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.LatLng;

import java.time.LocalDateTime;

public class RecordedFall {

    public Integer fallId;
    public FallDateTime fallDateTime ;
    public FallCoordinates latLng;
    public String description = null;

    public RecordedFall() {}

    @RequiresApi(api = Build.VERSION_CODES.O)
    public RecordedFall(Integer fallId, LocalDateTime localDateTime, LatLng latLng) {
        this.fallId = fallId;
        this.fallDateTime = new FallDateTime(localDateTime);
        this.latLng = new FallCoordinates(latLng);
    }

    public void addDescription(String description) {
        this.description = description;
    }
}
