package com.example.falldetectionapp.view;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.falldetectionapp.R;

import com.example.falldetectionapp.model.User;
import com.example.falldetectionapp.model.SharedPrefs;

import com.google.gson.Gson;



public class SignUpScreenActivity extends AppCompatActivity {
    private Button buttonRegisterUser;
    boolean signUpValidated;


    EditText editTextRegisterUserName;
    EditText editTextRegisterUserTelephone;
    EditText editTextRegisterUserEmail;
    EditText editTextRegisterUserPassword;
    EditText editTextRegisterUserRepeatedPassword;

    String name;
    String telephone;
    String email;
    String password;
    String repeatedPassword;



    //Button buttonForgottenPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        buttonRegisterUser = (Button) findViewById(R.id.buttonRegisterUser);
        buttonRegisterUser.setOnClickListener(new View.OnClickListener() { // why is't this closed?

            //Before create, check if user already exists, and also if fields are valid
            @Override
            public void onClick(View view) {

                //Getting name
                editTextRegisterUserName = (EditText) findViewById(R.id.editTextRegisterUserName);
                name = editTextRegisterUserName.getText().toString();

                //Getting telephone
                editTextRegisterUserTelephone = (EditText) findViewById(R.id.editTextRegisterUserTelephone);
                telephone = editTextRegisterUserTelephone.getText().toString();

                //Getting email
                editTextRegisterUserEmail = (EditText) findViewById(R.id.editTextRegisterUserEmail);
                email = editTextRegisterUserEmail.getText().toString();

                //Getting password
                editTextRegisterUserPassword = (EditText) findViewById(R.id.editTextRegisterUserPassword);
                password = editTextRegisterUserPassword.getText().toString();

                //Getting repeated password
                editTextRegisterUserRepeatedPassword = (EditText) findViewById(R.id.editTextRegisterUserRepeatedPassword);
                repeatedPassword = editTextRegisterUserPassword.getText().toString();

                checkSignUpFields();
                //openAddDeviceScreenActivity();
            }
        });
    }

        private void checkSignUpFields() {

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
            if (password.equals("")) {
                System.out.println("Invalid password");
                allValid = false;
            }
            if (!repeatedPassword.equals(password)) {
                System.out.println("Password invalid");
            }

            //Creating User
            if (allValid) {
                storingNewUser();
            }

        }


        private void storingNewUser() {
            User newUser = new User(name, telephone, email, password);

            //Passing values to Shared Preferences
            Gson gson = new Gson();
            String json = gson.toJson(newUser);

            //Passing the userID as the key value from the 'user' field inside the json
            SharedPrefs.localDiskEditor.putString(Integer.toString(newUser.userID), json);
            SharedPrefs.localDiskEditor.commit();


            //---Testing to retrieve the user--- (Working) ------------------------
            //Need to adapt from this part and put in the SignIn activity
            //Need to create a global variable of shared preferences that can be accessed in all project
            String jsonRet = SharedPrefs.localDisk.getString(Integer.toString(newUser.userID),"") ;
            User currentUser = gson.fromJson(jsonRet, User.class);

            System.out.println("\n\n\n"+currentUser.userID+"\n\n\n");
            //---------------------------------------------------------------------


            //Switch to next activity
            openAddDeviceScreenActivity();
        }

    
        private void openAddDeviceScreenActivity() {
            Intent intent = new Intent(this, AddDeviceScreenActivity.class);
            startActivity(intent);
        }
}

//how to save json files to shared prefs https://stackoverflow.com/questions/7145606/how-do-you-save-store-objects-in-sharedpreferences-on-android