package com.example.mareu.ui;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mareu.data.Meeting;
import com.example.mareu.data.MeetingRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * ViewModel for adding a new meeting.
 */
public class AddMeetingViewModel extends ViewModel {

    // Repository for meeting data
    private MeetingRepository meetingRepository;

    // Validation states
    private boolean isSubjectValid = false;
    private boolean isRoomValid = false;

    // Selected meeting details
    private String selectedSubject;
    private String selectedRoom;
    private Calendar selectedDate = Calendar.getInstance();
    private Calendar selectedTime = Calendar.getInstance();

    // LiveData for validation states
    private MutableLiveData<Boolean> isDateValidLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isTimeValidLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> areParticipantsValidLiveData = new MutableLiveData<>();
    private MutableLiveData<List<String>> participantListLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isFormValidLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> meetingAddedSuccessfully = new MutableLiveData<>();

    // Participant list
    private List<String> participantList = new ArrayList<>();

    /**
     * Constructor for AddMeetingViewModel.
     *
     * @param meetingRepository The repository for meeting data.
     */
    public AddMeetingViewModel(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    // Getter and setter methods for LiveData and selected meeting details...
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

    public LiveData<List<String>> getParticipantListLiveData() {
        return participantListLiveData;
    }

    public List<String> getParticipantList() {
        return participantList;
    }

    public void setParticipantList(List<String> participants) {
        participantList = participants;
    }

    public LiveData<Boolean> getIsFormValidLiveData() {
        return isFormValidLiveData;
    }

    public LiveData<Boolean> getMeetingAddedSuccessfully() {
        return meetingAddedSuccessfully;
    }

    /**
     * Validates the meeting subject.
     *
     * @param subject The subject to be validated.
     */
    public void validateSubject(String subject) {
        isSubjectValid = !TextUtils.isEmpty(subject);
        setSelectedSubject(subject);
        updateFormValidationState();
    }

    /**
     * Validates the selected meeting date.
     *
     * @param selectedDate The selected meeting date to be validated.
     */
    public void validateDate(Calendar selectedDate) {

        // Obtaining the current date
        Calendar currentDate = Calendar.getInstance();

        // Initializing hours, minutes, seconds, and milliseconds to midnight
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);

        // Checking if the selected date is equal to or after the current date
        boolean isDateValid = selectedDate != null && !selectedDate.before(currentDate);

        // Updating the LiveData with the validation result
        isDateValidLiveData.setValue(isDateValid);

        if (isDateValid) {
            // If the date is valid, updating the selected date
            setSelectedDate(selectedDate);

            // Updating the overall form validation state
            updateFormValidationState();
        }
    }

    /**
     * Checks if the selected meeting time is after the current date and time.
     * Updates the LiveData with the result of the validation.
     */
    public void checkTimeAfterDate() {

        // Retrieve the validity of the selected date
        boolean isDateValid = isDateValidLiveData.getValue();
        boolean isValid = true;

        // Perform validation only if the date is valid and a time is selected
        if (isDateValid && selectedTime != null) {
            // Get the current date and time
            Calendar currentDate = Calendar.getInstance();


            // Compare hours and minutes only, regardless of the day of the year
            int hourComparison = selectedTime.get(Calendar.HOUR_OF_DAY) - currentDate.get(Calendar.HOUR_OF_DAY);
            int minuteComparison = selectedTime.get(Calendar.MINUTE) - currentDate.get(Calendar.MINUTE);

            // If the selected time is earlier than the current time, set isValid to false
            if (hourComparison < 0 || (hourComparison == 0 && minuteComparison < 0)) {
                // Si l'heure est antérieure à l'heure actuelle, isValid est false
                isValid = false;
            }
        }

        isTimeValidLiveData.setValue(isValid);
    }

    /**
     * Validates the selected meeting time.
     *
     * @param selectedTime The selected meeting time to be validated.
     */
    public void validateTime(Calendar selectedTime) {

        Calendar currentDate = Calendar.getInstance();

        // Check if the selected time is not null and is after the current date and time
        boolean isValid = selectedTime!=null && (selectedDate.after(currentDate) || (isSameDay(selectedDate, currentDate) && selectedTime.after(currentDate)));

        isTimeValidLiveData.setValue(isValid);

        if (isValid) {

            // Si l'heure est valide, mise à jour de l'heure sélectionnée
            setSelectedTime(selectedTime);

            updateFormValidationState();
        }
    }

    /**
     * Checks if two Calendar objects represent the same day.
     *
     * @param cal1 The first Calendar object.
     * @param cal2 The second Calendar object.
     * @return True if both Calendar objects represent the same day; false otherwise.
     */
    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Validates the selected meeting room's availability based on the selected date and time.
     * Updates the LiveData with the result of the validation and the overall form validation state.
     *
     * @param selectedRoom The selected meeting room to be validated.
     * @param selectedDate The selected meeting date.
     * @param selectedTime The selected meeting time.
     */
    public void validateRoom(String selectedRoom, Calendar selectedDate, Calendar selectedTime) {

        // Check if the room is available for the selected date and time
        boolean isRoomAvailable = isRoomAvailable(selectedRoom, selectedDate, selectedTime);
        isRoomValid = !TextUtils.isEmpty(selectedRoom) && isRoomAvailable;

        setSelectedRoom(selectedRoom);
        updateFormValidationState();
    }

