package com.example.falldetectionapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.falldetectionapp.R;
import com.example.falldetectionapp.model.EmergencyContact;
import com.example.falldetectionapp.model.SharedPrefs;
import com.example.falldetectionapp.model.User;
import com.google.gson.Gson;



public class SettingsScreenActivity extends AppCompatActivity {
    private Button buttonHome;
    private Button buttonSignOut;
    private Button buttonAddEC1;
    private Button buttonAddEC2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_screen);

        buttonHome = (Button) findViewById(R.id.buttonHome);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDeviceScreenActivity();
            }
        });

        buttonSignOut = (Button) findViewById(R.id.buttonSignOut);
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInitialScreenActivity();
            }
        });

        buttonAddEC1 = (Button) findViewById(R.id.buttonAddEC1);
        buttonAddEC1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEmergencyContactScreenActivity();
            }
        });

        buttonAddEC2 = (Button) findViewById(R.id.buttonAddEC2);
        buttonAddEC2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEmergencyContactScreenActivity();
            }
        });
    }

    private void openAddDeviceScreenActivity() {
        Intent intent = new Intent(this, AddDeviceScreenActivity.class);
        startActivity(intent);
    }

    private void openInitialScreenActivity() {
        Intent intent = new Intent(this, InitialScreenActivity.class);
        startActivity(intent);
    }

    private void openEmergencyContactScreenActivity() {
        Intent intent = new Intent(this, EmergencyContactScreenActivity.class);
        startActivity(intent);
    }


}