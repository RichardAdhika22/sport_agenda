package model;

import java.util.Calendar;
import java.util.Date;

// Represents a sport agenda event.
public class Event {
    private static final int HASH_CONSTANT = 13;
    private Date dateLogged;
    private String description;

    // EFFECTS: creates an event with the given description and the current date/time
    public Event(String description) {
        dateLogged = Calendar.getInstance().getTime();
        this.description = description;
    }

    // EFFECTS: return the date of the event
    public Date getDate() {
        return dateLogged;
    }

    // EFFECTS: return the description of the event
    public String getDescription() {
        return description;
    }

    // EFFECTS: overriding the equal method so that any two events with the same date and description
    //          will be regarded as equals
    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (other.getClass() != this.getClass()) {
            return false;
        }

        Event otherEvent = (Event) other;

        return (this.dateLogged.equals(otherEvent.dateLogged)
                && this.description.equals(otherEvent.description));
    }

    // EFFECTS: reassign the hash code of the object
    @Override
    public int hashCode() {
        return (HASH_CONSTANT * dateLogged.hashCode() + description.hashCode());
    }

    // EFFECTS: override toString method so that it returns the date and description
    @Override
    public String toString() {
        return dateLogged.toString() + "\n" + description;
    }
}
