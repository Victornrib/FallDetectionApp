package com.example.falldetectionapp.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_emergency_contact);

        int buttonIndex = -1;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            buttonIndex = extras.getInt("buttonIndex");
            //The key argument here must match that used in the other activity
        }

        checkEmergencyContactController = new CheckEmergencyContactController(buttonIndex);

        buttonBackToSettings = (Button) findViewById(R.id.buttonBackToSettings);
        buttonBackToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSettingsActivity();
            }
        });

        buttonDeleteEmergencyContact = (Button) findViewById(R.id.buttonDeleteEmergencyContact);
        buttonDeleteEmergencyContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmergencyContactController.deleteEmergencyContact();
                openSettingsActivity();
            }
        });

        textViewEmergencyContactName = (TextView) findViewById(R.id.textViewEmergencyContactName);
        textViewEmergencyContactTelephone = (TextView) findViewById(R.id.textViewEmergencyContactTelephone);
        textViewEmergencyContactEmail = (TextView) findViewById(R.id.textViewEmergencyContactEmail);

        textViewEmergencyContactName.setText(checkEmergencyContactController.currentUserEmContactName);
        textViewEmergencyContactTelephone.setText(checkEmergencyContactController.currentUserEmContactTelephone);
        textViewEmergencyContactEmail.setText(checkEmergencyContactController.currentUserEmContactEmail);
        }

        public void openSettingsActivity() {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
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
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Program.getInstance().setScreenVisibility(false);
    }

}
