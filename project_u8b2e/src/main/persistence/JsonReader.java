package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import model.EventsAgenda;
import model.EventDate;
import model.Time;
import model.SportEvent;
import org.json.*;

// Represents a reader that reads agenda from JSON data stored in file
public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads agenda from file and returns it;
    // throws IOException if an error occurs reading data from file
    public EventsAgenda read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseAgenda(jsonObject);

    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses workroom from JSON object and returns it
    private EventsAgenda parseAgenda(JSONObject jsonObject) {
        EventsAgenda agenda = new EventsAgenda();
        addEvents(agenda, jsonObject);
        return agenda;
    }

    // MODIFIES: events
    // EFFECTS: parses thingies from JSON object and adds them to workroom
    private void addEvents(EventsAgenda events, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("events");
        for (Object json : jsonArray) {
            JSONObject nextEvent = (JSONObject) json;
            addEvent(events, nextEvent);
        }
    }

    // MODIFIES: events
    // EFFECTS: parses event from JSON object and adds it to workroom
    private void addEvent(EventsAgenda events, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String type = jsonObject.getString("type");
        int day = jsonObject.getInt("day");
        int month = jsonObject.getInt("month");
        int year = jsonObject.getInt("year");
        int startHour = jsonObject.getInt("startHour");
        int startMin = jsonObject.getInt("startMin");
        int endHour = jsonObject.getInt("endHour");
        int endMin = jsonObject.getInt("endMin");
        int rating = jsonObject.getInt("rating");
        EventDate eventDate = new EventDate(day, month, year);
        Time time = new Time(startHour, startMin, endHour, endMin);
        SportEvent event = new SportEvent(name, type, eventDate, time);
        event.rateEvent(rating);
        events.addEvent(event);
    }
}
