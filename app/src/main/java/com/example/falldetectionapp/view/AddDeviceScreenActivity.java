package com.example.falldetectionapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.falldetectionapp.R;

public class AddDeviceScreenActivity extends AppCompatActivity {
    private Button buttonSignOut;
    private Button buttonSettings;
    private Button buttonAddDevice;

    //why protected not private? (protected = can be accessed in its own package) - gwen
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device_screen);

        //when you click sign out, you go back to initial screen
        buttonSignOut = (Button) findViewById(R.id.buttonSignOut);
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInitialScreenActivity();
            }
        });

        //click settings --> settings screen
        buttonSettings = (Button) findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingsScreenActivity();
            }
        });

        //click add device --> pair device screen
        buttonAddDevice = (Button) findViewById(R.id.buttonAddDevice);
        buttonAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPairDeviceScreenActivity();
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

    private void openPairDeviceScreenActivity() {
        Intent intent = new Intent(this, PairDeviceScreenActivity.class);
        startActivity(intent);
    }
}