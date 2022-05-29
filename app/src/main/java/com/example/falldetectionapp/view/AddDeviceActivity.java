package com.example.falldetectionapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.falldetectionapp.R;
import com.example.falldetectionapp.model.Program;

public class AddDeviceActivity extends AppCompatActivity {
    private Button buttonSignOut;
    private Button buttonSettings;
    private Button buttonAddDevice;

    //why protected not private? (protected = can be accessed in its own package) - gwen
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        //when you click sign out, you go back to initial 
        buttonSignOut = (Button) findViewById(R.id.buttonSignOut);
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInitialActivity();
            }
        });

        //click settings --> settings 
        buttonSettings = (Button) findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingsActivity();
            }
        });

        //click add device --> pair device 
        buttonAddDevice = (Button) findViewById(R.id.buttonAddDevice);
        buttonAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPairDeviceActivity();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Program program = Program.getInstance();
        program.setCurrentActivity(this);
        if (program.isFallDetected()) {
            Intent intent = new Intent(this, FallDetectedActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Program.getInstance().setCurrentActivity(null);
    }

    private void openInitialActivity() {
        Intent intent = new Intent(this, InitialActivity.class);
        startActivity(intent);
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void openPairDeviceActivity() {
        Intent intent = new Intent(this, ConnectDeviceActivity.class);
        startActivity(intent);
    }
}