package com.example.falldetectionapp.model;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPrefs {
    public static SharedPreferences localDisk;
    public static SharedPreferences localDiskEditor;

    public void startLocalDisk() {
        //localDisk = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor localDiskEditor = localDisk.edit();
    }
}

