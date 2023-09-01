package com.example.mareu.data;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Meeting {

    private String subjectOfMeeting;
    private String meetingLocation;
    private Date meetingDateAndTime;
    private String meetingParticipants;

    public Meeting (String subjectOfMeeting, String meetingLocation, Date meetingDateAndTime, String meetingParticipants) {
        this.subjectOfMeeting = subjectOfMeeting;
        this.meetingLocation = meetingLocation;
        this.meetingDateAndTime = meetingDateAndTime;
        this.meetingParticipants = meetingParticipants;
    }

    public String getSubjectOfMeeting() {
        return subjectOfMeeting;
    }

    public void setSubjectOfMeeting(String subjectOfMeeting) {
        this.subjectOfMeeting = subjectOfMeeting;
    }

    public String getMeetingLocation() {
        return meetingLocation;
    }

    public void setMeetingLocation(String meetingLocation) {
        this.meetingLocation = meetingLocation;
    }

    public Date getMeetingDateAndTime() {
        return meetingDateAndTime;
    }

    public void setMeetingTime(Date meetingDateAndTime) {
        this.meetingDateAndTime = meetingDateAndTime;
    }

    public String getFormattedMeetingDateAndTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);
        return timeFormat.format(meetingDateAndTime);
    }

    public String getMeetingParticipants() {
        return meetingParticipants;
    }

    public void setMeetingParticipants(String meetingParticipants) {
        this.meetingParticipants = meetingParticipants;
    }
}
