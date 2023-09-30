package com.example.mareu.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public abstract class DummyMeetingGenerator {

    public static List<Meeting> DUMMY_MEETINGS = Arrays.asList(

            new Meeting("Meeting one", "room 1", generateTime(2023, 9, 18, 8, 0), "maxime@lamzone.com, alex@lamzone.com"), // new array list
            new Meeting("Meeting two", "room 2", generateTime(2023, 9, 18,9,0),"stan@lamzone.com, mike@lamzone.com, paul@lamzone.com"),
            new Meeting("Meeting three", "room 3", generateTime(2023, 9, 18, 10, 00) , "mathilde@lamzone.com, nicolas@lamzone.com, amelie@lamzone.com, lucie@lamzone.com"),
            new Meeting("Meeting four", "room 4", generateTime(2023, 9, 18, 11, 00) , "camille@lamzone.com, olivia@lamzone.com, fanny@lamzone.com, juliette@lamzone.com, erika@lamzone.com"),
            new Meeting("Meeting five", "room 5", generateTime(2023, 9, 18, 13, 00) , "vincent@lamzone.com, axel@lamzone.com, laure@lamzone.com"),
            new Meeting("Meeting six", "room 6", generateTime(2023, 9, 18, 14, 00) , "erwan@lamzone.com, emma@lamzone.com"),
            new Meeting("Meeting seven", "room 7", generateTime(2023, 9, 18, 15, 00) , "olivier@lamzone.com, romain@lamzone.com, rudy@lamzone.com, monica@lamzone.com"),
            new Meeting("Meeting eight", "room 8", generateTime(2023, 9, 18, 16, 00) ,"maxence@lamzone.com, valentin@lamzone.com, elodie@lamzone.com"),
            new Meeting("Meeting nine", "room 9", generateTime(2023, 9, 18, 17, 00) , "william@lamzone.com, alan@lamzone.com, alexia@lamzone.com, louis@lamzone.com"),
            new Meeting("Meeting ten", "room 10", generateTime(2023, 9, 18, 18, 00) , "julien@lamzone.com, sam@lamzone.com")
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

    static List<Meeting> generateMeetings() {
        return new ArrayList<>(DUMMY_MEETINGS);
    }
}
