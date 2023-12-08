package com.example.mareu.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * ViewModel shared between fragments to manage meeting-related data.
 * This ViewModel serves as a shared data holder for meeting-related information across multiple fragments.
 * It provides LiveData for observing meeting lists and methods to interact with the underlying data.
 */
public class MeetingSharedViewModel extends ViewModel {

    private MeetingRepository meetingRepository;

    /**
     * LiveData list of filtered meetings.
     */
    private MutableLiveData<List<Meeting>> meetingsLiveData = new MutableLiveData<>();

    private String currentFilter = "";
    private Calendar currentDateFilter = null;

    /**
     * Gets the current room filter.
     *
     * @return The current room filter.
     */
    public String getCurrentFilter() {
        return currentFilter;
    }

    /**
     * Gets the current date filter.
     *
     * @return The current date filter.
     */
    public Calendar getCurrentDateFilter() {
        return currentDateFilter;
    }

    /**
     * Sets the filter by room and updates the meeting list.
     *
     * @param room The room to filter by.
     */
    public void setFilterByRoom(String room) {
        currentFilter = room;
        currentDateFilter = null;
        applyFiltersAndUpdateList();
    }

    /**
     * Sets the filter by date and updates the meeting list.
     *
     * @param date The date to filter by.
     */
    public void setFilterByDate(Calendar date) {
        currentDateFilter = date;
        currentFilter = "";
        applyFiltersAndUpdateList();
    }

    /**
     * Gets the LiveData of filtered meetings.
     *
     * @return The LiveData of filtered meetings.
     */
    public LiveData<List<Meeting>> getMeetingsLiveData() {
        return meetingsLiveData;
    }

    /**
     * Constructs MeetingSharedViewModel with the provided MeetingRepository.
     *
     * @param meetingRepository The MeetingRepository to use.
     */
    public MeetingSharedViewModel(MeetingRepository meetingRepository) {
        // Assign the MeetingRepository received as an argument to the member variable meetingRepository
        this.meetingRepository = meetingRepository;
        // Initialize LiveData with the list of meetings
        meetingsLiveData.setValue(this.meetingRepository.getMeetings());
    }

    /**
     * Gets the list of all meetings.
     *
     * @return The list of all meetings.
     */
    public List<Meeting> getMeetings() {
        // Use the MeetingRepository to retrieve the list of meetings
        return meetingRepository.getMeetings();
    }

    /**
     * Deletes a meeting and updates the meeting list.
     *
     * @param meeting The meeting to delete.
     */
    public void deleteMeeting(Meeting meeting) {
        // Use the MeetingRepository to delete the meeting
        meetingRepository.deleteMeeting(meeting);
        // Update the list of meetings in LiveData
        List<Meeting> updatedMeetings = meetingRepository.getMeetings();
        meetingsLiveData.postValue(updatedMeetings);
    }

    /**
     * Filters meetings by room.
     *
     * @param room The room to filter by.
     * @return The list of filtered meetings.
     */
    public List<Meeting> filterMeetingsByRoom(String room) {
        List<Meeting> filteredMeetings = new ArrayList<>();
        for (Meeting meeting : meetingRepository.getMeetings()) {
            if (meeting.getMeetingLocation().equals(room)) {
                filteredMeetings.add(meeting);
            }
        }

        return filteredMeetings;
    }

    /**
     * Filters meetings by date.
     *
     * @param selectedDate The date to filter by.
     * @return The list of filtered meetings.
     */
    public List<Meeting> filterMeetingsByDate(Calendar selectedDate) {
        List<Meeting> filteredMeetings = new ArrayList<>();
        for (Meeting meeting : meetingRepository.getMeetings()) {
            Calendar meetingDate = Calendar.getInstance();
            meetingDate.setTime(meeting.getMeetingDate().getTime());

            if (isSameDay(selectedDate, meetingDate)) {
                filteredMeetings.add(meeting);
            }
        }

        return filteredMeetings;
    }

    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.DAY_OF_WEEK) == cal2.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * Applies the current filters to the list of meetings and updates the LiveData.
     *
     * If no filters are set, the method retrieves the unfiltered list of meetings from the repository.
     * If the room filter is set, it filters meetings by room.
     * If the date filter is set, it filters meetings by date.
     */
    public void applyFiltersAndUpdateList() {
        List<Meeting> filteredMeetings;

        if (currentFilter.isEmpty() && currentDateFilter == null) {
            // No filter, get all unfiltered meetings
            filteredMeetings = meetingRepository.getMeetings();
        } else if (!currentFilter.isEmpty()) {
            // Filter by room
            filteredMeetings = filterMeetingsByRoom(currentFilter);
        } else {
            // Filter by date
            filteredMeetings = filterMeetingsByDate(currentDateFilter);
        }

        // Update the LiveData of filtered meetings
        meetingsLiveData.postValue(filteredMeetings);
    }

    /**
     * Resets filters to their default values and updates the meeting list.
     */
    public void resetFilters() {
        currentFilter = "";
        currentDateFilter = null;
        applyFiltersAndUpdateList();
    }
}

// + de dev java que kotlin
// + de possibilité de resoudre les bug car 25 ans de ressources internet avec java et 6-7 ans kotlin (sur stackoverflow)
// +



