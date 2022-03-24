package com.example.falldetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeToSpecifiedActivity();
    }

    public void changeToSpecifiedActivity() {
        Intent intent = new Intent(this, InitialScreenActivity.class);
        startActivity(intent);
    }
}