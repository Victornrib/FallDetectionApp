package com.example.falldetectionapp.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.LatLng;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

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

    public static class FallDateTime {

        public int year;
        public int monthValue;
        public int hour;
        public int minute;
        public int second;
        public int nano;
        public int dayOfYear;
        public int dayOfMonth;
        public String month;
        public String dayOfWeek;

        public FallDateTime() {}

        @RequiresApi(api = Build.VERSION_CODES.O)
        public FallDateTime(LocalDateTime localDateTime) {
            this.year = localDateTime.getYear();
            this.monthValue = localDateTime.getMonthValue();
            this.hour = localDateTime.getHour();
            this.minute = localDateTime.getMinute();
            this.second = localDateTime.getSecond();
            this.nano = localDateTime.getNano();

            this.dayOfYear = localDateTime.getDayOfYear();
            this.dayOfMonth = localDateTime.getDayOfMonth();

            this.month = localDateTime.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            this.dayOfWeek = localDateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        }

        public String getFallDateTimeString() {
            String fallDateTimeString =  month + " " + dayOfMonth + ", " + year + " - " + hour + ":" + minute + ":" + second;
            return fallDateTimeString;
        }
    }

    public static class FallCoordinates {

        public double latitude;
        public double longitude;

        public FallCoordinates(){}

        public FallCoordinates(LatLng latLng) {
            this.latitude = latLng.latitude;
            this.longitude = latLng.longitude;
        }
    }
}
