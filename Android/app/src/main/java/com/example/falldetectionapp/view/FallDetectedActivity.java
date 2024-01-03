package com.example.falldetectionapp.view;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.falldetectionapp.R;
import com.example.falldetectionapp.controller.FallDetectedController;
import com.example.falldetectionapp.model.Program;

public class FallDetectedActivity extends AppCompatActivity {
    private ImageButton buttonHome;
    private TextView textViewTimeFall;

    private FallDetectedController fallDetectedController;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall_detected);

        buttonHome = (ImageButton) findViewById(R.id.buttonHomeFromFallDetected);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHomeActivity();
            }
        });

        textViewTimeFall = (TextView) findViewById(R.id.textViewTimeFall);

        fallDetectedController = new FallDetectedController();

        textViewTimeFall.setText("Time: "+fallDetectedController.fallTime);

        if (fallDetectedController.getCurrentUserAlertMode().equals("SMS")) {
            Toast.makeText(this, "SMS sent to all emergency contacts", Toast.LENGTH_LONG).show();
        }
        else if (fallDetectedController.getCurrentUserAlertMode().equals("Call")) {
            Toast.makeText(this, "Calling all emergency contacts", Toast.LENGTH_LONG).show();
            fallDetectedController.callEmergencyContactsFromUser();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Program program = Program.getInstance();
        program.setCurrentActivity(this);
        program.setScreenVisibility(true);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Program program = Program.getInstance();
        program.setScreenVisibility(false);
    }

    private void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}

