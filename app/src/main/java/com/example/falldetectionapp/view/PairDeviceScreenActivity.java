package com.example.falldetectionapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.falldetectionapp.R;


public class PairDeviceScreenActivity extends AppCompatActivity {
    private Button buttonBack;
    private Button buttonPairDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair_device_screen);

        //click back --> goes back to add device
        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDeviceScreenActivity();
            }
        });

        //click pair --> also goes back to add device.
        // This is not the intention right? -gwen
        buttonPairDevice = (Button) findViewById(R.id.buttonPairDevice);
        buttonPairDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDeviceScreenActivity();
            }
        });
    }

    private void openAddDeviceScreenActivity() {
        Intent intent = new Intent(this, AddDeviceScreenActivity.class);
        startActivity(intent);
    }

    //we will need an AlertDialog here to say if connection is successful or not.
}