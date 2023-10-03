package com.example.falldetectionapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import com.example.falldetectionapp.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class PersonalReportActivity extends AppCompatActivity {

    private BarChart barChart;
    private BarData barData;
    private BarDataSet barDataSet;
    private ArrayList barEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_report);
        getData();

        barChart = (BarChart) findViewById(R.id.barChart);

        barDataSet = new BarDataSet(barEntries, "Dataset");
        barData = new BarData(barDataSet);

        barChart.setData(barData);

        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(18f);
    }


    private void getData() {
        barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, 2));
        barEntries.add(new BarEntry(5, 3));
        barEntries.add(new BarEntry(2, 0));
        barEntries.add(new BarEntry(3, 3));
        barEntries.add(new BarEntry(0, 5));
    }
}