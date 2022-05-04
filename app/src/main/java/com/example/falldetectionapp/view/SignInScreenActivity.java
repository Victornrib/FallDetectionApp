package com.example.falldetectionapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.falldetectionapp.R;

import com.example.falldetectionapp.model.User;

import com.google.gson.Gson;


public class SignInScreenActivity extends AppCompatActivity {

    private Button buttonLogin;

    private EditText editTextEmail;
    private EditText editTextPassword;

    private String email;
    private String password;


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

                initializeSignIn();

            }
        });
    }


    private User getUser(String email, String password) {

        //Scrolls trough all Users in the database checking their emails
        //If no emails matches, opens popup "User don't exist"
        //If one email matches, compares the passwords
        //If both passwords are equal, user is validated and returned
        //If not, opens popup "Password invalid"

        //IGNORE THAT FOR NOW
        System.out.println("Get User");
        return new User("aaa","12345678","bbb","123");
    }


    private void initializeSignIn() {

        //User currentUser = getUser(email, password)
        //Pass all the user fields to all necessary activities
        openAddDeviceScreenActivity();


    }

    private void openAddDeviceScreenActivity() {
        Intent intent = new Intent(this, AddDeviceScreenActivity.class);
        startActivity(intent);
    }

}