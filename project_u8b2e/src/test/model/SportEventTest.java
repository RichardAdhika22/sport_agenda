package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SportEventTest {
    private SportEvent event;
    private SportEvent event1;
    private SportEvent event2;
    private SportEvent event3;
    private SportEvent event4;
    private SportEvent event5;
    private EventDate eventDateTest;
    private Time timeTest;

    @BeforeEach
    void runBefore() {
        eventDateTest = new EventDate(23, 9, 2023);
        timeTest = new Time(19,0,21,30);
        event = new SportEvent("A", "Tennis", eventDateTest, timeTest);
        event1 = new SportEvent("B", "Tennis",
                new EventDate(23, 9, 2023), new Time(19,30,20,30));
        event2 = new SportEvent("C", "Tennis",
                new EventDate(23, 9, 2023), new Time(20,0,23,30));
        event3 = new SportEvent("D", "Tennis",
                new EventDate(23, 9, 2023), new Time(17,0,20,30));
        event4 = new SportEvent("E", "Tennis",
                new EventDate(23, 9, 2023), new Time(17,0,23,30));
        event5 = new SportEvent("F", "Tennis",
                new EventDate(23, 9, 2023), new Time(13,0,17,30));
    }

    @Test
    void testConstructor() {
        assertEquals("A", event.getName());
        assertEquals("Tennis", event.getType());
        assertEquals(eventDateTest, event.getDate());
        assertEquals(timeTest, event.getTime());
        assertEquals(0, event.getRating());
    }

    // Only test that it accurately calculate the difference between the event's date and today's date
    // Various cases are similar to the test in DateTest.
    @Test
    void dayUntilEventTest() {
        LocalDate currentDate = LocalDate.now();
        int currentDay = currentDate.getDayOfMonth();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();
        EventDate eventDate = new EventDate(23, 9, 2023);

        int test = event.dayUntilEvent();
        int expected = eventDate.dayDifference(currentDay, currentMonth, currentYear);

        assertEquals(expected, test);
    }

    @Test
    void ratingTest() {
        event.rateEvent(5);
        assertEquals(5, event.getRating());
    }

    @Test
    void ratingCopyTest() {
        event.rateEventCopy(5);
        assertEquals(5, event.getRating());
    }

    @Test
    void printRatingNotRatedTest() {
        assertEquals("Not Rated", event.printRating());
    }

    @Test
    void printRatingRatedTest() {
        event.rateEvent(5);
        assertEquals("5", event.printRating());
    }

    // the starting time of event is later than starting time of event1
    // and the ending time of event is earlier than the ending time of event1.
    @Test
    void compareOverlappingTest1() {
        event1.compareOverlapping(event);
        Time time = event1.getTime();
        assertEquals(20, time.getStartHour());
        assertEquals(30, time.getStartMin());
        assertEquals(20, time.getEndHour());
        assertEquals(30, time.getEndMin());
    }

    // the starting time of event is earlier than the ending time of event2
    // and the ending time of event is later than the ending time of event2.
    @Test
    void compareOverlappingTest2() {
        event2.compareOverlapping(event);
        Time time = event2.getTime();
        assertEquals(21, time.getStartHour());
        assertEquals(30, time.getStartMin());
        assertEquals(23, time.getEndHour());
        assertEquals(30, time.getEndMin());
    }

    // the starting time of event is earlier than the starting time of event3
    // and the ending time of event is earlier than the ending time of event3.
    @Test
    void compareOverlappingTest3() {
        event3.compareOverlapping(event);
        Time time = event3.getTime();
        assertEquals(17, time.getStartHour());
        assertEquals(0, time.getStartMin());
        assertEquals(19, time.getEndHour());
        assertEquals(0, time.getEndMin());
    }

    // the starting time of event is earlier than the starting time of event4
    // and the ending time of event is later than the ending time of event4.
    @Test
    void compareOverlappingTest4() {
        event4.compareOverlapping(event);
        Time time = event4.getTime();
        assertEquals(17, time.getStartHour());
        assertEquals(0, time.getStartMin());
        assertEquals(19, time.getEndHour());
        assertEquals(0, time.getEndMin());
    }

    // two times are not overlapping with each other at all
    @Test
    void compareOverlappingTest5() {
        event5.compareOverlapping(event);
        Time time = event5.getTime();
        assertEquals(13, time.getStartHour());
        assertEquals(0, time.getStartMin());
        assertEquals(17, time.getEndHour());
        assertEquals(30, time.getEndMin());
    }
}