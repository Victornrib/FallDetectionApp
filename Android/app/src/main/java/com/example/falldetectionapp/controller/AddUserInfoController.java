package com.example.falldetectionapp.controller;

import com.example.falldetectionapp.model.Program;
import com.example.falldetectionapp.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AddUserInfoController {
    public ArrayList<String> movementDisorders = new ArrayList<String>();
    public String sex;
    public static String birthDate;
    public Integer age;

    public void addUserInfo() {
        Program program = Program.getInstance();
        User currentUser = program.getCurrentUser();
        currentUser.addInfo(sex, birthDate, age, movementDisorders);
    }

    public static void setUserBirthDate(Calendar calendarBirthDate) {
        Date birthDateRaw = calendarBirthDate.getTime();
        DateFormat formatter = SimpleDateFormat.getDateInstance(DateFormat.LONG, Locale.ENGLISH);
        birthDate = formatter.format(birthDateRaw);
    }

    public void addUserMovementDisorder(String movementDisorder) {
        movementDisorders.add(movementDisorder);
    }

    public void setUserSex(String sex) {
        this.sex = sex;
    }

    public void calculateUserAge(Calendar userCalendarBirthDate) {
        Calendar calendarCurrentDate = new GregorianCalendar();
        age = calendarCurrentDate.get(Calendar.YEAR) - userCalendarBirthDate.get(Calendar.YEAR);
    }

    public String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        year = year - 50;
        return makeDateString(day, month, year);
    }

    public String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }
}
