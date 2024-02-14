package ui.tabs;

import model.SportEvent;
import ui.AgendaUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

// A tab that allows user to remove or rate multiple events
public class EditEventsTab extends Tab {
    private static final String TITLE = "Remove/Rate Sport Events";

    private final JPanel eventsPanel;
    private final List<JCheckBox> boxes;
    private final JLabel result;
    private JSlider slider;

    private JButton removeButton;
    private JButton rateButton;

    // EFFECTS: instantiate the tab
    public EditEventsTab(AgendaUI agendaUI) {
        super(agendaUI);

        JLabel title = new JLabel(TITLE);
        title.setFont(new Font("Arial", Font.PLAIN, 25));
        add(title);

        boxes = new ArrayList<>();

        eventsPanel = new JPanel();
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));

        result = new JLabel("");

        add(refreshButton());
        add(eventsPanel);
        add(actionButtons());
        add(result);
    }

    // MODIFIES: this
    // EFFECTS: create a refresh button and add the functionality that when pressed,
    //          display all events based on the current agenda
    private JPanel refreshButton() {
        JPanel refreshRow = initRow();
        JButton refreshButton = new JButton("Refresh");
        JLabel status = new JLabel("");

        refreshButton.addActionListener(e -> {
            status.setText("");
            status.setText("Refreshed!");
            eventsPanel.removeAll();
            boxes.removeAll(boxes);
            addEventBoxes();
        });

        refreshRow.add(refreshButton);
        refreshRow.add(status);

        return refreshRow;
    }

    // EFFECTS: create a field containing remove and rate buttons
    private JPanel actionButtons() {
        JPanel actionRow = initRow();
        removeButton = new JButton("Remove");
        rateButton = new JButton("Rate");

        addRemoveAction();
        addRateAction();

        JLabel instruction = new JLabel("Rating: ");

        slider = new JSlider(1, 10);
        slider.setMajorTickSpacing(1);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);

        actionRow.add(removeButton);
        actionRow.add(rateButton);
        actionRow.add(instruction);
        actionRow.add(slider);

        return actionRow;
    }

    // MODIFIES: this
    // EFFECTS: print out all events in the agenda in the checkbox format
    private void addEventBoxes() {
        List<SportEvent> events = getAgendaUI().getAgenda().getEvents();

        for (SportEvent event : events) {
            int day = event.getDate().getDay();
            int month = event.getDate().getMonth();
            int year = event.getDate().getYear();
            int startHour = event.getTime().getStartHour();
            int startMin = event.getTime().getStartMin();
            int endHour = event.getTime().getEndHour();
            int endMin = event.getTime().getEndMin();
            JCheckBox box = new JCheckBox(event.getType() + ": " + event.getName() + ", Date: "
                    + printFormat(day) + "-" + printFormat(month) + "-" + year + ", Time: "
                    + printFormat(startHour) + ":" + printFormat(startMin) + " - "
                    + printFormat(endHour) + ":" + printFormat(endMin)
                    + ", Rating: " + event.printRating());
            boxes.add(box);
            eventsPanel.add(box);
        }
    }

    // MODIFIES: this
    // EFFECTS: add the remove functionality to the button, that is remove all events that is being checked
    //          when the button is pressed
    private void addRemoveAction() {
        removeButton.addActionListener(e -> {
            if (!boxes.isEmpty()) {
                int ind = 0;
                int removed = 0;

                for (JCheckBox box : boxes) {
                    if (box.isSelected()) {
                        getAgendaUI().getAgenda().removeEvent(ind);
                        removed++;
                    }
                    ind++;
                }

                if (removed == 0) {
                    result.setText("No events selected!");
                } else {
                    result.setText("Removed " + removed + " sport events.");
                }

                eventsPanel.removeAll();
                boxes.removeAll(boxes);
                addEventBoxes();
            } else {
                result.setText("No existing sport events!");
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: add the rate functionality to the button, that is rate all checked events based on the
    //          rating given by the slider
    private void addRateAction() {
        rateButton.addActionListener(e -> {
            if (!boxes.isEmpty()) {
                int ind = 0;
                int selected = 0;

                for (JCheckBox box : boxes) {
                    if (box.isSelected()) {
                        getAgendaUI().getAgenda().getEvents().get(ind).rateEvent(slider.getValue());
                        selected++;
                    }
                    ind++;
                }

                if (selected == 0) {
                    result.setText("No events selected!");
                } else {
                    result.setText("Rated " + selected + " sport events with " + slider.getValue() + ".");
                }

                eventsPanel.removeAll();
                boxes.removeAll(boxes);
                addEventBoxes();
            } else {
                result.setText("No existing sport events!");
            }
        });
    }
}
