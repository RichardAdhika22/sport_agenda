package model;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

// Represents a date with day, month, and year.
public class EventDate {
    private final int day;
    private final int month;
    private final int year;

    // REQUIRES: a valid date between 1-31, a valid month between 1-12,
    //           and a valid year between 1900-2099.
    // EFFECTS: construct a date with given day, month, and year
    public EventDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    // REQUIRES: this.date is later than the given parameter(currentDay-currentMonth-currentYear)
    // EFFECTS: compute the difference between the given date and this.date,
    //          return the number in which the first two digits represent the year, the next two digits represent the
    //          month and the last two digits represent the day.
    public int dayDifference(int currentDay, int currentMonth, int currentYear) {
        int eventDay = this.day;
        int eventMonth = this.month;
        int eventYear = this.year;

        if (currentDay > eventDay) {
            eventMonth--;
            eventDay += dayPerMonth(currentMonth);
        }
        if (currentMonth > eventMonth) {
            eventYear--;
            eventMonth += 12;
        }

        return (eventDay - currentDay) + (eventMonth - currentMonth) * 100
                + (eventYear - currentYear) * 10000;
    }

    // REQUIRES: valid month, between 1-12
    // EFFECTS: return the number of day per month according to the standard calendar
    //          (ignoring the leap year).
    public int dayPerMonth(int month) {
        List<Integer> day31 = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 8, 10, 12));

        if (day31.contains(month)) {
            return 31;
        } else if (month == 2) {
            return 28;
        } else {
            return 30;
        }
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}
