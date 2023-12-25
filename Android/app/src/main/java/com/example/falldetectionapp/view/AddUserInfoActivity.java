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
import com.example.falldetectionapp.model.Program;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.Locale;

public class AddUserInfoActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private DatePickerDialog datePickerDialog;
    private Button buttonDateOfBirth;

    private Button buttonConfirm;
    private Spinner spinnerGender;

    private ListView listViewMovementDisorders;

    private ArrayAdapter<String> arrayAdapterMovementDisorders;

    private String[] AllMovementDisorders = {"Ataxia", "Cervical dystonia", "Chorea", "Dystonia", "Functional movement disorder", "Huntington's disease", "Multiple system atrophy", "Myoclonus", "Parkinson's disease", "Parkinsonism", "Progressive supranuclear palsy", "Restless legs syndrome", "Tardive dyskinesia", "Tourette syndrome", "Tremor", "Wilson's disease"};

    private ArrayList<String> movementDisorders = new ArrayList<String>();

    private String sex;

    private String birthDate;
    private Integer age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_info);

        buttonDateOfBirth = findViewById(R.id.buttonDateOfBirth);
        buttonDateOfBirth.setText(getTodaysDate());
        initDateOfBirthPicker();

        arrayAdapterMovementDisorders = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, AllMovementDisorders);
        listViewMovementDisorders = findViewById(R.id.listViewMovementDisorders);
        listViewMovementDisorders.setAdapter(arrayAdapterMovementDisorders);

        spinnerGender = findViewById(R.id.spinnerGender);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.sexes, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(arrayAdapter);
        spinnerGender.setOnItemSelectedListener(this);

        buttonConfirm = findViewById(R.id.buttonConfirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateUserAge();
                getUserMovementDisorders();
                addUserInfo();
                openAddDeviceActivity();
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

    public void addUserInfo() {
        Program program = Program.getInstance();
        program.addCurrentUserInfo(sex, birthDate, age, movementDisorders);
    }

    private void getUserMovementDisorders() {
        for(int i=0; i < listViewMovementDisorders.getCount(); i++) {
            if (listViewMovementDisorders.isItemChecked(i)) {
                movementDisorders.add(listViewMovementDisorders.getItemAtPosition(i).toString());
            }
        }
    }

    private Calendar getUserCalendarBirthDate() {

        int day = datePickerDialog.getDatePicker().getDayOfMonth();
        int month = datePickerDialog.getDatePicker().getMonth();
        int year =  datePickerDialog.getDatePicker().getYear();

        Calendar calendarBirthDate = new GregorianCalendar(year, month, day);
        Date birthDateRaw = calendarBirthDate.getTime();

        DateFormat formatter = SimpleDateFormat.getDateInstance(DateFormat.LONG, Locale.ENGLISH);
        birthDate = formatter.format(birthDateRaw);

        return calendarBirthDate;
    }

    private void calculateUserAge() {
        Calendar calendarCurrentDate = new GregorianCalendar();
        Calendar userCalendarBirthDate = getUserCalendarBirthDate();
        age = calendarCurrentDate.get(Calendar.YEAR) - userCalendarBirthDate.get(Calendar.YEAR);
    }

    private void initDateOfBirthPicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                String date = makeDateString(day, month, year);
                buttonDateOfBirth.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        year = year - 50;

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        year = year - 50;
        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sex = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void openAddDeviceActivity() {
        Intent intent = new Intent(this, AddDeviceActivity.class);
        startActivity(intent);
    }
}