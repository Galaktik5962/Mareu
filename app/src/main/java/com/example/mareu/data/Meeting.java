package com.example.mareu.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Represents a meeting with details such as subject, location, date, time, and participants.
 */
public class Meeting {

    private String subjectOfMeeting;
    private String meetingLocation;
    private Calendar meetingDate;
    private Calendar meetingTime;
    private List<String> meetingParticipants;

    /**
     * Constructs a meeting with the specified details.
     *
     * @param subjectOfMeeting   The subject of the meeting.
     * @param meetingLocation    The location of the meeting.
     * @param meetingDate        The date of the meeting.
     * @param meetingTime        The time of the meeting.
     * @param meetingParticipants The list of participants in the meeting.
     */
    public Meeting(String subjectOfMeeting, String meetingLocation, Calendar meetingDate, Calendar meetingTime, List<String> meetingParticipants) {
        this.subjectOfMeeting = subjectOfMeeting;
        this.meetingLocation = meetingLocation;
        this.meetingDate = meetingDate;
        this.meetingTime = meetingTime;
        this.meetingParticipants = meetingParticipants;
    }

    /**
     * Gets the subject of the meeting.
     *
     * @return The subject of the meeting.
     */
    public String getSubjectOfMeeting() {
        return subjectOfMeeting;
    }

    /**
     * Sets the subject of the meeting.
     *
     * @param subjectOfMeeting The subject of the meeting.
     */
    public void setSubjectOfMeeting(String subjectOfMeeting) {
        this.subjectOfMeeting = subjectOfMeeting;
    }

    /**
     * Gets the location of the meeting.
     *
     * @return The location of the meeting.
     */
    public String getMeetingLocation() {
        return meetingLocation;
    }

    /**
     * Sets the location of the meeting.
     *
     * @param meetingLocation The location of the meeting.
     */
    public void setMeetingLocation(String meetingLocation) {
        this.meetingLocation = meetingLocation;
    }

    /**
     * Gets the date of the meeting.
     *
     * @return The date of the meeting.
     */
    public Calendar getMeetingDate() {
        return meetingDate;
    }

    /**
     * Sets the date of the meeting.
     *
     * @param meetingDate The date of the meeting.
     */
    public void setMeetingDate(Calendar meetingDate) {
        this.meetingDate = meetingDate;
    }

    /**
     * Gets the time of the meeting.
     *
     * @return The time of the meeting.
     */
    public Calendar getMeetingTime() {
        return meetingTime;
    }

    /**
     * Sets the time of the meeting.
     *
     * @param meetingTime The time of the meeting.
     */
    public void setMeetingTime(Calendar meetingTime) {
        this.meetingTime = meetingTime;
    }

    /**
     * Gets the formatted date of the meeting.
     *
     * @return The formatted date of the meeting.
     */
    public String getFormattedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(meetingDate.getTime());
    }

    /**
     * Gets the formatted time of the meeting.
     *
     * @return The formatted time of the meeting.
     */
    public String getFormattedTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return timeFormat.format(meetingTime.getTime());
    }

    /**
     * Gets the list of participants in the meeting.
     *
     * @return The list of participants in the meeting.
     */
    public List<String> getMeetingParticipants() {
        return meetingParticipants;
    }

    /**
     * Sets the list of participants in the meeting.
     *
     * @param meetingParticipants The list of participants in the meeting.
     */
    public void setMeetingParticipants(List<String> meetingParticipants) {
        this.meetingParticipants = meetingParticipants;
    }
}
