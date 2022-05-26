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
import com.example.falldetectionapp.controller.EmergencyContactController;
import com.example.falldetectionapp.model.EmergencyContact;
import com.example.falldetectionapp.model.Program;
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
    private EditText editTextRegisterContactName;
    private EditText editTextRegisterContactTel;
    private EditText editTextRegisterContactEmail;

    EmergencyContactController emergencyContactController;

    private AlertDialog alertDialog;

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

                //Getting name
                editTextRegisterContactName = (EditText) findViewById(R.id.editTextRegisterEmergencyContactName);
                name = editTextRegisterContactName.getText().toString();

                //Getting telephone
                editTextRegisterContactTel = (EditText) findViewById(R.id.editTextRegisterEmergencyContactTelephone);
                telephone = editTextRegisterContactTel.getText().toString();

                //Getting email
                editTextRegisterContactEmail = (EditText) findViewById(R.id.editTextRegisterEmergencyContactEmail);
                email = editTextRegisterContactEmail.getText().toString();

                emergencyContactController = new EmergencyContactController(name, telephone, email);

                generateDialog();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Program.getInstance().setCurrentActivity(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Program.getInstance().setCurrentActivity(null);
    }



    private void generateDialog() {

        //Create error alert dialog
        alertDialog = new AlertDialog.Builder(EmergencyContactScreenActivity.this).create();
        alertDialog.setCancelable(false);

        boolean registrationValid = emergencyContactController.checkContactFields();

        if (registrationValid) {
            alertDialog.setTitle("Registration successful");
        }
        else {
            alertDialog.setTitle("Error");
        }

        alertDialog.setMessage(emergencyContactController.getAlertDialogMessage());

        alertDialog.setButton(Dialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (registrationValid) {

                    emergencyContactController.addNewEmergencyContact();
                    openSettingsScreenActivity();
                }
                else {
                    alertDialog.cancel();
                }
            }
        });

        alertDialog.show();
    }


    private void openSettingsScreenActivity() {
        Intent intent = new Intent(this, SettingsScreenActivity.class);
        startActivity(intent);
    }
}