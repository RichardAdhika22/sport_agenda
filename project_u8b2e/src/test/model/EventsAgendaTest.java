package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventsAgendaTest {
    private EventsAgenda events;
    private SportEvent event1;
    private SportEvent event2;
    private SportEvent event3;
    private SportEvent event4;
    private SportEvent event5;
    private SportEvent event6;
    private SportEvent event7;
    private SportEvent event8;
    private SportEvent event9;
    private SportEvent event10;
    private SportEvent event11;
    private SportEvent event12;
    private SportEvent event13;

    @BeforeEach
    void runBefore() {
        LocalDate currentDate = LocalDate.now();
        int currentDay = currentDate.getDayOfMonth();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        events = new EventsAgenda();
        event1 = new SportEvent("US Open", "Tennis",
                new EventDate(24, 3,2020), new Time(11,0,14,0));
        event2 = new SportEvent("Hungary All Star", "Basketball",
                new EventDate(4, 11,2023), new Time(8,45,11,0));
        event3 = new SportEvent("Asian Games Semifinal", "Tennis",
                new EventDate(8, 11,2023), new Time(17,0,18,0));
        event4 = new SportEvent("Malaysia Open", "Badminton",
                new EventDate(8, 11,2020), new Time(17,0,18,0));
        event5 = new SportEvent("Singapore Open", "Badminton",
                new EventDate(8, 4,2023), new Time(17,0,18,0));
        event6 = new SportEvent("AB Tournament", "Basketball",
                new EventDate(8, 11,2023), new Time(9,45,12,15));
        event7 = new SportEvent("MLA Cup", "Soccer",
                new EventDate(8, 11,2023), new Time(8,0,9,30));
        event8 = new SportEvent("MLA Cup", "Soccer",
                new EventDate(8, 11,2023), new Time(8,30,9,15));

        event9 = new SportEvent("MLA Cup 1", "Soccer",
                new EventDate(currentDay - 1, currentMonth,currentYear), new Time(8,30,9,15));
        event10 = new SportEvent("MLA Cup 2", "Soccer",
                new EventDate(currentDay, currentMonth,currentYear), new Time(8,30,9,15));
        event11 = new SportEvent("MLA Cup 3", "Soccer",
                new EventDate(currentDay + 1, currentMonth,currentYear), new Time(8,30,9,15));
        event12 = new SportEvent("MLA Cup 4", "Soccer",
                new EventDate(currentDay + 1, currentMonth,currentYear), new Time(8,30,9,15));
        event13 = new SportEvent("MLA Cup 4", "Soccer",
                new EventDate(currentDay - 1, currentMonth + 1,currentYear), new Time(8,30,9,15));

        event1.rateEvent(8);
        event2.rateEvent(10);
        event3.rateEvent(1);
        event4.rateEvent(9);
        event5.rateEvent(7);
        event6.rateEvent(1);
        event7.rateEvent(5);
        event8.rateEvent(2);
    }

    @Test
    void constructorTest() {
        List<SportEvent> test = events.getEvents();
        assertTrue(test.isEmpty());
    }

    @Test
    void addSingleEventTest() {
        events.addEvent(event1);
        List<SportEvent> test = events.getEvents();
        assertEquals(1, test.size());
        assertEquals(event1, test.get(0));
    }

    @Test
    void addMultipleEventTest() {
        events.addEvent(event1);
        events.addEvent(event2);
        List<SportEvent> test = events.getEvents();
        assertEquals(2, test.size());
        assertEquals(event1, test.get(0));
        assertEquals(event2, test.get(1));
    }

    @Test
    void eventsByMonthEmptyTest() {
        events.addEvent(event1);
        List<SportEvent> test = events.eventsByMonth(5,2005);
        assertTrue(test.isEmpty());
    }

    @Test
    void eventsByMonthOneMatchTest() {
        events.addEvent(event1);
        events.addEvent(event2);
        List<SportEvent> test = events.eventsByMonth(3,2020);
        assertEquals(1, test.size());
        assertEquals(event1, test.get(0));
    }

    @Test
    void eventsByMonthMultipleMatchTest() {
        events.addEvent(event1);
        events.addEvent(event2);
        events.addEvent(event3);
        events.addEvent(event4);
        events.addEvent(event5);
        List<SportEvent> test = events.eventsByMonth(11,2023);
        assertEquals(2, test.size());
        assertEquals(event2, test.get(0));
        assertEquals(event3, test.get(1));
    }

    @Test
    void eventsByTypeEmptyTest() {
        events.addEvent(event1);
        List<SportEvent> test = events.eventsByType("Badminton");
        assertTrue(test.isEmpty());
    }

    @Test
    void eventsByTypeOneMatchTest() {
        events.addEvent(event1);
        events.addEvent(event2);
        List<SportEvent> test = events.eventsByType("Basketball");
        assertEquals(1, test.size());
        assertEquals(event2, test.get(0));
    }

    @Test
    void eventsByTypeMultipleMatchTest() {
        events.addEvent(event1);
        events.addEvent(event2);
        events.addEvent(event3);
        List<SportEvent> test = events.eventsByType("Tennis");
        assertEquals(2, test.size());
        assertEquals(event1, test.get(0));
        assertEquals(event3, test.get(1));
    }

    @Test
    void sortByRatingTest(){
        events.addEvent(event1);
        events.addEvent(event2);
        events.addEvent(event3);
        events.addEvent(event4);
        events.addEvent(event5);
        events.addEvent(event6);
        events.addEvent(event7);
        List<SportEvent> test = events.sortByRating();
        assertEquals(7, test.size());
        assertEquals(event2, test.get(0));
        assertEquals(event4, test.get(1));
        assertEquals(event1, test.get(2));
        assertEquals(event5, test.get(3));
        assertEquals(event7, test.get(4));
        assertEquals(event3, test.get(5));
        assertEquals(event6, test.get(6));
    }

    @Test
    void scheduleTest() {
        events.addEvent(event1);
        events.addEvent(event2);
        events.addEvent(event3);
        events.addEvent(event4);
        events.addEvent(event5);
        events.addEvent(event6);
        events.addEvent(event7);
        events.addEvent(event8);
        EventDate eventDate = new EventDate(8, 11, 2023);

        // 0 available time
        List<SportEvent> test1 = events.schedule(eventDate, 0, 0);
        assertEquals(0, test1.size());

        // available time barely enough to cover 1 event
        List<SportEvent> test2 = events.schedule(eventDate, 1, 30);
        assertEquals(1, test2.size());
        assertEquals(event7, test2.get(0));

        // available time enough to cover 1 event with a bit of surplus time
        List<SportEvent> test3 = events.schedule(eventDate, 1, 40);
        assertEquals(2, test3.size());
        assertEquals(event7, test3.get(0));
        event3.getTime().setEndHour(17);
        event3.getTime().setEndMin(10);
        assertEquals(event3, test3.get(1));

        // available time to cover all events
        List<SportEvent> test4 = events.schedule(eventDate, 6, 40);
        assertEquals(3, test4.size());
        assertEquals(event7, test4.get(0));
        assertEquals(event3, test4.get(1));
        assertEquals(event6, test4.get(2));
    }

    @Test
    void removeEventTest() {
        events.addEvent(event1);
        events.addEvent(event2);
        events.addEvent(event3);
        events.removeEvent(1);
        List<SportEvent> test = events.getEvents();
        assertEquals(2, test.size());
        assertEquals(event1, test.get(0));
        assertEquals(event3, test.get(1));
    }

    @Test
    void suggestedEventsTest() {
        events.addEvent(event8);
        events.addEvent(event9);
        events.addEvent(event10);
        events.addEvent(event11);
        events.addEvent(event12);
        events.addEvent(event13);
        List<SportEvent> test = events.suggestedEventsFilter();
        assertEquals(3, test.size());
        assertEquals(event10, test.get(0));
        assertEquals(event11, test.get(1));
        assertEquals(event12, test.get(2));
    }

    @Test
    void addCopySingleEventTest() {
        events.addEventCopy(event1);
        List<SportEvent> test = events.getEvents();
        assertEquals(1, test.size());
        assertEquals(event1, test.get(0));
    }

    @Test
    public void testViewAllEvent() {
        events.viewAll();
        List<Event> l = new ArrayList<Event>();

        EventLog el = EventLog.getInstance();
        for (Event next : el) {
            l.add(next);
        }
        assertTrue(l.contains(new Event ("Viewed all events in the agenda")));
    }

    @Test
    public void testLoadedEvent() {
        events.loaded();
        List<Event> l = new ArrayList<Event>();

        EventLog el = EventLog.getInstance();
        for (Event next : el) {
            l.add(next);
        }
        assertTrue(l.contains(new Event ("Loaded events from the JSON file")));
    }
}