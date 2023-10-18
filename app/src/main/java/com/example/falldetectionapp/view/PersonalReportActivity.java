package com.example.falldetectionapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.falldetectionapp.R;
import com.example.falldetectionapp.model.Program;
import com.example.falldetectionapp.model.fallStats.FallDateTime;
import com.example.falldetectionapp.model.fallStats.RecordedFall;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class PersonalReportActivity extends AppCompatActivity {

    private ListView listViewReportedFalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_report);

        listViewReportedFalls = (ListView) findViewById(R.id.listViewReportedFalls);
        ArrayAdapter<RecordedFallItem> arrayRecordedFalls = new ArrayAdapter<>(this,android.R.layout.simple_expandable_list_item_1);
        populateListViewRecordedFalls(arrayRecordedFalls);
    }

    public void populateListViewRecordedFalls(ArrayAdapter<RecordedFallItem> arrayRecordedFalls) {
        Program program = Program.getInstance();
        ArrayList<RecordedFall> recordedFalls = program.getCurrentUserRecordedFalls();

        for (int i = 0; i < recordedFalls.size(); i++) {
            // Catch FallDateTime of RecordedFall
            FallDateTime fallDateTime = recordedFalls.get(i).fallDateTime;
            String fallDateTimeString = fallDateTime.getFallDateTimeString();

            // Create RecordedFallItem and add it to the adapter
            RecordedFallItem recordedFallItem = new RecordedFallItem(fallDateTimeString);
            arrayRecordedFalls.add(recordedFallItem);
        }

        listViewReportedFalls.setAdapter(arrayRecordedFalls);
    }

    public class RecordedFallItem {
        private String fallDateTimeString;

        public RecordedFallItem(String fallDateTimeString) {
            this.fallDateTimeString = fallDateTimeString;
        }

        public String getFallDateTimeString() {
            return fallDateTimeString;
        }

        @Override
        public String toString() {
            return fallDateTimeString;
        }
    }
}