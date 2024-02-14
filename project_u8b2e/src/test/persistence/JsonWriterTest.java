package persistence;

import model.EventDate;
import model.EventsAgenda;
import model.Time;
import model.SportEvent;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            EventsAgenda agenda = new EventsAgenda();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            EventsAgenda agenda = new EventsAgenda();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyWorkroom.json");
            writer.open();
            writer.write(agenda);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyWorkroom.json");
            agenda = reader.read();
            assertEquals(0, agenda.getSize());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            EventsAgenda agenda = new EventsAgenda();
            SportEvent event1 = new SportEvent("A", "Swimming", new EventDate(27,8,2030),
                    new Time(13,0,15,43));
            SportEvent event2 = new SportEvent("B", "Boxing", new EventDate(20,8,2020),
                    new Time(11,15,15,15));
            event2.rateEvent(4);
            agenda.addEvent(event1);
            agenda.addEvent(event2);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralWorkroom.json");
            writer.open();
            writer.write(agenda);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralWorkroom.json");
            EventsAgenda test = reader.read();
            List<SportEvent> events = test.getEvents();
            assertEquals(2, events.size());
            checkEvent("A", "Swimming", 27, 8, 2030,
                    13, 0, 15, 43, 0, events.get(0));
            checkEvent("B", "Boxing", 20, 8, 2020,
                    11, 15, 15, 15, 4, events.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}