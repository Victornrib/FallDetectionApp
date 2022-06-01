package com.example.falldetectionapp.view;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.falldetectionapp.R;
import com.example.falldetectionapp.model.Program;

public class FallDetectedActivity extends AppCompatActivity {

    private Button buttonSignOut;
    private Button buttonSettings;
    private TextView textViewTimeFall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall_detected);

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

        textViewTimeFall = (TextView) findViewById(R.id.textViewTimeFall);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String fallTime = extras.getString("fallTime");
            textViewTimeFall.setText("Time: "+fallTime);
        }

        Toast.makeText(this, "SMS sent to all emergency contacts", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Program program = Program.getInstance();
        program.setCurrentActivity(this);
        program.setScreenVisibility(true);
        program.setFallDetected(false);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Program program = Program.getInstance();
        program.setScreenVisibility(false);
    }

    private void openInitialActivity() {
        Intent intent = new Intent(this, InitialActivity.class);
        startActivity(intent);
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}

