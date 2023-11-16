package com.example.falldetectionapp.view;

import androidx.appcompat.app.AppCompatActivity;

import com.example.falldetectionapp.model.Program;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Program program = Program.getInstance();
        changeToSpecifiedActivity();
    }

    public void changeToSpecifiedActivity() {
        Intent intent = new Intent(this, InitialActivity.class);
        startActivity(intent);
    }

}