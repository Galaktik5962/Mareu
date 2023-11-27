package com.example.mareu.ui;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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


    public AddMeetingViewModel(MeetingRepository meetingRepository) {  // Ajout de cette ligne
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

        // Vérifie si le jour sélectionné est postérieur ou égal au jour actuel
        boolean isDateValid = selectedDate != null && !selectedDate.before(currentDate);

        isDateValidLiveData.setValue(isDateValid);

        if (isDateValid) {
            // Si la date est valide, mise à jour de la date sélectionnée
            setSelectedDate(selectedDate);

            updateFormValidationState();
        }
    }

    public void checkTimeAfterDate() {
        boolean isDateValid = isDateValidLiveData.getValue();
        boolean isValid = true;

        if (isDateValid && selectedTime != null) {
            // Vérifie également l'heure et les minutes, indépendamment du jour de l'année
            Calendar currentDate = Calendar.getInstance();


            // Comparaison des heures et des minutes seulement
            int hourComparison = selectedTime.get(Calendar.HOUR_OF_DAY) - currentDate.get(Calendar.HOUR_OF_DAY);
            int minuteComparison = selectedTime.get(Calendar.MINUTE) - currentDate.get(Calendar.MINUTE);

            if (hourComparison < 0 || (hourComparison == 0 && minuteComparison < 0)) {
                // Si l'heure est antérieure à l'heure actuelle, isValid est false
                isValid = false;
            }
        }

        isTimeValidLiveData.setValue(isValid);
    }


    public void validateTime(Calendar selectedTime) {

        Calendar currentDate = Calendar.getInstance();

        boolean isValid = selectedTime!=null && (selectedDate.after(currentDate) || (isSameDay(selectedDate, currentDate) && selectedTime.after(currentDate)));

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
        boolean isRoomAvailable = isRoomAvailable(selectedRoom, selectedDate, selectedTime);
        isRoomValid = !TextUtils.isEmpty(selectedRoom) && isRoomAvailable;

        setSelectedRoom(selectedRoom);
        updateFormValidationState();
    }


    public boolean isRoomAvailable(String selectedRoom, Calendar selectedDate, Calendar selectedTime) {
        List<Meeting> existingMeetings = meetingRepository.getMeetings();

        for (Meeting meeting : existingMeetings) {
            if (meeting.getMeetingLocation().equals(selectedRoom) && isDateTimeOverlap(selectedDate, selectedTime, meeting)) {

                return false;
            }
        }

        return true;
    }

    private boolean isDateTimeOverlap(Calendar selectedDate, Calendar selectedTime, Meeting meeting) {
        Calendar meetingStartDateTime = convertDateToCalendar(meeting.getMeetingDateAndTime());

        // Comparer les composants de date et d'heure individuellement
        return selectedDate.get(Calendar.MONTH) == meetingStartDateTime.get(Calendar.MONTH)
                && selectedDate.get(Calendar.DAY_OF_MONTH) == meetingStartDateTime.get(Calendar.DAY_OF_MONTH)
                && selectedTime.get(Calendar.HOUR_OF_DAY) == meetingStartDateTime.get(Calendar.HOUR_OF_DAY)
                && selectedTime.get(Calendar.MINUTE) == meetingStartDateTime.get(Calendar.MINUTE);
    }

    // Méthode pour convertir un objet Date en Calendar
    private Calendar convertDateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
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
        boolean isRoomValid = !TextUtils.isEmpty(selectedRoom) && isRoomAvailable(selectedRoom, selectedDate, selectedTime);
        boolean areParticipantsValid = !participantList.isEmpty();

        // Mise à jour de l'état global du formulaire en fonction de la validité de tous les champs
        boolean isFormValid = isSubjectValid && isRoomValid && isDateValid && isTimeValid && areParticipantsValid;
        isFormValidLiveData.setValue(isFormValid);
    }

    public void addMeetingToMeetingList(String subjectOfMeeting, String selectedRoom, Calendar selectedDate, Calendar selectedTime, List<String> participantList) { // à tester

        // Conversion de la date et l'heure en un objet Date
        Date meetingDateAndTime = createMeetingDate(selectedDate, selectedTime);

        // Création d'une instance de la réunion avec les détails fournis
        Meeting newMeeting = new Meeting(subjectOfMeeting, selectedRoom, meetingDateAndTime, participantList);

        // Appel à la méthode pour ajouter la réunion à la liste
        addMeetingToMeetingListViaRepository(newMeeting);

        // La réunion s'ajoute avec succès
        meetingAddedSuccessfully.setValue(true);

    }

    public Date createMeetingDate(Calendar selectedDate, Calendar selectedTime) {

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
