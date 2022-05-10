package com.example.falldetectionapp.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Map;

public class SharedPrefs {

    private static SharedPreferences sharedPreferencesUser;
    private static SharedPreferences sharedPreferencesEmContact;
    private static SharedPreferences sharedPreferencesDevice;


    //Needs to declare a custom constructor cause it has to be static
    public static void init(Context context)
    {
        if (sharedPreferencesUser == null)
            sharedPreferencesUser = context.getSharedPreferences(context.getPackageName()+"_user_preferences", Activity.MODE_PRIVATE);

        if (sharedPreferencesEmContact == null)
            sharedPreferencesEmContact = context.getSharedPreferences(context.getPackageName()+"_emergency_contact_preferences", Activity.MODE_PRIVATE);

        if (sharedPreferencesDevice == null)
            sharedPreferencesDevice = context.getSharedPreferences(context.getPackageName()+"_device_preferences", Activity.MODE_PRIVATE);
    }
    //are these if/else statements? and is package name fx "model"? - gwen


    public static String getString(String entity, String key, String defValue) { //defValue is "default" or "defined"? -gwen -- It is the default value. I explained in a comment in the SignInScreenActivity class

        switch (entity) {

            case ("User"):
                return sharedPreferencesUser.getString(key, defValue);

            case ("EmergencyContact"):
                return sharedPreferencesEmContact.getString(key, defValue);

            case ("Device"):
                return sharedPreferencesDevice.getString(key, defValue);

            default:
                return null;
        }
    }


    public static void putString(String entity, String key, String value) {

        switch (entity) {

            case ("User"): {
                SharedPreferences.Editor prefsEditorUser = sharedPreferencesUser.edit();
                prefsEditorUser.putString(key, value);
                prefsEditorUser.apply();
                break;
            }

            case ("EmergencyContact"): {
                SharedPreferences.Editor prefsEditorEmContact = sharedPreferencesEmContact.edit();
                prefsEditorEmContact.putString(key, value);
                prefsEditorEmContact.apply();
                break;
            }

            case ("Device"): {
                SharedPreferences.Editor prefsEditorDevice = sharedPreferencesDevice.edit();
                prefsEditorDevice.putString(key, value);
                prefsEditorDevice.apply();
                break;
            }
        }
    }


    //// Clear Preference ////
    public static void clearPreference(String entity, Context context) {

        switch (entity) {

            case ("User"): {
                sharedPreferencesUser.edit().clear().apply();
                break;
            }

            case ("EmergencyContact"): {
                sharedPreferencesEmContact.edit().clear().apply();
                break;
            }

            case ("Device"): {
                sharedPreferencesDevice.edit().clear().apply();
                break;
            }
        }
    }

    //// Remove ////
    //for example to remove a user who has stopped with the device, or remove emergency contacts... -- Something like that...
    public static void removePreference(String entity, String Key){

        switch (entity) {

            case ("User"): {
                sharedPreferencesUser.edit().remove(Key).apply();
                break;
            }

            case ("EmergencyContact"): {
                sharedPreferencesEmContact.edit().remove(Key).apply();
                break;
            }

            case ("Device"): {
                sharedPreferencesDevice.edit().remove(Key).apply();
                break;
            }
        }
    }
}



