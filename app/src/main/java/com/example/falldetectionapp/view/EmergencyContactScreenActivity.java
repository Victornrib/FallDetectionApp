package com.example.falldetectionapp.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmergencyContactScreenActivity extends AppCompatActivity {
    private Button buttonBack;
    private Button buttonAddEmergencyContact;
    String name;
    String telephone;
    String email;
    EditText editTextRegisterContactName;
    EditText editTextRegisterContactTel;
    EditText editTextRegisterContactEmail;

    boolean emergencyContactValidated;

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
            }
        });
    }

    private void openSettingsScreenActivity() {
        Intent intent = new Intent(this, SettingsScreenActivity.class);
        startActivity(intent);
    }

    private void checkContactFields() {

        //Create error alert dialog
        AlertDialog alertDialog = new AlertDialog.Builder(EmergencyContactScreenActivity.this).create();
        alertDialog.setCancelable(false);
        alertDialog.setButton(Dialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (emergencyContactValidated) {
                    storeNewEmergencyContact();
                }
                else {
                    alertDialog.cancel();
                }
            }
        });

        //Regular expression to validate telephone
        //  "^[0-9]{8,9}$" -- For standard format (ex: 12345678 or 123456789)
        //  "^\+(?:[0-9] ?){6,14}[0-9]$" -- For international format (ex: +0133557799)
        String telephoneRegex = "(^\\+(?:[0-9] ?){6,14}[0-9]$)|(^[0-9]{8,9}$)";
        Pattern telephonePattern = Pattern.compile(telephoneRegex);
        Matcher telephoneMatcher = telephonePattern.matcher(telephone);

        //Regular expression to validate email
        //Format "XXX@YYY.ZZZ"
        String emailRegex = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher emailMatcher = emailPattern.matcher(email);

        emergencyContactValidated = true;

        String errorMessage = "";

        if (name.equals("")) {
            errorMessage = errorMessage + "Invalid name.\n";
            emergencyContactValidated = false;
        }
        if (!telephoneMatcher.matches()) {
            errorMessage = errorMessage + "Invalid telephone.\n";
            emergencyContactValidated = false;
        }
        if (SharedPrefs.getString("EmergencyContact", email,null) != null) {
            errorMessage = errorMessage + "Registration invalid. Emergency Contact with this email already exists.\n";
            emergencyContactValidated = false;
        }
        if (!emailMatcher.matches()) {
            errorMessage = errorMessage + "Invalid email.\n";
            emergencyContactValidated = false;
        }


        //Creating Emergency Contact
        if (emergencyContactValidated) {
            alertDialog.setTitle("Registration successful");
            alertDialog.setMessage("The emergency contact " + name + " has been successfully registered on the system.");
        }
        else {
            alertDialog.setTitle("Error");
            alertDialog.setMessage(errorMessage);
        }
        alertDialog.show();

    }

    private void storeNewEmergencyContact() {
        EmergencyContact newEmContact = new EmergencyContact(name, telephone, email);

        //Passing values to Shared Preferences
        Gson gsonContact = new Gson();
        String jsonContact = gsonContact.toJson(newEmContact);

        //Passing the emContactID as the key value from the 'emContact' field inside the json
        SharedPrefs.putString("EmergencyContact", newEmContact.email, jsonContact);

        //Switch to next activity
        openSettingsScreenActivity();
    }
}