package persistence;

import model.EventsAgenda;
import model.SportEvent;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            EventsAgenda agenda = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyAgenda.json");
        try {
            EventsAgenda agenda = reader.read();
            assertEquals(0, agenda.getSize());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralAgenda.json");
        try {
            EventsAgenda agenda = reader.read();
            List<SportEvent> events = agenda.getEvents();
            assertEquals(2, events.size());
            checkEvent("A", "badminton", 12, 12, 2023,
                    14, 0, 19, 0, 0, events.get(0));
            checkEvent("B", "basketball", 25, 10, 2023,
                    7, 15, 8, 45, 5, events.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}