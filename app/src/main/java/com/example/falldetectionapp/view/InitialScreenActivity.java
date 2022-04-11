package com.example.falldetectionapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.falldetectionapp.R;

public class InitialScreenActivity extends AppCompatActivity {
    private Button buttonSignIn;
    private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_screen);

        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignInScreenActivity();
            }
        });

        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSignUpScreenActivity();
            }
        });
    }

    private void openSignInScreenActivity() {
        Intent intent = new Intent(this, SignInScreenActivity.class);
        startActivity(intent);
    }

    private void openSignUpScreenActivity() {
        Intent intent = new Intent(this, SignUpScreenActivity.class);
        startActivity(intent);
    }
}