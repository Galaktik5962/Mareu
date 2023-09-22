package com.example.mareu.ui;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingRepository;

import java.util.Calendar;
import java.util.Date;

public class AddMeetingViewModel extends ViewModel {

    private MeetingRepository meetingRepository;

    private MutableLiveData<String> subjectOfMeeting = new MutableLiveData<>();

    private MutableLiveData<Calendar> selectedDate = new MutableLiveData<>();

    private MutableLiveData<Calendar> selectedTime = new MutableLiveData<>();

    private MutableLiveData<String> selectedRoom = new MutableLiveData<>();

    private MutableLiveData<String> participantList = new MutableLiveData<>();

    private MutableLiveData<Boolean> meetingAddedSuccessfully = new MutableLiveData<>();


    public AddMeetingViewModel(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public LiveData<String> getSubjectOfMeeting() {
        return subjectOfMeeting;
    }

    public void setSubjectOfMeeting(String subject) {
        subjectOfMeeting.setValue(subject);
    }

    public LiveData<Calendar> getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Calendar calendar) {
        selectedDate.setValue(calendar);
    }

    public LiveData<Calendar> getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(Calendar calendar) {
        selectedTime.setValue(calendar);
    }

    public LiveData<String> getSelectedRoom() {
        return selectedRoom;
    }

    public void setSelectedRoom(String room) {
        selectedRoom.setValue(room);
    }

    public LiveData<String> getParticipantList() {
        return participantList;
    }

    public void setParticipantList(String participants) {
        participantList.setValue(participants);
    }

    public LiveData<Boolean> getMeetingAddedSuccessfully() {
        return meetingAddedSuccessfully;
    }

    public void addMeetingToMeetingList(String subjectOfMeeting, String selectedRoom, Calendar selectedDate, Calendar selectedTime, String participantList) {

        // Vérifiez que tous les champs sont remplis
        if (isInputValid()) {

            // Convertissez la date et l'heure en un objet Date
            Date meetingDateAndTime = createMeetingDate(selectedDate, selectedTime);

            // Créez une instance de la réunion avec les détails fournis
            Meeting newMeeting = new Meeting(subjectOfMeeting, selectedRoom, meetingDateAndTime, participantList);

            // Appelez la méthode pour ajouter la réunion à la liste
            addMeetingToMeetingListViaRepository(newMeeting);

            // Dans la méthode d'ajout de la réunion réussie
            meetingAddedSuccessfully.setValue(true);

        } else {
            // Dans la méthode de réinitialisation des champs
            meetingAddedSuccessfully.setValue(false);
        }
    }

    private boolean isInputValid() {
        return !TextUtils.isEmpty(subjectOfMeeting.getValue())
                && !TextUtils.isEmpty(selectedRoom.getValue())
                && selectedDate.getValue() != null
                && selectedTime.getValue() != null
                && !TextUtils.isEmpty(participantList.getValue());
    }

    private Date createMeetingDate(Calendar selectedDate, Calendar selectedTime) {

        // Combinez la date et l'heure pour créer une instance de Date
        Calendar meetingDateAndTime = Calendar.getInstance();
        meetingDateAndTime.set(
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH),
                selectedTime.get(Calendar.HOUR_OF_DAY),
                selectedTime.get(Calendar.MINUTE)
        );
        return meetingDateAndTime.getTime();
    }

    private void addMeetingToMeetingListViaRepository(Meeting newMeeting) {
        // Ajoutez ici la logique pour ajouter la réunion à la liste (par exemple, via le Repository)
        meetingRepository.createMeeting(newMeeting);
    }
}
