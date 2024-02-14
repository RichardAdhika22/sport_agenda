package ui;

import model.EventDate;
import model.EventsAgenda;
import model.SportEvent;
import model.Time;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.List;
import java.util.Scanner;
import java.util.regex.*;

// Sport Agenda Application
public class AgendaApp {
    private static final String JSON_STORE = "./data/events.json";
    private final JsonReader jsonReader;
    private final JsonWriter jsonWriter;
    private Scanner input;
    private EventsAgenda agenda;

    // EFFECTS: run the agenda application.
    public AgendaApp() throws FileNotFoundException {
        agenda = new EventsAgenda();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runAgenda();
    }

    // MODIFIES: this
    // EFFECTS: asks and processes user input
    private void runAgenda() {
        boolean keepGoing = true;
        String command;
        input = new Scanner(System.in);
        input.useDelimiter("\n");

        while (keepGoing) {
            displayMenu();
            command = input.next();

            if (command.equals("9")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nHave a nice day!");
    }

    // EFFECTS: display menu of options to the user.
    private void displayMenu() {
        System.out.println("\nMenu Selection");
        System.out.println("\t1. Add a sport event");
        System.out.println("\t2. View sport events");
        System.out.println("\t3. See how long until an event");
        System.out.println("\t4. Rate a sport event");
        System.out.println("\t5. Manage watching schedule on a certain date");
        System.out.println("\t6. Remove a sport event");
        System.out.println("\t7. Save agenda to file");
        System.out.println("\t8. Load agenda from file");
        System.out.println("\t9. Quit");
        System.out.println("-".repeat(40));
    }

    // MODIFIES: this
    // EFFECTS: call the function based on the user's input.
    private void processCommand(String command) {
        if (command.equals("1")) {
            addEvent();
        } else if (command.equals("2")) {
            viewEvents();
        } else if (command.equals("3")) {
            countHowLong();
        } else if (command.equals("4")) {
            rateEvent();
        } else if (command.equals("5")) {
            scheduleEvents();
        } else if (command.equals("6")) {
            removeEvent();
        } else if (command.equals("7")) {
            saveAgenda();
        } else if (command.equals("8")) {
            loadAgenda();
        } else {
            System.out.println("Invalid selection!");
        }
    }

    // MODIFIES: this
    // EFFECTS: add an event to the events list with appropriate date and time
    private void addEvent() {
        System.out.print("Enter event name: ");
        String nameInput = input.next();
        System.out.print("Enter sport type: ");
        String typeInput = input.next();

        System.out.print("Enter date (dd/mm/yyyy): ");
        String dateInput = input.next();
        if (!matchDate(dateInput)) {
            System.out.println("Invalid date!");
            return;
        }
        EventDate eventDate = convertDate(dateInput);
        System.out.print("Enter time (hh:mm-hh:mm): ");
        String timeInput = input.next();

        if (!timeProcess(timeInput)) {
            return;
        }
        Time time = convertTime(timeInput);

        SportEvent event = new SportEvent(nameInput, typeInput, eventDate, time);
        agenda.addEvent(event);
    }

    private boolean timeProcess(String timeInput) {

        if (!matchTime(timeInput)) {
            System.out.println("Invalid time input!");
            return false;
        }
        Time time = convertTime(timeInput);
        if (time.getEndHour() < time.getStartHour() || ((time.getEndHour() == time.getStartHour())
                && (time.getEndMin() < time.getStartMin()))) {
            System.out.println("Invalid time: Start time must be earlier than End time");
            return false;
        }
        return true;
    }

    // EFFECTS: convert a string of date to the Date object.
    private EventDate convertDate(String dateInput) {
        int day = Integer.parseInt(dateInput.substring(0,2));
        int month = Integer.parseInt(dateInput.substring(3,5));
        int year = Integer.parseInt(dateInput.substring(6,10));
        return new EventDate(day, month, year);
    }

    // EFFECTS: convert a string of time to the Time object.
    private Time convertTime(String timeInput) {
        int startHour = Integer.parseInt(timeInput.substring(0,2));
        int startMin = Integer.parseInt(timeInput.substring(3,5));
        int endHour = Integer.parseInt(timeInput.substring(6,8));
        int endMin = Integer.parseInt(timeInput.substring(9,11));
        return new Time(startHour, startMin, endHour, endMin);
    }

    // EFFECTS: view a list of events based on the selected categories
    private void viewEvents() {
        System.out.println("Select categories to view by: ");
        System.out.println("\t1. Month");
        System.out.println("\t2. Sport type");
        System.out.println("\t3. All");
        int categorySelect = input.nextInt();
        if (categorySelect == 1) {
            viewByMonth();
        } else if (categorySelect == 2) {
            viewByType();
        } else if (categorySelect == 3) {
            printEvents(agenda.getEvents(), true);
        }
    }

    // EFFECTS: view a list of events based on the selected month
    private void viewByMonth() {
        System.out.print("Select month (1-12) and year (mm-yyyy): ");
        String monthSelect = input.next();
        int month = Integer.parseInt(monthSelect.substring(0,2));
        int year = Integer.parseInt(monthSelect.substring(3,5));
        List<SportEvent> result = agenda.eventsByMonth(month, year);
        printEvents(result, true);
    }

    // EFFECTS: view a list of events based on the selected sport type
    private void viewByType() {
        System.out.println("Select type: ");
        String typeSelect = input.next();
        typeSelect = typeSelect.toLowerCase();
        List<SportEvent> result = agenda.eventsByType(typeSelect);
        printEvents(result, true);
    }

    // EFFECTS: calculate and print how long from today until an event.
    private void countHowLong() {
        int eventSelect = askEvent("calculate how long from now");
        if (eventSelect == 0) {
            return;
        }

        SportEvent event = agenda.getEvents().get(eventSelect - 1);
        int howLong = event.dayUntilEvent();
        System.out.println((howLong / 10000) + " year(s) " + ((howLong / 100) % 100) + " month(s) "
                + (howLong % 100) + " day(s) until " + event.getName());
    }

    // MODIFIES: this
    // EFFECTS: give a rating to a selected event
    private void rateEvent() {
        int eventSelect = askEvent("rate");
        if (eventSelect == 0) {
            return;
        }

        System.out.print("Select a rating (1-10): ");
        int rating = input.nextInt();
        if (rating < 1 || rating > 10) {
            System.out.println("Invalid rating!");
            return;
        }

        SportEvent event = agenda.getEvents().get(eventSelect - 1);
        event.rateEvent(rating);
        System.out.println("You gave a score of " + rating + "/10 to " + event.getName());
    }

    // EFFECTS: give a list of events that can be watch given the time available on a certain date
    // based on the rating.
    private void scheduleEvents() {
        System.out.print("Enter date (dd/mm/yyyy): ");
        String dateInput = input.next();
        if (!matchDate(dateInput)) {
            System.out.println("Invalid date!");
            return;
        }
        EventDate eventDate = convertDate(dateInput);

        System.out.print("Enter hours available (hh:mm): ");
        String durationInput = input.next();
        if (!matchDuration(durationInput)) {
            System.out.println("Invalid duration!");
            return;
        }
        int hour = Integer.parseInt(durationInput.substring(0,2));
        int min = Integer.parseInt(durationInput.substring(3,5));
        EventsAgenda agendaSchedule = agendaSchedule();

        List<SportEvent> result = agendaSchedule.schedule(eventDate, hour, min);
        printEvents(result, true);
    }

    // EFFECTS: create a new copy of agenda
    private EventsAgenda agendaSchedule() {
        EventsAgenda agendaSchedule = new EventsAgenda();
        for (SportEvent event : agenda.getEvents()) {
            Time time = event.getTime();
            Time timeCopy = new Time(time.getStartHour(), time.getStartMin(), time.getEndHour(), time.getEndMin());
            SportEvent eventCopy = new SportEvent(event.getName(), event.getType(), event.getDate(), timeCopy);
            eventCopy.rateEvent(event.getRating());
            agendaSchedule.addEvent(eventCopy);
        }
        return agendaSchedule;
    }

    // MODIFIES: this
    // EFFECTS: remove an event from the agenda
    private void removeEvent() {
        int eventSelect = askEvent("remove");
        if (eventSelect == 0) {
            return;
        }
        agenda.removeEvent(eventSelect - 1);
    }

    // EFFECTS: saves the workroom to file
    private void saveAgenda() {
        try {
            jsonWriter.open();
            jsonWriter.write(agenda);
            jsonWriter.close();
            System.out.println("Saved your events agenda to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void loadAgenda() {
        try {
            agenda = jsonReader.read();
            System.out.println("Loaded your agenda from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: asking for user to select user and return false if the inputted event
    // is not in the list.
    private int askEvent(String activity) {
        printEvents(agenda.getEvents(), false);
        System.out.print("Select an event to " + activity +  ": ");
        int eventSelect = input.nextInt();
        if (agenda.getEvents().size() < eventSelect || eventSelect < 1) {
            System.out.println("No event exists!");
            return 0;
        }
        return eventSelect;
    }

    // EFFECTS: print a list of sport events
    private void printEvents(List<SportEvent> events, boolean detail) {
        int ind = 1;
        for (SportEvent event : events) {
            int day = event.getDate().getDay();
            int month = event.getDate().getMonth();
            int year = event.getDate().getYear();
            if (detail) {
                int startHour = event.getTime().getStartHour();
                int startMin = event.getTime().getStartMin();
                int endHour = event.getTime().getEndHour();
                int endMin = event.getTime().getEndMin();
                System.out.println(ind + ". " + event.getType() + ": " + event.getName() + ", Date: "
                        + printFormat(day) + "-" + printFormat(month) + "-" + year + ", Time: "
                        + printFormat(startHour) + ":" + printFormat(startMin) + " - "
                        + printFormat(endHour) + ":" + printFormat(endMin)
                        + ", Rating: " + event.printRating());
            } else {
                System.out.println(ind + ". " + event.getType() + ": " + event.getName() + ", Date: "
                        + printFormat(day) + "-" + printFormat(month) + "-" + year);
            }
            ind++;
        }
    }

    // EFFECTS: return the string "0x" if the number is in 1 digit of x,
    // and the string "xy" if the number is 2 digits of xy.
    private String printFormat(int number) {
        if (number / 10 == 0) {
            return "0" + number;
        } else {
            return Integer.toString(number);
        }
    }

    // EFFECTS: check whether the inputted date is valid.
    private boolean matchDate(String dateInput) {
        return Pattern.matches("(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/(19|20)[0-9][0-9]", dateInput);
    }

    // EFFECTS: check whether the inputted time is valid.
    private boolean matchTime(String timeInput) {
        return Pattern.matches("(0[1-9]|1[0-9]|2[0-4]):([0-5][0-9])-(0[1-9]|1[0-9]|2[0-4]):([0-5][0-9])",
                timeInput);
    }

    // EFFECTS: check whether the inputted duration is valid in format of hh:mm.
    private boolean matchDuration(String durationInput) {
        return Pattern.matches("([01][0-9]|2[0-4]):([0-5][0-9])", durationInput);
    }
}
