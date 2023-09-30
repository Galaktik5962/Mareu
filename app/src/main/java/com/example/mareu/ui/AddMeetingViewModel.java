package com.example.mareu.ui;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingRepository;

import java.util.Calendar;
import java.util.Date;

public class AddMeetingViewModel extends ViewModel {


    private MeetingRepository meetingRepository;

    private boolean isSubjectValid = false;

    private String selectedSubject;
    private MutableLiveData<Boolean> isDateValidLiveData = new MutableLiveData<>();
    private Calendar selectedDate = Calendar.getInstance();
    private MutableLiveData<Boolean> isTimeValidLiveData = new MutableLiveData<>();
    private Calendar selectedTime = Calendar.getInstance();

    private boolean isRoomValid = false;

    private String selectedRoom;
    private MutableLiveData<Boolean> areParticipantsValidLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isFormValidLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> meetingAddedSuccessfully = new MutableLiveData<>();


    public AddMeetingViewModel(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public String getSelectedSubject() {
        return selectedSubject;
    }

    public void setSelectedSubject(String newSubject) {
        selectedSubject = newSubject;
    }

    public String getSelectedRoom() {
        return selectedRoom;
    }

    public void setSelectedRoom(String newRoom) {
        selectedRoom = newRoom;
    }

    public LiveData<Boolean> getIsDateValidLiveData() {
        return isDateValidLiveData;
    }

    public Calendar getSelectedDate() {
        return selectedDate;
    }
    public void setSelectedDate(Calendar newDate) {
        selectedDate = newDate;
    }

    public LiveData<Boolean> getIsTimeValidLiveData() {
        return isTimeValidLiveData;
    }

    public Calendar getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(Calendar newTime) {
        selectedTime = newTime;
    }



    public LiveData<Boolean> areParticipantsValidLiveData() {
        return areParticipantsValidLiveData;
    }

    public LiveData<Boolean> getIsFormValidLiveData() {
        return isFormValidLiveData;
    }

    public LiveData<Boolean> getMeetingAddedSuccessfully() {
        return meetingAddedSuccessfully;
    }

    public void validateSubject(String subject) {
       isSubjectValid = !TextUtils.isEmpty(subject);

       setSelectedSubject(subject); // fournir la valeur pour tous les champs

        // Vérifie si tous les champs sont valides et met à jour l'état global du formulaire
        updateFormValidationState();
    }



    public void validateDate(Calendar selectedDate) {
        // Obtention de la date actuelle
        Calendar currentDate = Calendar.getInstance(); // heure et minute à minuit pile

        // Vérifie si la date sélectionnée est après ou égale à la date actuelle en ignorant l'heure
        boolean isValid = selectedDate != null && selectedDate.after(currentDate) || selectedDate.equals(currentDate);

        isDateValidLiveData.setValue(isValid);

        if (isValid) {
            // Si la date est valide, mise à jour de la date sélectionnée
            setSelectedDate(selectedDate);
        }

        // Vérifie si tous les champs sont valides et met à jour l'état global du formulaire
        updateFormValidationState();
    }

    public void validateTime(Calendar selectedTime) { // si date de demain et heure antérieure à celle d'aujourd'hui ça doit fonctionné, vérif doit se faire uniquement à la date d'aujourd'hui
        boolean isValid = selectedTime != null && selectedTime.after(Calendar.getInstance());
        isTimeValidLiveData.setValue(isValid);

        if (isValid) {
            // Si l'heure est valide, mise à jour de l'heure sélectionnée
            setSelectedTime(selectedTime);
        }

        // Vérifie si tous les champs sont valides et met à jour l'état global du formulaire
        updateFormValidationState();
    }

    public void validateRoom(String selectedRoom) {
        isRoomValid = !TextUtils.isEmpty(selectedRoom);

        setSelectedRoom(selectedRoom); // fournir la valeur pour tous les champs

        // Vérifie si tous les champs sont valides et met à jour l'état global du formulaire
        updateFormValidationState();
    }

    public void validateParticipants(String emails) {
        boolean isValid = areEmailsValid(emails);
        areParticipantsValidLiveData.setValue(isValid);

        if (isValid) {
            // Si les participants sont valides, mise à jour de la liste des participants
            setSelectedRoom(emails);
        }

        // Vérifie si tous les champs sont valides et met à jour l'état global du formulaire
        updateFormValidationState();
    }

    boolean areEmailsValid(String emails) {

        String regex = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";
        return emails.matches(regex);
    }

    private void updateFormValidationState() {

        boolean  isSubjectValid = !TextUtils.isEmpty(selectedSubject);
        boolean isDateValid = isDateValidLiveData.getValue() != null && isDateValidLiveData.getValue();
        boolean isTimeValid = isTimeValidLiveData.getValue() != null && isTimeValidLiveData.getValue();
        boolean isRoomValid = !TextUtils.isEmpty(selectedRoom);
        boolean areParticipantsValid = areParticipantsValidLiveData.getValue() != null && areParticipantsValidLiveData.getValue();

        // Mise à jour de l'état global du formulaire en fonction de la validité de tous les champs
        boolean isFormValid = isSubjectValid && isRoomValid && isDateValid && isTimeValid && areParticipantsValid;
        isFormValidLiveData.setValue(isFormValid);
    }

    public void addMeetingToMeetingList(String subjectOfMeeting, String selectedRoom, Calendar selectedDate, Calendar selectedTime, String participantList) {

        // Vérifie que tous les champs sont remplis et valides
        // enlever la ligne si le bouton s'active ou pas

            // Conversiion de la date et l'heure en un objet Date
            Date meetingDateAndTime = createMeetingDate(selectedDate, selectedTime);

            // Création d'une une instance de la réunion avec les détails fournis
            Meeting newMeeting = new Meeting(subjectOfMeeting, selectedRoom, meetingDateAndTime, participantList);

            // Appel à la méthode pour ajouter la réunion à la liste
            addMeetingToMeetingListViaRepository(newMeeting);

            // La réunion s'ajoute avec succès
            meetingAddedSuccessfully.setValue(true);
        }

    private Date createMeetingDate(Calendar selectedDate, Calendar selectedTime) {

        // Combinaison de la date et l'heure pour créer une instance de Date
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
        // Ajout de la réunion à la liste via le repository
        meetingRepository.createMeeting(newMeeting);
    }


}
