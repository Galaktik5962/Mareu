package com.example.mareu.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Utility class for generating dummy meetings and associated data for testing purposes.
 */
public abstract class DummyMeetingGenerator {

    /**
     * List of dummy meeting rooms.
     */
    public static List<String> DUMMY_ROOMS = Arrays.asList(
            "Room 1", "Room 2", "Room 3", "Room 4", "Room 5",
            "Room 6", "Room 7", "Room 8", "Room 9", "Room 10"
    );

    /**
     * List of dummy meetings with predefined data.
     */
    public static List<Meeting> DUMMY_MEETINGS = Arrays.asList(
            new Meeting("Meeting one", DUMMY_ROOMS.get(0), generateTime(8, 00), generateDate(2023, 12, 4),
                    Arrays.asList("maxime@lamzone.com, alex@lamzone.com")),
            new Meeting("Meeting two", DUMMY_ROOMS.get(1), generateTime(9, 00), generateDate(2023, 12, 4),
                    Arrays.asList("stan@lamzone.com, mike@lamzone.com, paul@lamzone.com")),
            new Meeting("Meeting three", DUMMY_ROOMS.get(2), generateTime(10, 00), generateDate(2023, 12, 4),
                    Arrays.asList("mathilde@lamzone.com, nicolas@lamzone.com, amelie@lamzone.com, lucie@lamzone.com")),
            new Meeting("Meeting four", DUMMY_ROOMS.get(3), generateTime(11, 00), generateDate(2023, 12, 4),
                    Arrays.asList("camille@lamzone.com, olivia@lamzone.com, fanny@lamzone.com, juliette@lamzone.com, erika@lamzone.com")),
            new Meeting("Meeting five", DUMMY_ROOMS.get(4), generateTime(13, 00), generateDate(2023, 12, 4),
                    Arrays.asList("vincent@lamzone.com, axel@lamzone.com, laure@lamzone.com")),
            new Meeting("Meeting six", DUMMY_ROOMS.get(5), generateTime(14, 00), generateDate(2023, 12, 4),
                    Arrays.asList("erwan@lamzone.com, emma@lamzone.com")),
            new Meeting("Meeting seven", DUMMY_ROOMS.get(6), generateTime(15, 00), generateDate(2023, 12, 4),
                    Arrays.asList("olivier@lamzone.com, romain@lamzone.com, rudy@lamzone.com, monica@lamzone.com")),
            new Meeting("Meeting eight", DUMMY_ROOMS.get(7), generateTime(16, 00), generateDate(2023, 12, 4),
                    Arrays.asList("maxence@lamzone.com, valentin@lamzone.com, elodie@lamzone.com")),
            new Meeting("Meeting nine", DUMMY_ROOMS.get(8), generateTime(17, 00), generateDate(2023, 12, 4),
                    Arrays.asList("william@lamzone.com, alan@lamzone.com, alexia@lamzone.com, louis@lamzone.com")),
            new Meeting("Meeting ten", DUMMY_ROOMS.get(9), generateTime(18, 00), generateDate(2023, 12, 4),
                    Arrays.asList("julien@lamzone.com, sam@lamzone.com"))
    );

    /**
     * Generates a Calendar instance representing a specific time.
     *
     * @param hour   The hour of the day (24-hour format).
     * @param minute The minute of the hour.
     * @return A Calendar instance representing the specified time.
     */
    public static Calendar generateTime(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar;
    }

    /**
     * Generates a Calendar instance representing a specific date.
     *
     * @param year  The year.
     * @param month The month (0-based).
     * @param day   The day of the month.
     * @return A Calendar instance representing the specified date.
     */
    public static Calendar generateDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar;
    }

    /**
     * Generates a list of dummy meetings.
     *
     * @return A list of dummy meetings.
     */
    public static List<Meeting> generateMeetings() {
        return new ArrayList<>(DUMMY_MEETINGS);
    }

    /**
     * Gets the list of dummy meeting rooms.
     *
     * @return The list of dummy meeting rooms.
     */
    public static List<String> getDUMMY_ROOMS() {
        return DUMMY_ROOMS;
    }
}
