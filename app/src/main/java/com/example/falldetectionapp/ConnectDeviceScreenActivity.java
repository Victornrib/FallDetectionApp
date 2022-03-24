package com.example.falldetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConnectDeviceScreenActivity extends AppCompatActivity {
    private Button buttonSignOut;
    private Button buttonSettings;
    private Button buttonConnectDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_device_screen);

        buttonSignOut = (Button) findViewById(R.id.buttonSignOut);
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInitialScreenActivity();
            }
        });

        buttonSettings = (Button) findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingsScreenActivity();
            }
        });
    }

    private void openInitialScreenActivity() {
        Intent intent = new Intent(this, InitialScreenActivity.class);
        startActivity(intent);
    }

    private void openSettingsScreenActivity() {
        Intent intent = new Intent(this, SettingsScreenActivity.class);
        startActivity(intent);
    }
}