    /**
     * Checks if the selected meeting room is available for the given date and time,
     * considering existing meetings in the repository.
     *
     * @param selectedRoom The selected meeting room to check for availability.
     * @param selectedDate The selected meeting date.
     * @param selectedTime The selected meeting time.
     * @return True if the room is available; false if there is a meeting scheduled for the same room, date, and time.
     */
    public boolean isRoomAvailable(String selectedRoom, Calendar selectedDate, Calendar selectedTime) {

        // Retrieve the list of existing meetings from the repository
        List<Meeting> existingMeetings = meetingRepository.getMeetings();

        // Iterate through existing meetings to check availability
        for (Meeting meeting : existingMeetings) {
            if (meeting.getMeetingLocation().equals(selectedRoom)
                    && isSameDay(meeting.getMeetingDate(), selectedDate)
                    && meeting.getMeetingTime().get(Calendar.HOUR_OF_DAY) == selectedTime.get(Calendar.HOUR_OF_DAY)
                    && meeting.getMeetingTime().get(Calendar.MINUTE) == selectedTime.get(Calendar.MINUTE)) {
                // If there is a meeting scheduled for the same room, date, and time, return false
                return false;
            }
        }

        // If no conflicting meetings are found, the room is available
        return true;
    }

    /**
     * Validates the entered email address for participants, checking if it is a valid email format.
     * Updates the LiveData with the validation result and the overall form validation state.
     * Adds the email to the participant list if valid and not already present.
     *
     * @param email The email address to be validated and potentially added to the participant list.
     */
    public void validateParticipants(String email) {

        // Check if the entered email is in a valid format
        boolean isValid = areEmailsValid(email);

        if (isValid) {
            // If the email is valid, check if it is not already in the participant list
            if (!participantList.contains(email)) {
                // Add the email to the participant list
                participantList.add(email);
                setParticipantList(participantList);
            } else {
                // The email is already in the list, set isValid to false
                isValid = false;
            }
        }

        areParticipantsValidLiveData.setValue(isValid);
        updateFormValidationState();
    }

    /**
     * Validates a string containing one or more email addresses by checking if they are in a valid format.
     *
     * @param emails The string containing one or more email addresses to be validated.
     * @return True if all email addresses in the input string are in a valid format; false otherwise.
     */
    boolean areEmailsValid(String emails) {

        // Define the regular expression for a valid email format
        String regex = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";

        // Check if the input string matches the email format
        return emails.matches(regex);
    }

    /**
     * Adds a new participant email address to the participant list LiveData.
     * If the participant list LiveData is not initialized, it creates a new ArrayList to store the participants.
     * Checks if the email address is not already in the participant list before adding it.
     *
     * @param email The email address of the participant to be added to the list.
     */
    public void addParticipant(String email) {
        // Retrieve the current participant list from LiveData
        List<String> updatedParticipantList = participantListLiveData.getValue();

        // If the participant list LiveData is not initialized, create a new ArrayList
        if (updatedParticipantList == null) {
            updatedParticipantList = new ArrayList<>();
        }

        // Check if the email is not already in the participant list before adding it
        if (!updatedParticipantList.contains(email)) {
            // Add the email to the participant list
            updatedParticipantList.add(email);
            // Update the participant list LiveData with the new list
            participantListLiveData.setValue(updatedParticipantList);
        }
    }

    /**
     * Removes a participant email address from the participant list LiveData.
     * Creates a new ArrayList based on the current participant list LiveData,
     * removes the specified email address, and updates the LiveData with the modified list.
     *
     * @param email The email address of the participant to be removed from the list.
     */
    public void removeParticipant(String email) {
        // Create a new ArrayList based on the current participant list LiveData
        List<String> updatedParticipantList = new ArrayList<>(participantListLiveData.getValue());
        // Remove the specified email address from the list
        updatedParticipantList.remove(email);
        // Update the participant list LiveData with the modified list
        participantListLiveData.setValue(updatedParticipantList);
    }

    /**
     * Updates the overall form validation state based on the current validity of individual fields.
     * Checks the validity of the subject, room, date, time, and participants, and
     * sets the LiveData with the resulting overall form validation state.
     */
    public void updateFormValidationState() {

        boolean isSubjectValid = !TextUtils.isEmpty(selectedSubject);
        boolean isDateValid = isDateValidLiveData.getValue() != null && isDateValidLiveData.getValue();
        boolean isTimeValid = isTimeValidLiveData.getValue() != null && isTimeValidLiveData.getValue();
        boolean isRoomValid = !TextUtils.isEmpty(selectedRoom) && isRoomAvailable(selectedRoom, selectedDate, selectedTime);
        boolean areParticipantsValid = !participantList.isEmpty();

        // Update the overall form validation state based on individual field validations
        boolean isFormValid = isSubjectValid && isRoomValid && isDateValid && isTimeValid && areParticipantsValid;
        // Set the LiveData with the overall form validation state
        isFormValidLiveData.setValue(isFormValid);
    }

    /**
     * Adds a new meeting to the meeting list.
     *
     * @param subjectOfMeeting  The subject of the meeting.
     * @param selectedRoom      The selected meeting room.
     * @param selectedDate      The selected meeting date.
     * @param selectedTime      The selected meeting time.
     * @param participantList   The list of participants.
     */
    public void addMeetingToMeetingList(String subjectOfMeeting, String selectedRoom, Calendar selectedDate, Calendar selectedTime, List<String> participantList) { // à tester

        // Création d'une instance de la réunion avec les détails fournis
        Meeting newMeeting = new Meeting(subjectOfMeeting, selectedRoom, selectedDate, selectedTime, participantList);

        // Appel à la méthode pour ajouter la réunion à la liste
        addMeetingToMeetingListViaRepository(newMeeting);

        // La réunion s'ajoute avec succès
        meetingAddedSuccessfully.setValue(true);
    }

    /**
     * Adds a meeting to the meeting list via the repository.
     *
     * @param newMeeting The new meeting to be added.
     */
    private void addMeetingToMeetingListViaRepository(Meeting newMeeting) {
        // Ajout de la réunion à la liste via le repository
        meetingRepository.createMeeting(newMeeting);

    }
}
