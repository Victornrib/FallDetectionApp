package com.example.falldetectionapp.controller;

import com.example.falldetectionapp.model.Program;
import com.example.falldetectionapp.model.fallStats.FallCoordinates;
import com.example.falldetectionapp.model.fallStats.FallDateTime;
import com.example.falldetectionapp.model.fallStats.RecordedFall;

import java.util.ArrayList;

public class PersonalReportController {

    public ArrayList<RecordedFallItem> getRecordedFallsFromProgram() {
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
        public FallCoordinates getFallCoordinates() {
            return fallCoordinates;
        }
        public String getFallDateTimeString() {
            return fallDateTimeString;
        }
    }
}
