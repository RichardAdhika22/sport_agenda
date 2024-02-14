package ui.tabs;

import model.EventDate;
import model.EventsAgenda;
import model.SportEvent;
import model.Time;
import ui.AgendaUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.regex.Pattern;

// A tab that allows user to schedule optimal events on a particular day based on the given duration
public class ScheduleEventsTab extends Tab {
    private static final String TITLE = "Schedule Optimal Events";

    private final JPanel scheduledEvents;
    private JTextField dateInputText;
    private JTextField durationInputText;
    private JButton confirmButton;
    private JLabel result;

    private EventDate eventDate;

    // EFFECTS: instantiate the tab
    public ScheduleEventsTab(AgendaUI agendaUI) {
        super(agendaUI);

        JLabel title = new JLabel(TITLE);
        title.setFont(new Font("Arial", Font.PLAIN, 25));
        add(title);

        JPanel eventsPanel = new JPanel();
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));

        scheduledEvents = new JPanel(new FlowLayout(FlowLayout.LEFT));

        add(schedulingExplanation());
        add(selectDate());
        add(selectDuration());
        add(confirmButton());
        add(scheduledEvents);
    }

    // EFFECTS: give a brief description on how does the scheduling works
    private JPanel schedulingExplanation() {
        JPanel explanationField = initRow();
        JLabel line1 = new JLabel("Event scheduling is based on the highest rating on the given day based on the "
                + "given duration.");
        JLabel line2 = new JLabel("if there are two events with the same rating, "
                + "the one added first will be prioritized");

        explanationField.add(line1);
        explanationField.add(line2);
        return explanationField;
    }

    // EFFECTS: construct a panel to input a date with its description
    private JPanel selectDate() {
        JPanel selectDateField = initRow();
        JLabel inputPrompt = new JLabel("Enter a date to schedule (dd/mm/yyyy): ");
        dateInputText = textBox();

        selectDateField.add(inputPrompt);
        selectDateField.add(dateInputText);

        return selectDateField;
    }

    // EFFECTS: construct a panel to input a duration with its description
    private JPanel selectDuration() {
        JPanel selectDurationField = initRow();
        JLabel inputPrompt = new JLabel("Enter duration available (hh:mm): ");
        durationInputText = textBox();

        selectDurationField.add(inputPrompt);
        selectDurationField.add(durationInputText);

        return selectDurationField;
    }

    // MODIFIES: this
    // EFFECTS: construct a button to do the scheduling action when clicked
    private JPanel confirmButton() {
        JPanel confirmButtonField = initRow();
        confirmButton = new JButton("Ok");
        result = new JLabel("");

        addConfirmAction();

        confirmButtonField.add(confirmButton);
        confirmButtonField.add(result);

        return confirmButtonField;
    }

    // MODIFIES: this
    // EFFECTS: add an action to the confirm button,
    //          the action is display all the scheduled events based on the given date and duration
    private void addConfirmAction() {
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result.setText("Displaying");
                result.setText("");
                scheduledEvents.removeAll();

                String dateInput = dateInputText.getText();
                String durationInput = durationInputText.getText();

                if (!processDate(dateInput) || !processDuration(durationInput)) {
                    return;
                }
                result.setText("");

                int hour = Integer.parseInt(durationInput.substring(0,2));
                int min = Integer.parseInt(durationInput.substring(3,5));
                EventsAgenda agendaSchedule = agendaSchedule();

                List<SportEvent> scheduledResult = agendaSchedule.schedule(eventDate, hour, min);
                if (scheduledResult.isEmpty()) {
                    result.setText("No matching events!");
                }

                printEvents(scheduledResult);
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: check whether the given date matches the proper format
    //          if the input doesn't match the format, give the message by setting the input box to red
    //          and give the error message
    private boolean processDate(String dateInput) {
        if (dateInput.length() == 0) {
            result.setText("Invalid Date! Please input the date.");
            dateInputText.setBackground(Color.red);
            return false;
        }

        if (!matchDate(dateInput)) {
            result.setText("Invalid Date! Please format your date properly.");
            dateInputText.setBackground(Color.red);
            return false;
        }
        eventDate = convertDate(dateInput);

        if (eventDate.getDay() > eventDate.dayPerMonth(eventDate.getMonth())) {
            result.setText("Invalid Date! The date does not match the month");
            dateInputText.setBackground(Color.red);
            return false;
        }
        dateInputText.setBackground(Color.WHITE);
        return true;
    }

    // MODIFIES: this
    // EFFECTS: return false if the input of the duration is empty or does not match the proper format
    private boolean processDuration(String durationInput) {
        if (durationInput.length() == 0) {
            result.setText("Invalid duration! Please input the duration.");
            durationInputText.setBackground(Color.red);
            return false;
        }

        if (!matchDuration(durationInput)) {
            result.setText("Invalid duration format!");
            durationInputText.setBackground(Color.red);
            return false;
        }
        durationInputText.setBackground(Color.white);
        return true;
    }

    // EFFECTS: check whether the inputted duration is valid in format of hh:mm.
    private boolean matchDuration(String durationInput) {
        return Pattern.matches("([01][0-9]|2[0-4]):([0-5][0-9])", durationInput);
    }

    // EFFECTS: create a new copy of agenda
    private EventsAgenda agendaSchedule() {
        EventsAgenda agendaSchedule = new EventsAgenda();
        for (SportEvent event : getAgendaUI().getAgenda().getEvents()) {
            Time time = event.getTime();
            Time timeCopy = new Time(time.getStartHour(), time.getStartMin(), time.getEndHour(), time.getEndMin());
            SportEvent eventCopy = new SportEvent(event.getName(), event.getType(), event.getDate(), timeCopy);
            eventCopy.rateEventCopy(event.getRating());
            agendaSchedule.addEventCopy(eventCopy);
        }
        return agendaSchedule;
    }

    // EFFECTS: print out the given list of events on the console
    private void printEvents(List<SportEvent> events) {
        int ind = 1;
        for (SportEvent event : events) {
            JPanel eventPanel = new JPanel();

            eventPanel.add(labelInit(event, ind));
            eventPanel.add(barInit(event.getRating()));
            scheduledEvents.add(eventPanel);

            ind++;
        }
    }
}