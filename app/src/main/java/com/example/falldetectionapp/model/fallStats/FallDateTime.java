package com.example.falldetectionapp.model.fallStats;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

public class FallDateTime {

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
        String fallDateTimeString =  month + " " + dayOfMonth + ", " + year;
        return fallDateTimeString;
    }
}
