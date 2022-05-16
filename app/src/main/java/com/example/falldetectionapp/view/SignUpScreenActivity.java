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
import com.example.falldetectionapp.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpScreenActivity extends AppCompatActivity {

    Button buttonRegisterUser;

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

    AlertDialog alertDialog;
    SignUpController signUpController;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

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

                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference("Users");

                //Needs to be instantiated on the onClick function because it has to get all the fields at the specific time of the click
                signUpController = new SignUpController(name, telephone, email, password, repeatedPassword); //Can pass the context as well

                generateDialog();


                reference.setValue(signUpController);


            }
        });
    }


    public void generateDialog() {

        //Setting up alert error dialog
        alertDialog = new AlertDialog.Builder(SignUpScreenActivity.this).create();

        //Cancelable set to false to only dismiss popup when clicking in 'Ok' button
        alertDialog.setCancelable(false);

        boolean registrationValid = signUpController.checkSignUpFields();

        if (registrationValid) {
            alertDialog.setTitle("Registration successful");
        }
        else {
            alertDialog.setTitle("Error");
        }

        //Setting dynamically generated message from the controller in dialog
        alertDialog.setMessage(signUpController.getAlertDialogMessage());

        //Defining button functionality
        alertDialog.setButton(Dialog.BUTTON_NEUTRAL, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //If all fields are valid
                if (registrationValid) {

                    //Store new user
                    signUpController.storeNewUser();

                    //Switch to next activity
                    openInitialScreenActivity();
                }
                else {
                    alertDialog.cancel();
                }
            }
        });

        alertDialog.show();
    }


    public void openInitialScreenActivity() {
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