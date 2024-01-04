package com.example.falldetectionapp.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.falldetectionapp.R;
import com.example.falldetectionapp.controller.PersonalReportController;
import com.example.falldetectionapp.model.Program;
import com.example.falldetectionapp.model.RecordedFall;

import java.util.ArrayList;

public class PersonalReportActivity extends AppCompatActivity {
    private ListView listViewReportedFalls;
    private PersonalReportController personalReportController;
    private ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_report);

        buttonBack = (ImageButton) findViewById(R.id.buttonBackToHomeFromReportedFalls);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openHomeActivity();
            }
        });

        personalReportController = new PersonalReportController();

        listViewReportedFalls = findViewById(R.id.listViewReportedFalls);
        ArrayList<PersonalReportController.RecordedFallItem> recordedFalls = personalReportController.getRecordedFallsFromProgram();

        if (recordedFalls.size() > 0) {
            RecordedFallAdapter adapter = new RecordedFallAdapter(this, recordedFalls);
            listViewReportedFalls.setAdapter(adapter);
        }
        else
            showNoRecordedFallsMessage();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume()
    {
        super.onResume();
        Program program = Program.getInstance();
        program.setCurrentActivity(this);
        program.setScreenVisibility(true);
        program.checkFallDetectedActivity();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Program.getInstance().setScreenVisibility(false);
    }

    private void showNoRecordedFallsMessage() {
        listViewReportedFalls.setVisibility(View.GONE);
        LinearLayout linearLayout = findViewById(R.id.linearLayoutReportedFalls);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        TextView textView = new TextView(this);
        Typeface customFont = ResourcesCompat.getFont(this, R.font.josefin_sans_semibold);
        textView.setTypeface(customFont);
        textView.setTextColor(getResources().getColor(R.color.standard_orange));
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setText("No falls have been recorded");

        linearLayout.addView(textView, layoutParams);
    }

    private void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    private class RecordedFallAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<PersonalReportController.RecordedFallItem> recordedFalls;
        private LayoutInflater inflater;

        public RecordedFallAdapter(Context context, ArrayList<PersonalReportController.RecordedFallItem> recordedFalls) {
            this.context = context;
            this.recordedFalls = recordedFalls;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    RecordedFall.FallCoordinates coords = recordedFallItem.getFallCoordinates();
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

    private void openMapActivty(RecordedFall.FallCoordinates coords) {
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude", coords.latitude);
        bundle.putDouble("longitude", coords.longitude);
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}