package com.example.mareu.data;

import android.util.Log;

import java.util.List;

public class MeetingRepository {

    private final MeetingApiService meetingApiService;

    public MeetingRepository(MeetingApiService meetingApiService) {
        this.meetingApiService = meetingApiService;
    }

    public List<Meeting> getMeetings() {
        return meetingApiService.getMeetings();
    }

    public void deleteMeeting(Meeting meeting) {

        meetingApiService.deleteMeeting(meeting);
    }

    public void createMeeting(Meeting meeting) {
        meetingApiService.createMeeting(meeting);
    }

    public List<Meeting> getFilteredMeetingsByLocation() {
        return meetingApiService.getFilteredMeetingsByLocation();
    }

    public List<Meeting> getFilteredMeetingsByDate() {
        return meetingApiService.getFilteredMeetingsByDate();
    }
}
