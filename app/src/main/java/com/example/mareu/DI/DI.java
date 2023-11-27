package com.example.mareu.DI;

import com.example.mareu.data.DummyMeetingApiService;
import com.example.mareu.data.MeetingRepository;

// Injection de dépendances
public class DI {

    private static MeetingRepository meetingRepository;


    public static MeetingRepository checkIfRepositoryExists() {
        if (meetingRepository == null) {
            DummyMeetingApiService dummyMeetingApiService = new DummyMeetingApiService();
            meetingRepository = new MeetingRepository(dummyMeetingApiService);
        }
        return meetingRepository;
    }
}
