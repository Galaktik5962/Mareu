package com.example.mareu.ui;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mareu.data.DummyMeetingApiService;
import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MeetingSharedViewModel extends ViewModel {

    private MeetingRepository meetingRepository;

    private MutableLiveData<List<Meeting>> meetingsLiveData = new MutableLiveData<>(); // Liste des réunions filtrées

    private List<Meeting> allMeetings; // Variable pour stocker la liste complète de toutes les réunions

    private String currentFilter = ""; // Par défaut, pas de filtre

    private Calendar currentDateFilter; // Par défaut, pas de filtre

    public String getCurrentFilter() {
        return currentFilter;
    }

    public void setCurrentFilter(String currentFilter) {
        this.currentFilter = currentFilter;
    }

    public Calendar getCurrentDateFilter() {
        return currentDateFilter;
    }

    public void setCurrentDateFilter(Calendar currentDateFilter) {
        this.currentDateFilter = currentDateFilter;
    }

    public LiveData<List<Meeting>> getMeetingsLiveData() {
        return meetingsLiveData;
    }

   public void setAllMeetings(List<Meeting> meetings) {
       allMeetings = meetings;
    }


    // Constructeur de MeetingSharedViewModel, prenant en argument un objet MeetingRepository
    public MeetingSharedViewModel (MeetingRepository meetingRepository) {

        // Attribue le MeetingRepository reçu en argument à la variable de membre meetingRepository
       this.meetingRepository = meetingRepository;

       this.allMeetings = new ArrayList<>(meetingRepository.getMeetings()); // Initialisation de allMeetings avec la liste de réunions

       // Initialise les LiveData avec la liste des réunions
        meetingsLiveData.setValue(this.meetingRepository.getMeetings());
    }


    // Méthode pour obtenir la liste des réunions
    public List<Meeting> getMeetings() {

        // Utilise le MeetingRepository pour récupérer la liste des réunions
        return meetingRepository.getMeetings();
    }


    // Méthode pour supprimer une réunion
    public void deleteMeeting(Meeting meeting) {

        // Utilise le MeetingRepository pour supprimer la réunion
        meetingRepository.deleteMeeting(meeting);

        // Met à jour la liste des réunions dans les LiveData
        List<Meeting> updatedMeetings = meetingRepository.getMeetings();

        meetingsLiveData.postValue(updatedMeetings);

        // Mettez à jour la liste allMeetings
        allMeetings = new ArrayList<>(updatedMeetings);
    }

    // Méthode pour filtrer les réunions en fonction de la salle sélectionnée

    public void filterMeetingsByRoom (String room) {
        List<Meeting> filteredMeetings = new ArrayList<>();
        for (Meeting meeting : allMeetings) {
            if (meeting.getMeetingLocation().equals(room)) {
                filteredMeetings.add(meeting);
            }
        }

        meetingsLiveData.setValue(filteredMeetings);
    }

    public void filterMeetingsByDate(Calendar selectedDate) {
        List<Meeting> filteredMeetings = new ArrayList<>();
        for (Meeting meeting : allMeetings) {
            Calendar meetingDate = Calendar.getInstance();
            meetingDate.setTime(meeting.getMeetingDateAndTime());

            if (isSameDay(selectedDate, meetingDate)) {
                filteredMeetings.add(meeting);
            }
        }

        meetingsLiveData.setValue(filteredMeetings);
    }

    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

}


