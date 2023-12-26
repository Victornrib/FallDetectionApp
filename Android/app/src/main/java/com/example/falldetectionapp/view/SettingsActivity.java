package com.example.falldetectionapp.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.example.falldetectionapp.R;
import com.example.falldetectionapp.controller.SettingsController;
import com.example.falldetectionapp.model.Program;

public class SettingsActivity extends AppCompatActivity {
    private Button buttonBack;
    private Button buttonSignOut;
    private Button buttonEC1;
    private Button buttonEC2;
    private Button buttonEC3;
    private Switch switchAlertMode;

    private SettingsController settingsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsController = new SettingsController();

        buttonBack = (Button) findViewById(R.id.buttonBackFromSettings);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHomeActivity();
            }
        });

        switchAlertMode = (Switch) findViewById(R.id.switchAlertMode);
        switchAlertMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsController.switchCurrentUserAlertMode();
                Toast.makeText(getApplicationContext(), "Alert mode set to: "+settingsController.getCurrentUserAlertMode(), Toast.LENGTH_SHORT).show();
            }
        });

        buttonSignOut = (Button) findViewById(R.id.buttonSignOut);
        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openInitialActivity();
            }
        });

        buttonEC1 = (Button) findViewById(R.id.buttonEC1);
        buttonEC1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddEmergencyContactActivity();
            }
        });

        buttonEC2 = (Button) findViewById(R.id.buttonEC2);
        buttonEC2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddEmergencyContactActivity();
            }
        });

        buttonEC3 = (Button) findViewById(R.id.buttonEC3);
        buttonEC3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddEmergencyContactActivity();
            }
        });

        for (int i = 0; i < settingsController.currentUserEmContactNames.size(); i++) {
            if (i == 0) {
                buttonEC1.setText("View EC1:\n"+settingsController.currentUserEmContactNames.get(i));
                int finalI = i;
                buttonEC1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openCheckEmergencyContactActivity(finalI);
                    }
                });
            }
            if (i == 1) {
                buttonEC2.setText("View EC2:\n"+settingsController.currentUserEmContactNames.get(i));
                int finalI1 = i;
                buttonEC2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openCheckEmergencyContactActivity(finalI1);
                    }
                });
            }
            if (i == 2) {
                buttonEC3.setText("View EC3:\n"+settingsController.currentUserEmContactNames.get(i));
                int finalI2 = i;
                buttonEC3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openCheckEmergencyContactActivity(finalI2);
                    }
                });
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume()
    {
        super.onResume();
        Program program = Program.getInstance();
        program.setCurrentActivity(this);
        program.setScreenVisibility(true);
        program.checkFallDetectedActivity();
        if (settingsController.getCurrentUserAlertMode().equals("SMS")) {
            switchAlertMode.setChecked(false);
        }
        else if (settingsController.getCurrentUserAlertMode().equals("Call")) {
            switchAlertMode.setChecked(true);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Program.getInstance().setScreenVisibility(false);
    }

    private void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private void openInitialActivity() {
        Intent intent = new Intent(this, InitialActivity.class);
        startActivity(intent);
    }

    private void openAddEmergencyContactActivity() {
        Intent intent = new Intent(this, AddEmergencyContactActivity.class);
        startActivity(intent);
    }

    private void openCheckEmergencyContactActivity(int buttonIndex) {
        Intent intent = new Intent(this, CheckEmergencyContactActivity.class);
        intent.putExtra("buttonIndex",buttonIndex);
        startActivity(intent);
    }
}