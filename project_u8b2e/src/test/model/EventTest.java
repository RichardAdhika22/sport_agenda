package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

// Unit tests for the Event class
class EventTest {
    private Event e;
    private Date d;

    @BeforeEach
    public void runBefore() {
        e = new Event("Added an event");
        d = Calendar.getInstance().getTime();
    }

    @Test
    public void testEvent() {
        assertEquals("Added an event", e.getDescription());
        assertEquals(d, e.getDate());
    }

    @Test
    public void testToString() {
        assertEquals(d.toString() + "\n" + "Added an event", e.toString());
    }

    @Test
    public void testHashCode() {
        Event test = new Event("Added an event");
        assertEquals(test.hashCode(), e.hashCode());
    }

    @Test
    public void testEqual() {
        assertFalse(e.equals(null));
        SportEvent test1 = new SportEvent("A", "soccer", new EventDate(12,12,2000),
                new Time(12,0,13,30));
        assertFalse(e.equals(test1));

        Event test2 = new Event("Added an event");
        assertTrue(e.equals(test2));

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {

        }
        Event test3 = new Event("Added an event");
        assertFalse(e.equals(test3));
    }
}