/*

public static Integer getInteger(String entity, String key, int defValue) {

        switch (entity) {

            case ("User"):
                return sharedPreferencesUser.getInt(key, defValue);

            case ("EmergencyContact"):
                return sharedPreferencesEmContact.getInt(key, defValue);

            case ("Device"):
                return sharedPreferencesDevice.getInt(key, defValue);

            default:
                return null;
        }
    }


    public static void putInteger(String entity, String key, Integer value) {

        switch (entity) {

            case ("User"): {
                SharedPreferences.Editor prefsEditorUser = sharedPreferencesUser.edit();
                prefsEditorUser.putInt(key, value);
                prefsEditorUser.apply();
                break;
            }

            case ("EmergencyContact"): {
                SharedPreferences.Editor prefsEditorEmContact = sharedPreferencesEmContact.edit();
                prefsEditorEmContact.putInt(key, value);
                prefsEditorEmContact.apply();
                break;
            }

            case ("Device"): {
                SharedPreferences.Editor prefsEditorDevice = sharedPreferencesDevice.edit();
                prefsEditorDevice.putInt(key, value);
                prefsEditorDevice.apply();
                break;
            }
        }
    }


    public static Boolean getBoolean(String entity, String key, boolean defValue) {

        switch (entity) {

            case ("User"):
                return sharedPreferencesUser.getBoolean(key, defValue);

            case ("EmergencyContact"):
                return sharedPreferencesEmContact.getBoolean(key, defValue);

            case ("Device"):
                return sharedPreferencesDevice.getBoolean(key, defValue);

            default:
                return null;
        }
    }


    public static void putBoolean(String entity, String key, boolean value) {

        switch (entity) {

            case ("User"): {
                SharedPreferences.Editor prefsEditorUser = sharedPreferencesUser.edit();
                prefsEditorUser.putBoolean(key, value);
                prefsEditorUser.apply();
                break;
            }

            case ("EmergencyContact"): {
                SharedPreferences.Editor prefsEditorEmContact = sharedPreferencesEmContact.edit();
                prefsEditorEmContact.putBoolean(key, value);
                prefsEditorEmContact.apply();
                break;
            }

            case ("Device"): {
                SharedPreferences.Editor prefsEditorDevice = sharedPreferencesDevice.edit();
                prefsEditorDevice.putBoolean(key, value);
                prefsEditorDevice.apply();
                break;
            }
        }
    }


    public static Long getLong(String entity, String key, long defValue) {

        switch (entity) {

            case ("User"):
                return sharedPreferencesUser.getLong(key, defValue);

            case ("EmergencyContact"):
                return sharedPreferencesEmContact.getLong(key, defValue);

            case ("Device"):
                return sharedPreferencesDevice.getLong(key, defValue);

            default:
                return null;
        }
    }


    public static void putLong(String entity, String key, long value) {

        switch (entity) {

            case ("User"): {
                SharedPreferences.Editor prefsEditorUser = sharedPreferencesUser.edit();
                prefsEditorUser.putLong(key, value);
                prefsEditorUser.apply();
            }

            case ("EmergencyContact"): {
                SharedPreferences.Editor prefsEditorEmContact = sharedPreferencesEmContact.edit();
                prefsEditorEmContact.putLong(key, value);
                prefsEditorEmContact.apply();
            }

            case ("Device"): {
                SharedPreferences.Editor prefsEditorDevice = sharedPreferencesDevice.edit();
                prefsEditorDevice.putLong(key, value);
                prefsEditorDevice.apply();
            }
        }
    }


    public static Float getFloat(String entity, String key, float defValue) {

        switch (entity) {

            case ("User"):
                return sharedPreferencesUser.getFloat(key, defValue);

            case ("EmergencyContact"):
                return sharedPreferencesEmContact.getFloat(key, defValue);

            case ("Device"):
                return sharedPreferencesDevice.getFloat(key, defValue);

            default:
                return null;
        }
    }

    public static void putFloat(String entity, String key, float value) {

        switch (entity) {

            case ("User"): {
                SharedPreferences.Editor prefsEditorUser = sharedPreferencesUser.edit();
                prefsEditorUser.putFloat(key, value);
                prefsEditorUser.apply();
            }

            case ("EmergencyContact"): {
                SharedPreferences.Editor prefsEditorEmContact = sharedPreferencesEmContact.edit();
                prefsEditorEmContact.putFloat(key, value);
                prefsEditorEmContact.apply();
            }

            case ("Device"): {
                SharedPreferences.Editor prefsEditorDevice = sharedPreferencesDevice.edit();
                prefsEditorDevice.putFloat(key, value);
                prefsEditorDevice.apply();
            }
        }
    }

 */

