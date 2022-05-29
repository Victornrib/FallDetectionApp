package com.example.falldetectionapp.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.falldetectionapp.R;
import com.example.falldetectionapp.controller.CheckEmergencyContactController;
import com.example.falldetectionapp.model.Program;

public class CheckEmergencyContactActivity extends AppCompatActivity {

    private Button buttonBackToSettings;
    private Button buttonDeleteEmergencyContact;

    private TextView textViewEmergencyContactName;
    private TextView textViewEmergencyContactTelephone;
    private TextView textViewEmergencyContactEmail;

    private CheckEmergencyContactController checkEmergencyContactController;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_emergency_contact);

        buttonBackToSettings = (Button) findViewById(R.id.buttonBackToSettings);
        buttonBackToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingsActivity();
            }
        });

        textViewEmergencyContactName = (TextView) findViewById(R.id.textViewEmergencyContactName);
        textViewEmergencyContactTelephone = (TextView) findViewById(R.id.textViewEmergencyContactTelephone);
        textViewEmergencyContactEmail = (TextView) findViewById(R.id.textViewEmergencyContactEmail);

        int buttonIndex = -1;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            buttonIndex = extras.getInt("buttonIndex");
            //The key argument here must match that used in the other activity
        }

        checkEmergencyContactController = new CheckEmergencyContactController();

        textViewEmergencyContactName.setText(checkEmergencyContactController.currentUserEmContactNames.get(buttonIndex));
        textViewEmergencyContactTelephone.setText(checkEmergencyContactController.currentUserEmContactTelephones.get(buttonIndex));
        textViewEmergencyContactEmail.setText(checkEmergencyContactController.currentUserEmContactEmails.get(buttonIndex));
        }

        public void openSettingsActivity() {
            Intent intent = new Intent(this, AddDeviceActivity.class);
            startActivity(intent);
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
    }
