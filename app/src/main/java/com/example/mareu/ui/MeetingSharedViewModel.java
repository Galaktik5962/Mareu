package com.example.mareu.ui;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MeetingSharedViewModel extends ViewModel {

    private MeetingRepository meetingRepository;

    private MutableLiveData<List<Meeting>> meetingsLiveData = new MutableLiveData<>(); // Liste des réunions filtrées



    private String currentFilter = ""; // Par défaut, pas de filtre

    private Calendar currentDateFilter = null; // Par défaut, pas de filtre

    public void setFilterByRoom(String room) {
        currentFilter = room;
        currentDateFilter = null;
        applyFiltersAndUpdateList();

    }

    public void setFilterByDate(Calendar date) {
        currentDateFilter = date;
        currentFilter = "";
        applyFiltersAndUpdateList();
    }


    public LiveData<List<Meeting>> getMeetingsLiveData() {
        return meetingsLiveData;
    }




    // Constructeur de MeetingSharedViewModel, prenant en argument un objet MeetingRepository
    public MeetingSharedViewModel(MeetingRepository meetingRepository) {

        // Attribue le MeetingRepository reçu en argument à la variable de membre meetingRepository
        this.meetingRepository = meetingRepository;



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


    }

    // Méthode pour filtrer les réunions en fonction de la salle sélectionnée

    public List<Meeting> filterMeetingsByRoom(String room) {
        List<Meeting> filteredMeetings = new ArrayList<>();
        for (Meeting meeting : meetingRepository.getMeetings()) {
            if (meeting.getMeetingLocation().equals(room)) {
                filteredMeetings.add(meeting);
            }
        }

        return filteredMeetings;
    }

    public List<Meeting> filterMeetingsByDate(Calendar selectedDate) {
        List<Meeting> filteredMeetings = new ArrayList<>();
        for (Meeting meeting : meetingRepository.getMeetings()) {
            Calendar meetingDate = Calendar.getInstance();
            meetingDate.setTime(meeting.getMeetingDateAndTime());

            if (isSameDay(selectedDate, meetingDate)) {
                filteredMeetings.add(meeting);
            }
        }

        return filteredMeetings;
    }

    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.DAY_OF_WEEK) == cal2.get(Calendar.DAY_OF_WEEK);
    }

    // Méthode pour appliquer les filtres et mettre à jour la liste des réunions
    public void applyFiltersAndUpdateList() { // à tester

        List<Meeting> filteredMeetings;

        if (TextUtils.isEmpty(currentFilter) && currentDateFilter == null) {
            // Aucun filtre, récupérer toutes les réunions non filtrées
            filteredMeetings = meetingRepository.getMeetings();

        } else if (!TextUtils.isEmpty(currentFilter)) {
            // Filtrer par salle
            filteredMeetings = filterMeetingsByRoom(currentFilter);
        } else {
            // Filtrer par date
            filteredMeetings = filterMeetingsByDate(currentDateFilter);
        }

        // Mettre à jour le LiveData des réunions filtrées
        meetingsLiveData.postValue(filteredMeetings);

    }

    public void resetFilters() {
        currentFilter = "";
        currentDateFilter = null;
        applyFiltersAndUpdateList();
    }
}

// + de dev java que kotlin
// + de possibilité de resoudre les bug car 25 ans de ressources internet avec java et 6-7 ans kotlin (sur stackoverflow)
// +



