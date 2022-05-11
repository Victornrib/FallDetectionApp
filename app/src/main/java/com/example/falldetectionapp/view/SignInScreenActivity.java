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
import com.example.falldetectionapp.controller.SignInController;



public class SignInScreenActivity extends AppCompatActivity {

    private Button buttonLogin;

    private EditText editTextEmail;
    private EditText editTextPassword;

    private String email;
    private String password;

    private SignInController signInController;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_screen);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Getting email
                editTextEmail = (EditText) findViewById(R.id.editTextEmail);
                email = editTextEmail.getText().toString();

                //Getting password
                editTextPassword = (EditText) findViewById(R.id.editTextPassword);
                password = editTextPassword.getText().toString();

                //Instantiating SignInController
                signInController = new SignInController(email, password);


                if (signInController.checkSignInFields()) {
                    openAddDeviceScreenActivity();
                }
                else {
                    generateErrorDialog();
                }
            }
        });
    }


    private void generateErrorDialog() {

        //Create alert dialog
        alertDialog = new AlertDialog.Builder(SignInScreenActivity.this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Error");
        alertDialog.setMessage(signInController.getAlertDialogErrorMessage());
        alertDialog.setButton(Dialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }


    private void openAddDeviceScreenActivity() {
        Intent intent = new Intent(this, AddDeviceScreenActivity.class);
        startActivity(intent);
    }

}