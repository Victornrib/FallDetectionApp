package com.example.falldetectionapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.falldetectionapp.R;

public class SignUpScreenActivity extends AppCompatActivity {
    private Button buttonRegisterUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        buttonRegisterUser = (Button) findViewById(R.id.buttonRegisterUser);
        buttonRegisterUser.setOnClickListener(new View.OnClickListener() {
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
}