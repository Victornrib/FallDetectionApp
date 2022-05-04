package com.example.falldetectionapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.falldetectionapp.R;
import com.example.falldetectionapp.model.EmergencyContact;
import com.example.falldetectionapp.model.SharedPrefs;
import com.example.falldetectionapp.model.User;
import com.google.gson.Gson;



public class SettingsScreenActivity extends AppCompatActivity {
    private Button buttonHome;
    private Button buttonSignOut;
    private Button buttonAddEC1;
    private Button buttonAddEC2;
    String name;
    String telephone;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        buttonHome = (Button) findViewById(R.id.buttonHome);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDeviceScreenActivity();
            }
        });

        buttonSignOut = (Button) findViewById(R.id.buttonSignOut);
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInitialScreenActivity();
            }
        });

        buttonAddEC1 = (Button) findViewById(R.id.buttonAddEC1);
        buttonAddEC1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEmergencyContactScreenActivity();
            }
        });

        buttonAddEC2 = (Button) findViewById(R.id.buttonAddEC2);
        buttonAddEC2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEmergencyContactScreenActivity();
            }
        });
    }

    private void openAddDeviceScreenActivity() {
        Intent intent = new Intent(this, AddDeviceScreenActivity.class);
        startActivity(intent);
    }

    private void openInitialScreenActivity() {
        Intent intent = new Intent(this, InitialScreenActivity.class);
        startActivity(intent);
    }

    private void openEmergencyContactScreenActivity() {
        Intent intent = new Intent(this, EmergencyContactScreenActivity.class);
        startActivity(intent);
    }

    private void checkContactFields() {

        boolean allValid = true;

        if (name.equals("")) {
            System.out.println("Invalid name");
            allValid = false;
        }
        if (telephone.length() != 8) {
            System.out.println("Invalid telephone");
            allValid = false;
        }
        if (email.equals("")) {
            System.out.println("Invalid email");
            allValid = false;
        }


        //Creating User
        if (allValid) {
            storeNewEmergencyContact();
        }

    }

    private void storeNewEmergencyContact() {
        EmergencyContact newContact = new EmergencyContact(name, telephone, email);

        //Passing values to Shared Preferences
        Gson gsonContact = new Gson();
        String jsonContact = gsonContact.toJson(newContact);

        //Passing the userID as the key value from the 'user' field inside the json
        SharedPrefs.localDiskEditor.putString(Integer.toString(newContact.emContactID), jsonContact);
        SharedPrefs.localDiskEditor.commit();


        //---Testing to retrieve the user--- (Working) ------------------------
        //Need to adapt from this part and put in the SignIn activity
        //Need to create a global variable of shared preferences that can be accessed in all project
        String jsonGetContact = SharedPrefs.localDisk.getString(Integer.toString(newContact.emContactID),"") ;
        EmergencyContact currentContact = gsonContact.fromJson(jsonGetContact, EmergencyContact.class);

        System.out.println("\n\n\n"+currentContact.emContactID+"\n\n\n");
        //---------------------------------------------------------------------
//

        //Switch to next activity
        openAddDeviceScreenActivity();
    }
}