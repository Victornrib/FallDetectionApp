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

    private AddEmergencyContactController addEmergencyContactController;

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

                editTextRegisterContactName = (EditText) findViewById(R.id.editTextRegisterEmergencyContactName);
                String name = editTextRegisterContactName.getText().toString();

                editTextRegisterContactTel = (EditText) findViewById(R.id.editTextRegisterEmergencyContactTelephone);
                String telephone = editTextRegisterContactTel.getText().toString();

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
        //program.checkFallDetectedActivity();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Program.getInstance().setScreenVisibility(false);
    }

    public void generateDialog() {

        alertDialog = new AlertDialog.Builder(AddEmergencyContactActivity.this).create();

        alertDialog.setCancelable(false);

        alertDialog.setButton(Dialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
            }
        });

        if (!addEmergencyContactController.emergencyContactFieldsValid()) {
            alertDialog.setMessage(addEmergencyContactController.getAlertDialogMessage());
            alertDialog.setTitle("Error");
            alertDialog.show();
        }
        else {
            addEmergencyContactController.checkExistingEmergencyContact();
        }
    }


    public void generateEmContactCheckDialog(String errorMessage) {
        if (errorMessage == null) {
            addEmergencyContactController.storeNewEmergencyContact();
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
            alertDialog.setMessage(errorMessage);
        }
        alertDialog.show();
    }


    public void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}