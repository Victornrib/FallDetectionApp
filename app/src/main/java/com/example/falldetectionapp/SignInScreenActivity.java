package com.example.falldetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignInScreenActivity extends AppCompatActivity {
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_screen);

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFallDetectedScreenActivity();
            }
        });
    }

    private void openFallDetectedScreenActivity() {
        Intent intent = new Intent(this, ConnectDeviceScreenActivity.class);
        startActivity(intent);
    }

}