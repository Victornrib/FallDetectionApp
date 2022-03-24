package com.example.falldetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsScreenActivity extends AppCompatActivity {
    private Button buttonHome;
    private Button buttonSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        buttonHome = (Button) findViewById(R.id.buttonHome);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openConnectDeviceScreenActivity();
            }
        });

        buttonSignOut = (Button) findViewById(R.id.buttonSignOut);
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInitialScreenActivity();
            }
        });
    }

    private void openConnectDeviceScreenActivity() {
        Intent intent = new Intent(this, ConnectDeviceScreenActivity.class);
        startActivity(intent);
    }

    private void openInitialScreenActivity() {
        Intent intent = new Intent(this, InitialScreenActivity.class);
        startActivity(intent);
    }
}