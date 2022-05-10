package com.example.falldetectionapp.view;

import androidx.appcompat.app.AppCompatActivity;
import com.example.falldetectionapp.model.SharedPrefs;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPrefs.init(this);
        changeToSpecifiedActivity(); //why do we need this instead of just straight to initial screen? -- Cause we will probably have the remember login function
    }

    public void changeToSpecifiedActivity() {
        Intent intent = new Intent(this, InitialScreenActivity.class);
        startActivity(intent);
    }
}