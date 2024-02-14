package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.List;
import java.util.ArrayList;

// Collection of Sport Events
public class EventsAgenda implements Writable {
    List<SportEvent> events;

    // EFFECT: construct an empty list of sport events.
    public EventsAgenda() {
        events = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECT: add a sport event into the list.
    public void addEvent(SportEvent event) {
        events.add(event);
        EventLog.getInstance().logEvent(new Event("Added " + event.getName() + " to the agenda"));
    }

    // EFFECT: record the "view all" action
    public void viewAll() {
        EventLog.getInstance().logEvent(new Event("Viewed all events in the agenda"));
    }

    // REQUIRES: valid month (between 1-12) and valid year (1900-2099).
    // EFFECT: return a list of sport events that matches the month and the year.
    public List<SportEvent> eventsByMonth(int month, int year) {
        List<SportEvent> result = new ArrayList<>();
        for (SportEvent event : events) {
            if (event.getDate().getMonth() == month && event.getDate().getYear() == year) {
                result.add(event);
            }
        }
        EventLog.getInstance().logEvent(new Event("Viewed events by month " + month + ", year " + year));
        return result;
    }

    // EFFECTS: return a list of sport events that matches the given sport type.
    public List<SportEvent> eventsByType(String type) {
        type = type.toLowerCase();
        List<SportEvent> result = new ArrayList<>();
        for (SportEvent event : events) {
            String eventType = event.getType().toLowerCase();
            if (eventType.equals(type)) {
                result.add(event);
            }
        }
        EventLog.getInstance().logEvent(new Event("Viewed events by type " + type));
        return result;
    }

    // REQUIRES: no two overlapping events' times
    // EFFECTS: schedule a list of sport events to watch on a given date based on the duration available.
    //          Events are prioritized based on the rating and if there is equal rating between two events,
    //          the one added first will be prioritized.
    //          Overlapped events are adjusted based on the priority too.
    public List<SportEvent> schedule(EventDate eventDate, int hour, int min) {
        List<SportEvent> modifiedEvents = sortByRating();
        modifiedEvents = filterDate(eventDate, modifiedEvents);
        modifiedEvents = adjustOverlapping(modifiedEvents);

        List<SportEvent> result = new ArrayList<>();
        int duration = hour * 60 + min;

        for (SportEvent event : modifiedEvents) {
            if (duration <= 0) {
                break;
            }
            int eventDuration = event.getTime().countDuration();
            if (eventDuration <= duration) {
                duration -= eventDuration;
            } else {
                event.getTime().adjustEndTime(duration);
                duration = 0;
            }
            result.add(event);
        }
        EventLog.getInstance().logEvent(new Event("Viewed a schedule on " + eventDate.getDay() + "/"
                + eventDate.getMonth() + "/" + eventDate.getYear()));
        return result;
    }

    // EFFECTS: sort the list of sport events by its rating;
    //          If there is an equal rating between two events, the first one added will come first.
    public List<SportEvent> sortByRating() {
        List<SportEvent> sorted = new ArrayList<>();

        for (int i = 10; i >= 0; i--) {
            for (SportEvent event : events) {
                if (event.getRating() == i) {
                    sorted.add(event);
                }
            }
        }
        return sorted;
    }

    // EFFECTS: return top 3 closest events from today
    public List<SportEvent> suggestedEventsFilter() {
        List<SportEvent> filteredEvents = new ArrayList<>();
        List<SportEvent> result = new ArrayList<>();

        int maxDistance = 0;

        for (SportEvent event : events) {
            int distance = event.dayUntilEvent();
            if (distance >= 0) {
                filteredEvents.add(event);
            }
            if (distance > maxDistance) {
                maxDistance = distance;
            }
        }

        for (int day = 0; day <= maxDistance; day++) {
            for (SportEvent event : filteredEvents) {
                if (result.size() == 3) {
                    break;
                }
                if (event.dayUntilEvent() == day) {
                    result.add(event);
                }
            }
        }

        return result;
    }

    // EFFECTS: return a list of events with the given date.
    private List<SportEvent> filterDate(EventDate eventDate, List<SportEvent> events) {
        List<SportEvent> filterDate = new ArrayList<>();

        for (SportEvent event : events) {
            if (eventDate.getDay() == event.getDate().getDay() && eventDate.getMonth() == event.getDate().getMonth()
                    && eventDate.getYear() == event.getDate().getYear()) {
                filterDate.add(event);
            }
        }
        return filterDate;
    }

    // EFFECTS: adjust overlapping events
    private List<SportEvent> adjustOverlapping(List<SportEvent> events) {
        List<SportEvent> removedOverlapping = new ArrayList<>();
        for (SportEvent event : events) {
            for (SportEvent adjusted : removedOverlapping) {
                event.compareOverlapping(adjusted);
            }
            if (event.getTime().countDuration() != 0) {
                removedOverlapping.add(event);
            }
        }
        return removedOverlapping;
    }

    // EFFECTS: return the size of the events
    public int getSize() {
        return events.size();
    }

    // MODIFIES: this
    // EFFECTS: remove an event from the agenda
    public void removeEvent(int index) {
        EventLog.getInstance().logEvent(new Event("Removed " + events.get(index).getName() + " from the agenda"));
        events.remove(index);
    }

    // MODIFIES: this
    // EFFECT: add a sport event into the list only for the purpose for creating a copy and therefore
    //         will not be saved in the events log
    public void addEventCopy(SportEvent event) {
        events.add(event);
    }

    public List<SportEvent> getEvents() {
        return events;
    }

    // EFFECTS: put all the events that have been added to the JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("events", eventsToJson());
        EventLog.getInstance().logEvent(new Event("Saved Events"));
        return json;
    }

    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray eventsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (SportEvent event : events) {
            jsonArray.put(event.toJson());
        }

        return jsonArray;
    }

    // EFFECT: record the "load events" action
    public void loaded() {
        EventLog.getInstance().logEvent(new Event("Loaded events from the JSON file"));
    }
}
