package com.example.mareu.ui;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    private MutableLiveData<List<String>> participantListLiveData = new MutableLiveData<>();

    private List<String> participantList = new ArrayList<>();


    public LiveData<List<String>> getParticipantListLiveData() {
        return participantListLiveData;
    }

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

    public List<String> getParticipantList() {
        return participantList;
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

        updateFormValidationState();

    }

    public void validateDate(Calendar selectedDate) {
        // Obtention de la date actuelle
        Calendar currentDate = Calendar.getInstance();

        // Initialisation de l'heure et des minutes à minuit
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);

        // Vérifie si la date sélectionnée est après ou égale à la date actuelle en ignorant l'heure
        boolean isValid = selectedDate != null && selectedDate.after(currentDate) || selectedDate.equals(currentDate);

        isDateValidLiveData.setValue(isValid);

        if (isValid) {

            // Si la date est valide, mise à jour de la date sélectionnée
            setSelectedDate(selectedDate);

            updateFormValidationState();
        }
    }

    public void validateTime(Calendar selectedTime) {
        boolean isValid = false;

        if (selectedTime != null) {
            Calendar currentDate = Calendar.getInstance();

            if (selectedDate.after(currentDate) || (isSameDay(selectedDate, currentDate) && selectedTime.after(currentDate))) {
                // Si le jour sélectionné est postérieur au jour actuel OU
                // Si le jour sélectionné est le jour actuel et l'heure est postérieure à l'heure actuelle
                isValid = true;
            }
        }

        isTimeValidLiveData.setValue(isValid);

        if (isValid) {

            // Si l'heure est valide, mise à jour de l'heure sélectionnée
            setSelectedTime(selectedTime);

            updateFormValidationState();
        }
    }

    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }


    public void validateRoom(String selectedRoom) {
        isRoomValid = !TextUtils.isEmpty(selectedRoom);

        setSelectedRoom(selectedRoom); // fournir la valeur pour tous les champs

        updateFormValidationState();
    }

    public void validateParticipants(String email) {
        boolean isValid = areEmailsValid(email);

        if (isValid) {
            if (!participantList.contains(email)) {
                participantList.add(email);
                setParticipantList(participantList);
            } else {
                // L'email est déjà dans la liste.
                isValid = false;

            }
        }

        areParticipantsValidLiveData.setValue(isValid);

        updateFormValidationState();
    }


    boolean areEmailsValid(String emails) {

        String regex = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";
        return emails.matches(regex);
    }

    public void addParticipant(String email) {
        List<String> updatedParticipantList = participantListLiveData.getValue();

        if (updatedParticipantList == null) {
            updatedParticipantList = new ArrayList<>();
        }

        if (!updatedParticipantList.contains(email)) {
            updatedParticipantList.add(email);
            participantListLiveData.setValue(updatedParticipantList);
        }
    }

    public void removeParticipant(String email) {
        List<String> updatedParticipantList = new ArrayList<>(participantListLiveData.getValue());
        updatedParticipantList.remove(email);
        participantListLiveData.setValue(updatedParticipantList);
    }

    public void setParticipantList(List<String> participants) {
        participantList = participants;
    }

    public void updateFormValidationState() {

        boolean isSubjectValid = !TextUtils.isEmpty(selectedSubject);
        boolean isDateValid = isDateValidLiveData.getValue() != null && isDateValidLiveData.getValue();
        boolean isTimeValid = isTimeValidLiveData.getValue() != null && isTimeValidLiveData.getValue();
        boolean isRoomValid = !TextUtils.isEmpty(selectedRoom);
        boolean areParticipantsValid = !participantList.isEmpty();

        // Mise à jour de l'état global du formulaire en fonction de la validité de tous les champs
        boolean isFormValid = isSubjectValid && isRoomValid && isDateValid && isTimeValid && areParticipantsValid;
        isFormValidLiveData.setValue(isFormValid);
    }

    public void addMeetingToMeetingList(String subjectOfMeeting, String selectedRoom, Calendar selectedDate, Calendar selectedTime, List<String> participantList) {

        // Conversion de la date et l'heure en un objet Date
        Date meetingDateAndTime = createMeetingDate(selectedDate, selectedTime);

        // Création d'une instance de la réunion avec les détails fournis
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
