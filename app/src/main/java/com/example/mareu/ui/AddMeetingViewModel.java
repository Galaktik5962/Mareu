package com.example.mareu.ui;

import androidx.lifecycle.ViewModel;

import com.example.mareu.data.MeetingRepository;

public class AddMeetingViewModel extends ViewModel {
    private MeetingRepository meetingRepository;

    public AddMeetingViewModel(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }
}