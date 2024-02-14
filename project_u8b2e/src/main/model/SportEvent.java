package model;

import persistence.Writable;
import org.json.JSONObject;

import java.time.LocalDate;

// Represents a sport events to watch having name, type, date, and time.
public class SportEvent implements Writable {
    private final String name;
    private final String type;
    private final EventDate eventDate;
    private final Time time;
    private int rating;

    // EFFECTS: Construct a sport event with the name, the type of sport, date and time.
    public SportEvent(String name, String type, EventDate eventDate, Time time) {
        this.name = name;
        this.type = type;
        this.eventDate = eventDate;
        this.time = time;
        this.rating = 0;
    }

    // EFFECTS: calculate how many days from today until an event.
    //          Return the number in which the first two digits represent the year, the next two digits represent the
    //          month and the last two digits represent the day.
    public int dayUntilEvent() {
        LocalDate currentDate = LocalDate.now();
        int currentDay = currentDate.getDayOfMonth();
        int currentMonth = currentDate.getMonthValue();
        int currentYear = currentDate.getYear();

        return eventDate.dayDifference(currentDay, currentMonth, currentYear);
    }

    // REQUIRES: rating is an integer between 1-10 inclusive.
    // MODIFIES: this
    // EFFECTS: give a rating to a sport event.
    public void rateEvent(int rating) {
        this.rating = rating;
        EventLog.getInstance().logEvent(new Event("Rated " + name + " with score " + rating));
    }

    // REQUIRES: rating is an integer between 1-10 inclusive.
    // MODIFIES: this
    // EFFECTS: give a rating to a sport event only for the sake of creating a copy, and therefore will not be
    //          saved in the events log
    public void rateEventCopy(int rating) {
        this.rating = rating;
    }

    // EFFECTS: return "not rated" if no rating has been given to the event, return the rating value otherwise.
    public String printRating() {
        if (rating == 0) {
            return "Not Rated";
        } else {
            return Integer.toString(rating);
        }
    }

    // MODIFIES: this
    // EFFECTS: change the starting time or ending time so that it does not overlap with the adjusted event.
    //          The first added to the list is always prioritized, therefore if there is second event that overlaps
    //          with it, that second event must adjust either its starting or ending time.
    //          In the case where the second event completely overlap with the first event, we can only watch the
    //          second event from its start up to the starting time of the first event.
    public void compareOverlapping(SportEvent adjusted) {
        boolean startOverlap = time.isOverlapTime(adjusted.getTime(), true);
        boolean endOverlap = time.isOverlapTime(adjusted.getTime(), false);
        boolean completeOverlap = time.isCompletelyOverlap(adjusted.getTime());

        if (startOverlap && endOverlap) {
            time.setStartHour(time.getEndHour());
            time.setStartMin(time.getEndMin());
        } else if (startOverlap) {
            time.setStartHour(adjusted.getTime().getEndHour());
            time.setStartMin(adjusted.getTime().getEndMin());
        } else if (endOverlap || completeOverlap) {
            time.setEndHour(adjusted.getTime().getStartHour());
            time.setEndMin(adjusted.getTime().getStartMin());
        }
    }

    public EventDate getDate() {
        return eventDate;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getRating() {
        return rating;
    }

    public Time getTime() {
        return time;
    }

    // EFFECTS: deconstruct the elements of a sport event so that it can be stored in a Json file
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("type", type);
        json.put("day", eventDate.getDay());
        json.put("month", eventDate.getMonth());
        json.put("year", eventDate.getYear());
        json.put("startHour", time.getStartHour());
        json.put("startMin", time.getStartMin());
        json.put("endHour", time.getEndHour());
        json.put("endMin", time.getEndMin());
        json.put("rating", rating);
        return json;
    }
}
