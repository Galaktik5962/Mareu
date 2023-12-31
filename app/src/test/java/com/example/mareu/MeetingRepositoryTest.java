package com.example.mareu; /**
 * JUnit test class for validating the functionality of the MeetingRepository.
 * Tests include obtaining meetings, deleting meetings, and creating new meetings.
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.mareu.data.DummyMeetingApiService;
import com.example.mareu.data.DummyMeetingGenerator;
import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingRepository;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class MeetingRepositoryTest {

    private MeetingRepository meetingRepository;

    private DummyMeetingApiService dummyMeetingApiService;

    /**
     * Set up the test environment by initializing the required services and repositories.
     */
    @Before
    public void setUp() {

        // Initialize dummyMeetingApiService
        dummyMeetingApiService = new DummyMeetingApiService();

        // Initialize MeetingRepository with dummyMeetingApiService
        meetingRepository = new MeetingRepository(dummyMeetingApiService);
    }

    /**
     * Test the functionality of obtaining the list of meetings.
     */
    @Test
    public void functionGetMeetingsTest() {
        // Obtain the list generated by DummyMeetingGenerator
        List<Meeting> expectedMeetings = DummyMeetingGenerator.generateMeetings();

        // Call the function to be tested
        List<Meeting> meetings = meetingRepository.getMeetings();

        // Verify that the returned list is the same as the generated one
        assertEquals(expectedMeetings, meetings);
    }

    /**
     * Test the functionality of deleting a meeting from the list.
     */
    @Test
    public void functionDeleteMeetingTest() {

        // Obtain the initial list of meetings
        List<Meeting> initialMeetings = DummyMeetingGenerator.generateMeetings();

        // Select a meeting to delete (e.g., the first one in the initial list)
        Meeting meetingToDelete = initialMeetings.get(0);

        // Verify the initial size of the list
        int initialSize = dummyMeetingApiService.getMeetings().size();

        // Call the function to be tested
        meetingRepository.deleteMeeting(meetingToDelete);

        // Obtain the updated list after deletion
        List<Meeting> updatedMeetings = dummyMeetingApiService.getMeetings();

        // Verify that the size of the list has decreased by one unit
        assertEquals(initialSize - 1, updatedMeetings.size());

        // Verify that the meeting has been correctly deleted
        assertFalse(updatedMeetings.contains(meetingToDelete));
    }

    /**
     * Test the functionality of creating a new meeting and adding it to the list.
     */
    @Test
    public void functionCreateMeetingTest() {
        // Obtain the initial list of meetings
        List<Meeting> initialMeetings = dummyMeetingApiService.getMeetings();

        // Create a new meeting
        Meeting newMeeting = new Meeting("Test Meeting", "Room Test", DummyMeetingGenerator.generateTime(14, 0), DummyMeetingGenerator.generateDate(2023,12,25), List.of("test@lamzone.com"));

        // Verify the initial size of the list
        int initialSize = initialMeetings.size();

        // Call the function to be tested
        meetingRepository.createMeeting(newMeeting);

        // Obtain the updated list after creation
        List<Meeting> updatedMeetings = dummyMeetingApiService.getMeetings();

        // Verify that the size of the list has increased by one unit
        assertEquals(initialSize + 1, updatedMeetings.size());

        // Verify that the new meeting has been correctly added
        assertTrue(updatedMeetings.contains(newMeeting));
    }

}


