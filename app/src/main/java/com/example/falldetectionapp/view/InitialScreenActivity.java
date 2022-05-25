package com.example.falldetectionapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.falldetectionapp.R;
import com.example.falldetectionapp.model.Program;

public class InitialScreenActivity extends AppCompatActivity {
    private Button buttonSignIn;
    private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_screen);

        //click sign in --> sign in screen
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignInScreenActivity();
            }
        });

        //click sign up --> sign up screen
        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignUpScreenActivity();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Program.getInstance().setCurrentActivity(this);
    }

    private void openSignInScreenActivity() {
        Intent intent = new Intent(this, SignInScreenActivity.class);
        startActivity(intent);
    }

    private void openSignUpScreenActivity() {
        Intent intent = new Intent(this, SignUpScreenActivity.class);
        startActivity(intent);
    }

    //may need an alert dialog here for if the previous login is not remembered/stored properly for some reason?
}