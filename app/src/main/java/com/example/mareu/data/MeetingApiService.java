package com.example.mareu.data;

import java.util.List;

public interface MeetingApiService {

    List<Meeting> getMeetings();

    void deleteMeeting(Meeting meeting);

    void createMeeting(Meeting meeting);

    List<Meeting> getFilteredMeetingsByLocation();

    List<Meeting> getFilteredMeetingsByDate();

}



