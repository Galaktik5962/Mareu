package com.example.mareu.DI;

import com.example.mareu.data.DummyMeetingApiService;
import com.example.mareu.data.MeetingRepository;

/**
 * Dependency injection class responsible for providing instances of MeetingRepository.
 */
public class DI {

    private static MeetingRepository meetingRepository;

    /**
     * Checks if a MeetingRepository instance exists. If not, creates a new one using DummyMeetingApiService.
     *
     * @return The MeetingRepository instance.
     */
    public static MeetingRepository checkIfRepositoryExists() {
        if (meetingRepository == null) {
            DummyMeetingApiService dummyMeetingApiService = new DummyMeetingApiService();
            meetingRepository = new MeetingRepository(dummyMeetingApiService);
        }
        return meetingRepository;
    }
}
