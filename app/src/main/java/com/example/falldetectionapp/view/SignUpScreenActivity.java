package com.example.falldetectionapp.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;

import android.content.Intent;

import android.os.Bundle;
import java.util.regex.*;

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
        //why does the method below not have a closed bracket before the curly bracket opens? - Because clickListener needs a View. Since you are creating one right away, you need to also declare its scope, even being inside of another function.
        buttonRegisterUser.setOnClickListener(new View.OnClickListener() {

            //Before create, check if user already exists, and also if fields are valid
            @Override
            public void onClick(View view) {

                //Getting name
                editTextRegisterUserName = (EditText) findViewById(R.id.editTextRegisterUserName);
                name = editTextRegisterUserName.getText().toString();
                //if (name.length()==0) {editTextRegisterUserName.setError("Field cannot be left blank"); -- I took care of that before, check how is it bellow --- name.equals("")

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
                repeatedPassword = editTextRegisterUserRepeatedPassword.getText().toString();

                checkSignUpFields();
            }
        });
    }

    private void checkSignUpFields() {
        //Setting up alert error dialog
        AlertDialog alertDialog = new AlertDialog.Builder(SignUpScreenActivity.this).create();

        //Cancelable set to false to only dismiss popup when clicking in 'Ok' button
        alertDialog.setCancelable(false);
        alertDialog.setButton(Dialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (signUpValidated) {
                    storeNewUser();
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

        String errorMessage = "";

        //we should make this a boolean method and call it :)
        signUpValidated = true;

        if (name.equals("")) {
            errorMessage = errorMessage + "Registration invalid. Invalid name.\n";
            signUpValidated = false;
        }
        if (!telephoneMatcher.matches()) {
            errorMessage = errorMessage + "Registration invalid. Invalid telephone.\n";
            signUpValidated = false;
        }
        if (!emailMatcher.matches()) {
            errorMessage = errorMessage + "Registration invalid. Invalid email.\n";
            signUpValidated = false;
        }
        if (SharedPrefs.getString("User", email,null) != null) {
            errorMessage = errorMessage + "Registration invalid. User with this email already exists.\n";
            signUpValidated = false;
        }
        if (password.equals("")) {
            errorMessage = errorMessage + "Registration invalid. Invalid password.\n";
            signUpValidated = false;
        }
        if (!repeatedPassword.equals(password)) {
            errorMessage = errorMessage + "Registration invalid. Passwords do not match.\n";
            signUpValidated = false;
        }

        //Displaying success alert
        if (signUpValidated) {
            alertDialog.setTitle("Registration successful");
            alertDialog.setMessage("The user " + name + " has been successfully registered on the system.");
        }
        //if cannot create user, error alert should show up
        else {
            alertDialog.setTitle("Error");
            alertDialog.setMessage(errorMessage);
        }

        //If all fields are valid, the "Ok" button on the alertDialog will call the 'storeNewUser' function
        alertDialog.show();
    }


    private void storeNewUser() {
        User newUser = new User(name, telephone, email, password);

        //Passing values to Shared Preferences
        Gson gson = new Gson();
        String json = gson.toJson(newUser);

        //Passing the userID as the key value from the 'user' field inside the json
        SharedPrefs.putString("User", newUser.email, json);

        //Switch to next activity
        openInitialScreenActivity();
    }


    private void openInitialScreenActivity() {
        Intent intent = new Intent(this, InitialScreenActivity.class);
        startActivity(intent);
    }

}

//how to save json files to shared prefs https://stackoverflow.com/questions/7145606/how-do-you-save-store-objects-in-sharedpreferences-on-android
//regex1: https://stackoverflow.com/questions/37114166/regex-for-8-digit-phone-number-singapore-number-length
//regex2: https://howtodoinjava.com/java/regex/java-regex-validate-international-phone-numbers/
//regex3: https://stackoverflow.com/questions/8204680/java-regex-email
//regex4: https://www.javatpoint.com/java-email-validation
//alertDialog: https://stackoverflow.com/questions/37984723/alert-dialog-was-disappearing-when-user-clicks-out-side
//alertDialog2: https://stackoverflow.com/questions/30018587/alertdialog-setbutton-was-deprecated