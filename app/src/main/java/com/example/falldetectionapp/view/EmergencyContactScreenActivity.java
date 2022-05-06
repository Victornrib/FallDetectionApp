package com.example.falldetectionapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.falldetectionapp.R;
import com.example.falldetectionapp.model.EmergencyContact;
import com.example.falldetectionapp.model.SharedPrefs;
import com.example.falldetectionapp.model.User;
import com.google.gson.Gson;

public class EmergencyContactScreenActivity extends AppCompatActivity {
    private Button buttonBack;
    private Button buttonAddEmergencyContact;
    String name;
    String telephone;
    String email;
    EditText editTextRegisterContactName;
    EditText editTextRegisterContactTel;
    EditText editTextRegisterContactEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact);

        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingsScreenActivity();
            }
        });

        buttonAddEmergencyContact = (Button) findViewById(R.id.buttonAddEmergencyContact);
        buttonAddEmergencyContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openSettingsScreenActivity(); Is this needed? because you click the app emergency button
                //Getting name
                editTextRegisterContactName = (EditText) findViewById(R.id.editTextRegisterEmergencyContactName);
                name = editTextRegisterContactName.getText().toString();

                //Getting telephone
                editTextRegisterContactTel = (EditText) findViewById(R.id.editTextRegisterEmergencyContactTelephone);
                telephone = editTextRegisterContactTel.getText().toString();

                //Getting email
                editTextRegisterContactEmail = (EditText) findViewById(R.id.editTextRegisterEmergencyContactEmail);
                email = editTextRegisterContactEmail.getText().toString();



                checkContactFields();
                //openAddDeviceScreenActivity();

            }
        });
    }

    private void openSettingsScreenActivity() {
        Intent intent = new Intent(this, SettingsScreenActivity.class);
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
        EmergencyContact newEmContact = new EmergencyContact(name, telephone, email);

        //Passing values to Shared Preferences
        Gson gsonContact = new Gson();
        String jsonContact = gsonContact.toJson(newEmContact);

        //Passing the emContactID as the key value from the 'emContact' field inside the json
        SharedPrefs.putString("EmergencyContact", Integer.toString(newEmContact.emContactID), jsonContact);

        //String jsonRet = SharedPrefs.getString("EmergencyContact", Integer.toString(newEmContact.emContactID),"") ;
        //EmergencyContact currentEmContact = gsonContact.fromJson(jsonRet, EmergencyContact.class);

        //System.out.println("\n\n\n"+currentEmContact.emContactID+"\n\n\n");

        //Switch to next activity
        openSettingsScreenActivity();
    }
}