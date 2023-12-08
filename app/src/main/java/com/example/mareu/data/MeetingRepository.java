package com.example.mareu.data;

import java.util.List;

/**
 * Repository class for managing meetings.
 */
public class MeetingRepository {

    private final MeetingApiService meetingApiService;

    /**
     * Constructs a new MeetingRepository with the specified MeetingApiService.
     *
     * @param meetingApiService The MeetingApiService to be used.
     */
    public MeetingRepository(MeetingApiService meetingApiService) {
        this.meetingApiService = meetingApiService;
    }

    /**
     * Gets the list of meetings.
     *
     * @return The list of meetings.
     */
    public List<Meeting> getMeetings() {
        return meetingApiService.getMeetings();
    }

    /**
     * Deletes a meeting from the repository.
     *
     * @param meeting The meeting to be deleted.
     */
    public void deleteMeeting(Meeting meeting) {
        meetingApiService.deleteMeeting(meeting);
    }

    /**
     * Creates a new meeting and adds it to the repository.
     *
     * @param meeting The meeting to be created and added.
     */
    public void createMeeting(Meeting meeting) {
        meetingApiService.createMeeting(meeting);
    }
}
