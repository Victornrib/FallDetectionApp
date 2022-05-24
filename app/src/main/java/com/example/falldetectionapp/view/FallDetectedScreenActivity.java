package com.example.falldetectionapp.view;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.falldetectionapp.R;
import com.example.falldetectionapp.controller.FallDetectedController;
import com.example.falldetectionapp.controller.SignInController;
import com.example.falldetectionapp.model.EmergencyContact;
import com.example.falldetectionapp.model.Program;

public class FallDetectedScreenActivity extends AppCompatActivity {

    private Button buttonSignOut;
    private Button buttonSettings;
    private String textViewTimeFall;

    private FallDetectedController fallDetectedController;

    //private String phoneNumber;
    //private String message;
    //private static final int REQUEST_CODE = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall_detected_screen);

        fallDetectedController = new FallDetectedController();

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

        //is this how i call this method? does it change the text automatically, or do we need to make a loop to check constantly if the text changed
        getTimeOfFallText();

        //not sure how to change the actual text of the TextView???


        if (ContextCompat.checkSelfPermission(FallDetectedScreenActivity.this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            sendSMS();
        } else {
            ActivityCompat.requestPermissions(FallDetectedScreenActivity.this, new String[]{Manifest.permission.SEND_SMS}, 100);
        }
    }

    private void openInitialScreenActivity() {
        Intent intent = new Intent(this, InitialScreenActivity.class);
        startActivity(intent);
    }

    private void openSettingsScreenActivity() {
        Intent intent = new Intent(this, SettingsScreenActivity.class);
        startActivity(intent);
    }

    //this should be here or in the onCreate??
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getTimeOfFallText() {
        if (fallDetectedController.checkTimeOfAlert()) {
            textViewTimeFall = fallDetectedController.timeOfAlertMessage;
        }
        return textViewTimeFall;
    }

//    //move to controller
    //why do we call sms twice in this class???!??
    
//    //code from https://www.youtube.com/watch?v=ofAL1C4jUJw
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if(requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMS();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
            }
        }

    //move to controller
    private void sendSMS() {
        Program program = Program.getInstance();
        SmsManager smsManager = SmsManager.getDefault();

        for (int i = 0; i < program.getCurrentUser().getEmContacts().size(); i++) {
            EmergencyContact currentEmContact = program.getCurrentUser().getEmContacts().get(i);
            String emContactPhoneNumber = currentEmContact.telephone;
            String message = "Attention " + currentEmContact.name + ": " + program.getCurrentUser().name + " felt!\nPlease take action now!";
            smsManager.sendTextMessage(emContactPhoneNumber, null, message, null, null);
        }

        Toast.makeText(this, "SMS sent to all emergency contacts", Toast.LENGTH_LONG).show();
    }
}

//not sure if we need this but in theory we need to also close the receiver, maybe in the main activity.
//    @Override
//    protected void onDestroy() {
//        unregisterReceiver(receiver);
//        super.onDestroy();
//    }


