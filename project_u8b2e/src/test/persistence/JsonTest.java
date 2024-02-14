package persistence;

import model.EventDate;
import model.Time;
import model.SportEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkEvent(String name, String type, int day, int month, int year, int startHour, int startMin,
                              int endHour, int endMin, int rating, SportEvent event) {
        assertEquals(name, event.getName());
        assertEquals(type, event.getType());
        EventDate eventDate = event.getDate();
        assertEquals(day, eventDate.getDay());
        assertEquals(month, eventDate.getMonth());
        assertEquals(year, eventDate.getYear());
        Time time = event.getTime();
        assertEquals(startHour, time.getStartHour());
        assertEquals(startMin, time.getStartMin());
        assertEquals(endHour, time.getEndHour());
        assertEquals(endMin, time.getEndMin());
        assertEquals(rating, event.getRating());
    }
}
