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
        alertDialog = new AlertDialog.Builder(AddEmergencyContactActivity.this).create();
        alertDialog.setCancelable(false);

        boolean registrationValid = addEmergencyContactController.checkContactFields();

        if (registrationValid) {
            alertDialog.setTitle("Registration successful");
        }
        else {
            alertDialog.setTitle("Error");
        }

        alertDialog.setMessage(addEmergencyContactController.getAlertDialogMessage());

        alertDialog.setButton(Dialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (registrationValid) {

                    addEmergencyContactController.addNewEmergencyContact();
                    openSettingsActivity();
                }
                else {
                    alertDialog.cancel();
                }
            }
        });

        alertDialog.show();
    }


    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}