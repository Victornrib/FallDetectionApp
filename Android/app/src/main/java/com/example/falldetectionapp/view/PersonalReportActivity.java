package com.example.falldetectionapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.falldetectionapp.R;
import com.example.falldetectionapp.controller.PersonalReportController;
import com.example.falldetectionapp.model.fallStats.FallCoordinates;

import java.util.ArrayList;

public class PersonalReportActivity extends AppCompatActivity {
    private ListView listViewReportedFalls;
    private PersonalReportController personalReportController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        personalReportController = new PersonalReportController();

        setContentView(R.layout.activity_personal_report);

        listViewReportedFalls = findViewById(R.id.listViewReportedFalls);

        ArrayList<PersonalReportController.RecordedFallItem> recordedFalls = personalReportController.getRecordedFallsFromProgram();
        RecordedFallAdapter adapter = new RecordedFallAdapter(this, recordedFalls);
        listViewReportedFalls.setAdapter(adapter);
    }

    private class RecordedFallAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<PersonalReportController.RecordedFallItem> recordedFalls;
        private LayoutInflater inflater;

        public RecordedFallAdapter(Context context, ArrayList<PersonalReportController.RecordedFallItem> recordedFalls) {
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

            PersonalReportController.RecordedFallItem recordedFallItem = recordedFalls.get(position);
            holder.textViewFallDateTime.setText(recordedFallItem.getFallDateTimeString());
            holder.buttonFallCoordinates.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FallCoordinates coords = recordedFallItem.getFallCoordinates();
                    openMapActivty(coords);
                }
            });

            return view;
        }

        public class ViewHolder {
            TextView textViewFallDateTime;
            ImageButton buttonFallCoordinates;
        }
    }

    private void openMapActivty(FallCoordinates coords) {
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude", coords.latitude);
        bundle.putDouble("longitude", coords.longitude);
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}