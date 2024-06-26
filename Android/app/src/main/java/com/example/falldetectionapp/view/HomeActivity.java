package com.example.falldetectionapp.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.falldetectionapp.R;
import com.example.falldetectionapp.model.Program;

public class HomeActivity extends AppCompatActivity {
    private Button buttonSignOut;
    private Button buttonSettings;
    private ImageButton imageButtonAddDevice;
    private Button buttonAddDevice;
    private Button buttonPersonalReport;
    private Button buttonStatistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        buttonSignOut = (Button) findViewById(R.id.buttonSignOut);
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInitialActivity();
            }
        });

        buttonSettings = (Button) findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingsActivity();
            }
        });

        buttonAddDevice = (Button) findViewById(R.id.buttonAddDevice);
        buttonAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openConnectDeviceActivity();
            }
        });

        imageButtonAddDevice = (ImageButton) findViewById(R.id.imageButtonAddDevice);
        imageButtonAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConnectDeviceActivity();
            }
        });

        buttonPersonalReport = (Button) findViewById(R.id.buttonPersonalReport);
        buttonPersonalReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPersonalReportActivity();
            }
        });

/*        buttonStatistics = (Button) findViewById(R.id.buttonStatistics);
        buttonStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openStatisticsActivity();
            }
        });*/
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume()
    {
        super.onResume();
        Program program = Program.getInstance();
        program.setCurrentActivity(this);
        program.setScreenVisibility(true);
        //program.checkFallDetectedActivity();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Program.getInstance().setScreenVisibility(false);
    }

    private void openInitialActivity() {
        Intent intent = new Intent(this, InitialActivity.class);
        startActivity(intent);
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void openConnectDeviceActivity() {
        Intent intent = new Intent(this, ConnectDeviceActivity.class);
        startActivity(intent);
    }

    private void openPersonalReportActivity() {
        Intent intent = new Intent(this, PersonalReportActivity.class);
        startActivity(intent);
    }

    private void openStatisticsActivity() {
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
    }
}