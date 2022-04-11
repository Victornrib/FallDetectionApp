package com.example.falldetectionapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.falldetectionapp.R;

public class EmergencyContactScreenActivity extends AppCompatActivity {
    private Button buttonBack;
    private Button buttonAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact);

        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDeviceScreenActivity();
            }
        });

        buttonAddUser = (Button) findViewById(R.id.buttonAddEmergencyContact);
        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingsScreenActivity();
            }
        });
    }

    private void openAddDeviceScreenActivity() {
        Intent intent = new Intent(this, AddDeviceScreenActivity.class);
        startActivity(intent);
    }

    private void openSettingsScreenActivity() {
        Intent intent = new Intent(this, SettingsScreenActivity.class);
        startActivity(intent);
    }
}