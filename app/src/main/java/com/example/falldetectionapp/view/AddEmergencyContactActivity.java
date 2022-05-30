package com.example.falldetectionapp.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.falldetectionapp.R;
import com.example.falldetectionapp.controller.AddEmergencyContactController;
import com.example.falldetectionapp.model.Program;

public class AddEmergencyContactActivity extends AppCompatActivity {
    private Button buttonBack;
    private Button buttonAddEmergencyContact;
    private EditText editTextRegisterContactName;
    private EditText editTextRegisterContactTel;
    private EditText editTextRegisterContactEmail;

    AddEmergencyContactController addEmergencyContactController;

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_emergency_contact);

        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingsActivity();
            }
        });

        buttonAddEmergencyContact = (Button) findViewById(R.id.buttonAddEmergencyContact);
        buttonAddEmergencyContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Getting name
                editTextRegisterContactName = (EditText) findViewById(R.id.editTextRegisterEmergencyContactName);
                String name = editTextRegisterContactName.getText().toString();

                //Getting telephone
                editTextRegisterContactTel = (EditText) findViewById(R.id.editTextRegisterEmergencyContactTelephone);
                String telephone = editTextRegisterContactTel.getText().toString();

                //Getting email
                editTextRegisterContactEmail = (EditText) findViewById(R.id.editTextRegisterEmergencyContactEmail);
                String email = editTextRegisterContactEmail.getText().toString();

                addEmergencyContactController = new AddEmergencyContactController(name, telephone, email);

                generateDialog();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume()
    {
        super.onResume();
        Program program = Program.getInstance();
        program.setCurrentActivity(this);
        program.setScreenVisibility(true);
        program.checkFallDetectedActivity();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Program.getInstance().setScreenVisibility(false);
    }



    public void generateDialog() {

        //Setting up alert error dialog
        alertDialog = new AlertDialog.Builder(AddEmergencyContactActivity.this).create();

        //Cancelable set to false to only dismiss popup when clicking in 'Ok' button
        alertDialog.setCancelable(false);

        //Defining default dialog button functionality. Is overridden again if all fields are valid and user is unique
        alertDialog.setButton(Dialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
            }
        });

        if (!addEmergencyContactController.emergencyContactFieldsValid()) {
            //Setting dynamically generated message from the controller in dialog
            alertDialog.setMessage(addEmergencyContactController.getAlertDialogMessage());
            alertDialog.setTitle("Error");
            alertDialog.show();
        }
        else {
            addEmergencyContactController.checkExistingEmergencyContact();
        }
    }


    public void generateEmergencyContactCheckDialogMessage(String errorMessage) {
        //Function called by the program

        if (errorMessage == null) {
            //Store new user
            addEmergencyContactController.storeNewEmergencyContact();

            //Override function of dialog button to lead the user to the AddDeviceActivity
            alertDialog.setTitle("Registration successful");
            alertDialog.setMessage("The emergency contact " + addEmergencyContactController.name + " has been successfully registered on the system.");
            alertDialog.setButton(Dialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    openSettingsActivity();
                }
            });
        }
        else {
            alertDialog.setTitle("Error");
            //Display error message of already existing user
            alertDialog.setMessage(errorMessage);
        }
        alertDialog.show();
    }


    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}