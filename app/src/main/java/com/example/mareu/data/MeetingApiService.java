package com.example.mareu.data;

import java.util.List;

/**
 * Interface representing the API service for managing meetings.
 */
public interface MeetingApiService {

    /**
     * Gets the list of meetings.
     *
     * @return The list of meetings.
     */
    List<Meeting> getMeetings();

    /**
     * Deletes a meeting from the list.
     *
     * @param meeting The meeting to be deleted.
     */
    void deleteMeeting(Meeting meeting);

    /**
     * Creates a new meeting and adds it to the list.
     *
     * @param meeting The meeting to be created and added.
     */
    void createMeeting(Meeting meeting);
}
