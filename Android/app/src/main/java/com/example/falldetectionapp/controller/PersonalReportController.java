package com.example.falldetectionapp.controller;

import com.example.falldetectionapp.model.Program;
import com.example.falldetectionapp.model.RecordedFall;

import java.util.ArrayList;

public class PersonalReportController {

    public ArrayList<RecordedFallItem> getRecordedFallsFromProgram() {
        Program program = Program.getInstance();
        ArrayList<RecordedFall> recordedFalls = program.getCurrentUser().getRecordedFalls();

        ArrayList<RecordedFallItem> falls = new ArrayList<>();
        for (RecordedFall recordedFall : recordedFalls) {
            RecordedFall.FallDateTime fallDateTime = recordedFall.fallDateTime;
            RecordedFall.FallCoordinates fallCoordinates = recordedFall.latLng;

            String fallDateTimeString = fallDateTime.getFallDateTimeString();
            RecordedFallItem recordedFallItem = new RecordedFallItem(fallDateTimeString, fallCoordinates);
            falls.add(recordedFallItem);
        }
        return falls;
    }

    public class RecordedFallItem {
        private String fallDateTimeString;
        private RecordedFall.FallCoordinates fallCoordinates;
        public RecordedFallItem(String fallDateTimeString, RecordedFall.FallCoordinates fallCoordinates) {
            this.fallDateTimeString = fallDateTimeString;
            this.fallCoordinates = fallCoordinates;
        }
        public RecordedFall.FallCoordinates getFallCoordinates() {
            return fallCoordinates;
        }
        public String getFallDateTimeString() {
            return fallDateTimeString;
        }
    }
}
