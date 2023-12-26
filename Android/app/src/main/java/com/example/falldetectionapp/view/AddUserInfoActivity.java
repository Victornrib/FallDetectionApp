package com.example.falldetectionapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.falldetectionapp.R;
import com.example.falldetectionapp.controller.AddUserInfoController;
import com.example.falldetectionapp.model.Program;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class AddUserInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatePickerDialog datePickerDialog;
    private Button buttonDateOfBirth;
    private Button buttonConfirm;
    private Spinner spinnerSex;
    private ListView listViewMovementDisorders;
    private ArrayAdapter<String> arrayAdapterMovementDisorders;
    private String[] AllMovementDisorders = {"Ataxia", "Cervical dystonia", "Chorea", "Dystonia", "Functional movement disorder", "Huntington disease", "Multiple system atrophy", "Myoclonus", "Parkinson disease", "Parkinsonism", "Progressive supranuclear palsy", "Restless legs syndrome", "Tardive dyskinesia", "Tourette syndrome", "Tremor", "Wilson disease"};
    private AddUserInfoController addUserInfoController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_info);

        addUserInfoController = new AddUserInfoController();

        buttonDateOfBirth = findViewById(R.id.buttonDateOfBirth);
        buttonDateOfBirth.setText(addUserInfoController.getTodaysDate());
        initDateOfBirthPicker();

        arrayAdapterMovementDisorders = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, AllMovementDisorders);
        listViewMovementDisorders = findViewById(R.id.listViewMovementDisorders);
        listViewMovementDisorders.setAdapter(arrayAdapterMovementDisorders);

        spinnerSex = findViewById(R.id.spinnerSex);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.sexes, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSex.setAdapter(arrayAdapter);
        spinnerSex.setOnItemSelectedListener(this);

        buttonConfirm = findViewById(R.id.buttonConfirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar userCalendarBirthDate = getUserCalendarBirthDate();
                addUserInfoController.calculateUserAge(userCalendarBirthDate);
                getUserMovementDisorders();
                addUserInfoController.addUserInfo();
                openHomeActivity();
            }
        });
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
        Program.getInstance().setScreenVisibility(false);
    }

    public void initDateOfBirthPicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                String date = addUserInfoController.makeDateString(day, month, year);
                buttonDateOfBirth.setText(date);
            }
        };

        int style = AlertDialog.THEME_HOLO_LIGHT;

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        year = year - 50;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private Calendar getUserCalendarBirthDate() {
        int day = datePickerDialog.getDatePicker().getDayOfMonth();
        int month = datePickerDialog.getDatePicker().getMonth();
        int year =  datePickerDialog.getDatePicker().getYear();
        Calendar calendarBirthDate = new GregorianCalendar(year, month, day);
        AddUserInfoController.getUserBirthDate(calendarBirthDate);
        return calendarBirthDate;
    }

    public void getUserMovementDisorders() {
        for(int i=0; i < listViewMovementDisorders.getCount(); i++) {
            if (listViewMovementDisorders.isItemChecked(i)) {
                String movementDisorder = listViewMovementDisorders.getItemAtPosition(i).toString();
                addUserInfoController.getUserMovementDisorders(movementDisorder);
            }
        }
    }

    public void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String sex = parent.getItemAtPosition(position).toString();
        addUserInfoController.setUserSex(sex);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}