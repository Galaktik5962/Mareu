package com.example.mareu.data;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MeetingRepository {

    private final MeetingApiService meetingApiService;


    public MeetingRepository(MeetingApiService meetingApiService) {
        this.meetingApiService = meetingApiService;
    }

    public List<Meeting> getMeetings() {
        return meetingApiService.getMeetings();
    } // à tester

    public void deleteMeeting(Meeting meeting) { // à tester

        meetingApiService.deleteMeeting(meeting);
    }

    public void createMeeting(Meeting meeting) {
        meetingApiService.createMeeting(meeting);
    } // à tester


}
