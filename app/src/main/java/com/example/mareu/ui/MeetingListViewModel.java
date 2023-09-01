package com.example.mareu.ui;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingRepository;

import java.util.List;

public class MeetingListViewModel extends ViewModel {
    private MeetingRepository meetingRepository;

    private MutableLiveData<List<Meeting>> meetingsLiveData = new MutableLiveData<>();


    // Constructeur du ViewModel de la liste des réunions
    public MeetingListViewModel(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
        meetingsLiveData.setValue(meetingRepository.getMeetings()); // Initialisation des LiveData
    }

    // Méthode pour obtenir la liste des réunions
    public List<Meeting> getMeetings() {

        // Utilise le MeetingRepository pour récupérer la liste des réunions
        return meetingRepository.getMeetings();
    }

    public LiveData<List<Meeting>> getMeetingsLiveData() {
        return meetingsLiveData;
    }

    // Méthode pour supprimer une réunion
    public void deleteMeeting(Meeting meeting) {

        // Utilise le MeetingRepository pour supprimer la réunion
        meetingRepository.deleteMeeting(meeting);

        // Met à jour la liste des réunions dans les LiveData
        meetingsLiveData.postValue(meetingRepository.getMeetings());
    }
}


