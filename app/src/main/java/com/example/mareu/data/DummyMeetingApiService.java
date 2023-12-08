package com.example.mareu.data;

import java.util.List;

/**
 * Dummy implementation of the {@link MeetingApiService} interface for testing purposes.
 */
public class DummyMeetingApiService implements MeetingApiService {

    /**
     * List of meetings generated using {@link DummyMeetingGenerator}.
     */
    private List<Meeting> meetings = DummyMeetingGenerator.generateMeetings();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Meeting> getMeetings() {
        return meetings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMeeting(Meeting meeting) {
        meetings.remove(meeting);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createMeeting(Meeting meeting) {
        meetings.add(meeting);
    }
}
