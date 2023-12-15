package com.example.mareu; /**
 * JUnit test class for validating the functionality of the MeetingSharedViewModel.
 * Tests include applying filters, updating the list, and resetting filters.
 */

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.mareu.data.DummyMeetingApiService;
import com.example.mareu.data.DummyMeetingGenerator;
import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingRepository;
import com.example.mareu.ui.MeetingSharedViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

public class MeetingSharedViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private MeetingRepository meetingRepository;
    private MeetingSharedViewModel meetingSharedViewModel;

    /**
     * Set up the test environment by initializing the required services and repositories.
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Prepare test data
        DummyMeetingApiService dummyMeetingApiService = new DummyMeetingApiService();
        List<Meeting> allMeetings = dummyMeetingApiService.getMeetings();

        // Configure the behavior of meetingRepository.getMeetings()
        when(meetingRepository.getMeetings()).thenReturn(allMeetings);

        // Initialize the ViewModel with the mocked meetingRepository
        meetingSharedViewModel = new MeetingSharedViewModel(meetingRepository);
    }

    /**
     * Test the functionality of applying filters and updating the list of meetings.
     */
    @Test
    public void testApplyFiltersAndUpdateList() {

        // Manually create a Meeting object with the expected properties
        Meeting expectedMeeting = new Meeting("Test", "Room1", DummyMeetingGenerator.generateDate(2023,11,30), DummyMeetingGenerator.generateTime(14,00), List.of("test@lamzone.com"));

        // Configure the behavior of the mock
        when(meetingRepository.getMeetings()).thenReturn(Collections.singletonList(expectedMeeting));

        // Test with no filters
        meetingSharedViewModel.applyFiltersAndUpdateList();

        // Use assertEquals to compare values directly from the LiveData of the class
        assertEquals(Collections.singletonList(expectedMeeting), meetingSharedViewModel.getMeetingsLiveData().getValue());

        // Test with a filter by room (Room1)
        meetingSharedViewModel.setFilterByRoom("Room1");
        meetingSharedViewModel.applyFiltersAndUpdateList();

        assertEquals(Collections.singletonList(expectedMeeting), meetingSharedViewModel.getMeetingsLiveData().getValue());

        // Test with a filter by room (Room1)
        Calendar dateFilter = new GregorianCalendar(2023, 11, 30);
        meetingSharedViewModel.setFilterByDate(dateFilter);
        meetingSharedViewModel.applyFiltersAndUpdateList();

        assertEquals(Collections.singletonList(expectedMeeting), meetingSharedViewModel.getMeetingsLiveData().getValue());
    }

    /**
     * Test the functionality of resetting filters.
     */
    @Test
    public void testResetFilters() {

        // Configure the current filters to simulate a filtered state
        meetingSharedViewModel.setFilterByRoom("Room1");
        Calendar dateFilter = Calendar.getInstance();
        meetingSharedViewModel.setFilterByDate(dateFilter);

        // Call the method to reset filters
        meetingSharedViewModel.resetFilters();

        // Verify that the filters have been reset correctly
        assertEquals("", meetingSharedViewModel.getCurrentFilter());
        assertEquals(null, meetingSharedViewModel.getCurrentDateFilter());

        // Apply filter and verify that my LiveData correctly returns all meetings
        meetingSharedViewModel.applyFiltersAndUpdateList();
        assertEquals(meetingRepository.getMeetings(), meetingSharedViewModel.getMeetingsLiveData().getValue());

    }
}
