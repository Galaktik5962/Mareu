package com.example.mareu.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public abstract class DummyMeetingGenerator {

    public static List<String> DUMMY_ROOMS = Arrays.asList(
            "Room 1",
            "Room 2",
            "Room 3",
            "Room 4",
            "Room 5",
            "Room 6",
            "Room 7",
            "Room 8",
            "Room 9",
            "Room 10"
    );

    public static List<Meeting> DUMMY_MEETINGS = Arrays.asList(

            new Meeting("Meeting one", DUMMY_ROOMS.get(0), generateTime(2023, 9, 18, 8, 0), Arrays.asList("maxime@lamzone.com, alex@lamzone.com")),
            new Meeting("Meeting two", DUMMY_ROOMS.get(1), generateTime(2023, 9, 18,9,0), Arrays.asList("stan@lamzone.com, mike@lamzone.com, paul@lamzone.com")),
            new Meeting("Meeting three", DUMMY_ROOMS.get(2), generateTime(2023, 9, 18, 10, 00) , Arrays.asList("mathilde@lamzone.com, nicolas@lamzone.com, amelie@lamzone.com, lucie@lamzone.com")),
            new Meeting("Meeting four", DUMMY_ROOMS.get(3), generateTime(2023, 9, 18, 11, 00) , Arrays.asList("camille@lamzone.com, olivia@lamzone.com, fanny@lamzone.com, juliette@lamzone.com, erika@lamzone.com")),
            new Meeting("Meeting five", DUMMY_ROOMS.get(4), generateTime(2023, 9, 18, 13, 00) , Arrays.asList("vincent@lamzone.com, axel@lamzone.com, laure@lamzone.com")),
            new Meeting("Meeting six", DUMMY_ROOMS.get(5), generateTime(2023, 9, 18, 14, 00) , Arrays.asList("erwan@lamzone.com, emma@lamzone.com")),
            new Meeting("Meeting seven", DUMMY_ROOMS.get(6), generateTime(2023, 9, 18, 15, 00) , Arrays.asList("olivier@lamzone.com, romain@lamzone.com, rudy@lamzone.com, monica@lamzone.com")),
            new Meeting("Meeting eight", DUMMY_ROOMS.get(7), generateTime(2023, 9, 18, 16, 00) , Arrays.asList("maxence@lamzone.com, valentin@lamzone.com, elodie@lamzone.com")),
            new Meeting("Meeting nine", DUMMY_ROOMS.get(8), generateTime(2023, 9, 18, 17, 00) , Arrays.asList("william@lamzone.com, alan@lamzone.com, alexia@lamzone.com, louis@lamzone.com")),
            new Meeting("Meeting ten", DUMMY_ROOMS.get(9), generateTime(2023, 9, 18, 18, 00) , Arrays.asList("julien@lamzone.com, sam@lamzone.com"))
    );

    public static Date generateTime (int year, int month, int day, int hour, int minute) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.YEAR, year);
        calendar.set(calendar.MONTH, month - 1);
        calendar.set(calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        return calendar.getTime();
    }

    public static List<Meeting> generateMeetings() {
        return new ArrayList<>(DUMMY_MEETINGS);
    }

    public static List<String> getDUMMY_ROOMS() {
        return DUMMY_ROOMS;
    }
}

