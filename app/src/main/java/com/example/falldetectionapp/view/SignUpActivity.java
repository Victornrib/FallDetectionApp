package com.example.falldetectionapp.view;

import com.example.falldetectionapp.controller.SignUpController;
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
import com.example.falldetectionapp.model.Program;

public class SignUpActivity extends AppCompatActivity {

    private Button buttonRegisterUser;

    private EditText editTextRegisterUserName;
    private EditText editTextRegisterUserTelephone;
    private EditText editTextRegisterUserEmail;
    private EditText editTextRegisterUserPassword;
    private EditText editTextRegisterUserRepeatedPassword;

    private AlertDialog alertDialog;
    private SignUpController signUpController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        buttonRegisterUser = (Button) findViewById(R.id.buttonRegisterUser);
        buttonRegisterUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                editTextRegisterUserName = (EditText) findViewById(R.id.editTextRegisterUserName);
                String name = editTextRegisterUserName.getText().toString();

                editTextRegisterUserTelephone = (EditText) findViewById(R.id.editTextRegisterUserTelephone);
                String telephone = editTextRegisterUserTelephone.getText().toString();

                editTextRegisterUserEmail = (EditText) findViewById(R.id.editTextRegisterUserEmail);
                String email = editTextRegisterUserEmail.getText().toString();

                editTextRegisterUserPassword = (EditText) findViewById(R.id.editTextRegisterUserPassword);
                String password = editTextRegisterUserPassword.getText().toString();

                editTextRegisterUserRepeatedPassword = (EditText) findViewById(R.id.editTextRegisterUserRepeatedPassword);
                String repeatedPassword = editTextRegisterUserRepeatedPassword.getText().toString();

                signUpController = new SignUpController(name, telephone, email, password, repeatedPassword); //Can pass the context as well

                generateDialog();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Program program = Program.getInstance();
        program.setCurrentActivity(this);
        program.setScreenVisibility(true);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Program.getInstance().setScreenVisibility(false);
    }


    public void generateDialog() {

        alertDialog = new AlertDialog.Builder(SignUpActivity.this).create();

        alertDialog.setCancelable(false);

        alertDialog.setButton(Dialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
            }
        });

        if (!signUpController.signUpFieldsValid()) {
            alertDialog.setMessage(signUpController.getAlertDialogMessage());
            alertDialog.setTitle("Error");
            alertDialog.show();
        }
        else {
            signUpController.checkExistingUser();
        }
    }


    public void generateUserCheckDialogMessage(String errorMessage) {

        if (errorMessage == null) {
            signUpController.storeNewUser();

            alertDialog.setTitle("Registration successful");
            alertDialog.setMessage("The user " + signUpController.name + " has been successfully registered on the system.");
            alertDialog.setButton(Dialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    openInitialActivity();
                }
            });
        }
        else {
            alertDialog.setTitle("Error");
            alertDialog.setMessage(errorMessage);
        }
        alertDialog.show();
    }


    public void openInitialActivity() {
        Intent intent = new Intent(this, InitialActivity.class);
        startActivity(intent);
    }

}
