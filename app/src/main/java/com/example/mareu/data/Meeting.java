package com.example.mareu.data;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Meeting {

    private String subjectOfMeeting;
    private String meetingLocation;
    private Date meetingDateAndTime;
    private List<String> meetingParticipants;

    public Meeting (String subjectOfMeeting, String meetingLocation, Date meetingDateAndTime, List<String> meetingParticipants) {
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

    public List<String> getMeetingParticipants() {
        return meetingParticipants;
    }

    public void setMeetingParticipants(List<String> meetingParticipants) {
        this.meetingParticipants = meetingParticipants;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Meeting meeting = (Meeting) obj;

        // Comparez les propriétés pertinentes ici (par exemple, salle et date)
        return meetingLocation.equals(meeting.meetingLocation) && meetingDateAndTime.equals(meeting.meetingDateAndTime);
    }

    @Override
    public int hashCode() {
        int result = meetingLocation.hashCode();
        result = 31 * result + meetingLocation.hashCode();
        return result;
    }

}
