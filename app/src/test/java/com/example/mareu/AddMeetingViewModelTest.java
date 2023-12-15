package com.example.mareu; /**
 * JUnit test class for validating the functionality of the AddMeetingViewModel.
 * Utilizes the InstantTaskExecutorRule to execute LiveData-related operations synchronously in tests.
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.mareu.data.DummyMeetingApiService;
import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingRepository;
import com.example.mareu.ui.AddMeetingViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class AddMeetingViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    private AddMeetingViewModel addMeetingViewModel;
    private MeetingRepository meetingRepository;
    private DummyMeetingApiService dummyMeetingApiService;

    /**
     * Set up the test environment by initializing the required services and view models.
     */
    @Before
    public void setUp() {
        // Initialize DummyMeetingApiService
        dummyMeetingApiService = new DummyMeetingApiService();

        // Initialize MeetingRepository with DummyMeetingApiService
        meetingRepository = new MeetingRepository(dummyMeetingApiService);

        // Initialize AddMeetingViewModel with MeetingRepository
        addMeetingViewModel = new AddMeetingViewModel(meetingRepository);
    }

    /**
     * Test the functionality of adding a meeting to the meeting list.
     */
    @Test
    public void testAddMeetingToMeetingList() {
        // Prepare test data
        String subject = "Test Meeting";
        String room = "Room Test";

        // Set a specific date (e.g., December 10, 2023)
        Calendar date = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        date.set(Calendar.YEAR, 2023);
        date.set(Calendar.MONTH, Calendar.DECEMBER);
        date.set(Calendar.DAY_OF_MONTH, 10);

        // Set a specific time (e.g., 14:30)
        Calendar time = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        time.set(Calendar.HOUR_OF_DAY, 14);
        time.set(Calendar.MINUTE, 30);

        List<String> participants = List.of("test@lamzone.com");

        // Call the function to be tested
        addMeetingViewModel.addMeetingToMeetingList(subject, room, date, time, participants);

        // Verify that the meeting has been added with the correct details
        List<Meeting> meetings = meetingRepository.getMeetings();
        Meeting addedMeeting = meetings.get(meetings.size() - 1);

        assertEquals(subject, addedMeeting.getSubjectOfMeeting());
        assertEquals(room, addedMeeting.getMeetingLocation());

        // Verify the date and time of the meeting
        assertEquals(date.get(Calendar.YEAR), addedMeeting.getMeetingDate().get(Calendar.YEAR));
        assertEquals(date.get(Calendar.MONTH), addedMeeting.getMeetingDate().get(Calendar.MONTH));
        assertEquals(date.get(Calendar.DAY_OF_MONTH), addedMeeting.getMeetingDate().get(Calendar.DAY_OF_MONTH));
        assertEquals(time.get(Calendar.HOUR_OF_DAY), addedMeeting.getMeetingTime().get(Calendar.HOUR_OF_DAY));
        assertEquals(time.get(Calendar.MINUTE), addedMeeting.getMeetingTime().get(Calendar.MINUTE));

        assertEquals(participants, addedMeeting.getMeetingParticipants());

        // Verify that the value of meetingAddedSuccessfully is correctly set to true
        assertTrue(addMeetingViewModel.getMeetingAddedSuccessfully().getValue());

    }
}
