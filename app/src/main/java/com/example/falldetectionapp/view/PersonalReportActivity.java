package com.example.falldetectionapp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.falldetectionapp.R;
import com.example.falldetectionapp.model.Program;
import com.example.falldetectionapp.model.fallStats.FallCoordinates;
import com.example.falldetectionapp.model.fallStats.FallDateTime;
import com.example.falldetectionapp.model.fallStats.RecordedFall;

import java.util.ArrayList;
import java.util.List;

public class PersonalReportActivity extends AppCompatActivity {
    private ListView listViewReportedFalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_report);

        listViewReportedFalls = findViewById(R.id.listViewReportedFalls);

        ArrayList<RecordedFallItem> recordedFalls = getRecordedFallsFromProgram();
        RecordedFallAdapter adapter = new RecordedFallAdapter(this, recordedFalls);
        listViewReportedFalls.setAdapter(adapter);
    }

    private ArrayList<RecordedFallItem> getRecordedFallsFromProgram() {
        Program program = Program.getInstance();
        ArrayList<RecordedFall> recordedFalls = program.getCurrentUserRecordedFalls();

        ArrayList<RecordedFallItem> falls = new ArrayList<>();
        for (RecordedFall recordedFall : recordedFalls) {
            FallDateTime fallDateTime = recordedFall.fallDateTime;
            FallCoordinates fallCoordinates = recordedFall.latLng;

            String fallDateTimeString = fallDateTime.getFallDateTimeString();
            RecordedFallItem recordedFallItem = new RecordedFallItem(fallDateTimeString, fallCoordinates);
            falls.add(recordedFallItem);
        }
        return falls;
    }

    public class RecordedFallItem {
        private String fallDateTimeString;
        private FallCoordinates fallCoordinates;

        public RecordedFallItem(String fallDateTimeString, FallCoordinates fallCoordinates) {
            this.fallDateTimeString = fallDateTimeString;
            this.fallCoordinates = fallCoordinates;
        }

        public String getFallDateTimeString() {
            return fallDateTimeString;
        }
    }

    private class RecordedFallAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<RecordedFallItem> recordedFalls;
        private LayoutInflater inflater;

        public RecordedFallAdapter(Context context, ArrayList<RecordedFallItem> recordedFalls) {
            this.context = context;
            this.recordedFalls = recordedFalls;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return recordedFalls.size();
        }

        @Override
        public Object getItem(int position) {
            return recordedFalls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder;

            if (view == null) {
                view = inflater.inflate(R.layout.list_item_recorded_fall, null);
                holder = new ViewHolder();
                holder.textViewFallDateTime = view.findViewById(R.id.textViewFallDateTime);
                holder.buttonFallCoordinates = view.findViewById(R.id.buttonFallCoordinates);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            RecordedFallItem recordedFallItem = recordedFalls.get(position);
            holder.textViewFallDateTime.setText(recordedFallItem.getFallDateTimeString());
            holder.buttonFallCoordinates.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FallCoordinates coords = recordedFallItem.fallCoordinates;

                }
            });

            return view;
        }

        public class ViewHolder {
            TextView textViewFallDateTime;
            ImageButton buttonFallCoordinates;
        }
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}