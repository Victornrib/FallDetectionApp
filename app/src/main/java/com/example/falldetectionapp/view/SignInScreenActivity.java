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

import com.example.falldetectionapp.model.SharedPrefs;
import com.example.falldetectionapp.model.User;

import com.google.gson.Gson;

public class SignInScreenActivity extends AppCompatActivity {

    private Button buttonLogin;

    private EditText editTextEmail;
    private EditText editTextPassword;

    private String email;
    private String password;

    public User currentUser; // After will pass to the controller

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

                checkSignInFields();

            }
        });
    }


    //should change name to checkSignInFields() -- Agreed. Done.
    private void checkSignInFields() {

        //Pass all the user fields to all necessary activities
        currentUser = getUser(email, password);
        //if current user exists/not null --> add device screen
        if (currentUser != null) {
            openAddDeviceScreenActivity();
        }
    }


    //move to controller?
    private User getUser(String email, String password) {

        //Scrolls trough all Users in the database checking their emails
        //If no emails matches, opens popup "User don't exist"
        //If one email matches, compares the passwords
        //If both passwords are equal, user is validated and returned
        //If not, opens popup "Password invalid"

        Gson gson = new Gson();
        String jsonRet = SharedPrefs.getString("User", email,null);
        //does jsonRet get all the users??? or what does it get? -gwen  ---- It gets only the user with the corresponding email. If it not finds any, will return 'null' as default value because that is the value that i chose as a default in the defValue field


        //Create alert dialog
        AlertDialog alertDialog = new AlertDialog.Builder(SignInScreenActivity.this).create();
        alertDialog.setTitle("Error");
        alertDialog.setCancelable(false);
        alertDialog.setButton(Dialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.cancel();
            }
        });


        //if database is not empty... not sure how this exactly works. -- This is only checking if some user was received. We can change after the default value to a string with more meaning if you want
        if (jsonRet != null) {
            //System.out.println("\n\n\n"+jsonRet+"\n\n\n"); //this print does not work for me! -gwen -- You can take it out
            currentUser = gson.fromJson(jsonRet, User.class);

            if (currentUser.password.equals(password)) {
                return currentUser;
            }
            else {
                alertDialog.setMessage("Password wrong.");
                alertDialog.show();
                return null;
            }
        }
        else {
            alertDialog.setMessage("Email not found.");
            alertDialog.show();
            return null;
        }
    }


    private void openAddDeviceScreenActivity() {
        Intent intent = new Intent(this, AddDeviceScreenActivity.class);
        startActivity(intent);
    }

}