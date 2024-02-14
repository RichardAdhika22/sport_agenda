package ui.tabs;

import model.EventDate;
import model.SportEvent;
import model.Time;
import ui.AgendaUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

// A tab to add an event
public class AddEventsTab extends Tab implements ActionListener {

    private static final String TITLE = "Add New Sport Events!";

    private JComboBox categoryBox;
    private JLabel result;
    private JSlider ratingSlider;

    private JTextField nameText;
    private JTextField dateText;
    private JTextField startTimeText;
    private JTextField endTimeText;

    private EventDate eventDate;
    private Time eventTime;

    // EFFECTS: initialize the panel to add an event
    public AddEventsTab(AgendaUI agendaUI) {
        super(agendaUI);

        JLabel title = new JLabel(TITLE);
        title.setFont(new Font("Arial", Font.PLAIN, 25));
        add(title);

        this.add(eventNameField());
        this.add(eventCategoryField());
        this.add(dateField());
        this.add(timeField());
        this.add(ratingField());
        this.add(buttonField());
        this.add(resultField());
    }

    // EFFECTS: create a field asking for event's name input
    //          with a label and textbox to fill out
    private JPanel eventNameField() {
        JPanel nameRow = initRow();
        JLabel name = new JLabel("Event name: ");

        nameText = textBox();
        nameRow.add(name);
        nameRow.add(nameText);

        return nameRow;
    }

    // EFFECTS: create a field asking for sport's category
    //          with a label and several types to choose from
    private JPanel eventCategoryField() {
        JPanel categoryRow = initRow();
        JLabel name = new JLabel("Event category: ");

        String[] categories = {"Soccer", "Swimming", "Badminton", "Basketball", "Volleyball"};
        categoryBox = new JComboBox(categories);
        categoryRow.add(name);
        categoryRow.add(categoryBox);

        return categoryRow;
    }

    // EFFECTS: create a field asking for the event's date
    //          with a label and a text box to fill out the date
    private JPanel dateField() {
        JPanel dateRow = initRow();
        JLabel name = new JLabel("Event date (dd/mm/yy): ");

        dateText = textBox();
        dateRow.add(name);
        dateRow.add(dateText);

        return dateRow;
    }

    // EFFECTS: create a field asking for the event's time
    //          with a label and two text boxes for the starting time and ending time
    private JPanel timeField() {
        JPanel timeRow = initRow();

        JLabel name = new JLabel("Event time (hh:mm - hh:mm): ");
        JLabel separator = new JLabel(" - ");

        timeRow.add(name);
        startTimeText = textBox();
        endTimeText = textBox();
        timeRow.add(startTimeText);
        timeRow.add(separator);
        timeRow.add(endTimeText);

        return timeRow;
    }

    //  EFFECTS: create a field asking for the event's rating
    //           with a scale from 1 to 10 represented in a slider
    private JPanel ratingField() {
        JPanel ratingRow = initRow();
        JLabel name = new JLabel("Select rating: ");

        ratingRow.add(name);
        ratingSlider = ratingSlider();
        ratingRow.add(ratingSlider);

        return ratingRow;
    }

    // EFFECTS: create a button that add an event to the agenda list when clicked
    private JButton buttonField() {
        JButton addButton = new JButton("Add Event");
        addButton.setBounds(10,80,80,25);
        addButton.addActionListener(this);

        return addButton;
    }

    // MODIFIES: this
    // EFFECTS: create a field to display whether the adding action is successful or not
    private JPanel resultField() {
        JPanel row = initRow();
        result = new JLabel("");

        row.add(result);

        return row;
    }

    // EFFECTS: create a slider from scale 1 to 10 with 1 increment
    private JSlider ratingSlider() {
        JSlider slider = new JSlider(1, 10);
        slider.setMajorTickSpacing(1);
        slider.setPaintLabels(true);
        slider.setPaintTicks(true);

        return slider;
    }

    // MODIFIES: this
    // EFFECTS: add an event with the provided information to the agenda list
    //          and check whether the information given is valid or not
    @Override
    public void actionPerformed(ActionEvent e) {
        String name = nameText.getText();
        String category = (String) categoryBox.getSelectedItem();
        String date = dateText.getText();
        String startTime = startTimeText.getText();
        String endTime = endTimeText.getText();

        if (!processName(name) || !processDate(date) || !processTime(startTime, endTime)) {
            return;
        }

        SportEvent event = new SportEvent(name, category, eventDate, eventTime);
        event.rateEvent(ratingSlider.getValue());
        getAgendaUI().getAgenda().addEvent(event);
        result.setText("Successfully added " + event.getName() + " to your agenda!");
    }

    // MODIFIES: this
    // EFFECTS: check whether the sport event's name is empty
    //          if the input is empty, give the message by setting the input box to red
    //          and give the error message
    private boolean processName(String nameInput) {
        if (nameInput.length() == 0) {
            result.setText("Invalid name! Please input the event's name");
            nameText.setBackground(Color.RED);
            return false;
        }
        nameText.setBackground(Color.WHITE);
        return true;
    }

    // MODIFIES: this
    // EFFECTS: check whether the given date matches the proper format
    //          if the input doesn't match the format, give the message by setting the input box to red
    //          and give the error message
    private boolean processDate(String dateInput) {
        if (dateInput.length() == 0) {
            result.setText("Invalid Date! Please input the date.");
            dateText.setBackground(Color.red);
            return false;
        }

        if (!matchDate(dateInput)) {
            result.setText("Invalid Date! Please format your date properly.");
            dateText.setBackground(Color.red);
            return false;
        }
        eventDate = convertDate(dateInput);

        if (eventDate.getDay() > eventDate.dayPerMonth(eventDate.getMonth())) {
            result.setText("Invalid Date! The date does not match the month");
            dateText.setBackground(Color.red);
            return false;
        }
        dateText.setBackground(Color.WHITE);
        return true;
    }

    // MODIFIES: this
    // EFFECTS: check whether the given time matches the proper format
    //          if the input doesn't match the format, give the message by setting the input box to red
    //          and give the error message
    private boolean processTime(String startTime, String endTime) {
        if (!matchTime(startTime)) {
            result.setText("Invalid starting time! Please format your starting time properly.");
            startTimeText.setBackground(Color.red);
            return false;
        }
        startTimeText.setBackground(Color.WHITE);

        if (!matchTime(endTime)) {
            result.setText("Invalid ending time! Please format your ending time properly.");
            endTimeText.setBackground(Color.red);
            return false;
        }
        endTimeText.setBackground(Color.WHITE);

        eventTime = convertTime(startTime, endTime);

        if (eventTime.getEndHour() < eventTime.getStartHour() || ((eventTime.getEndHour() == eventTime.getStartHour())
                && (eventTime.getEndMin() <= eventTime.getStartMin()))) {
            result.setText("Invalid time! Starting time should be earlier than ending time");
            endTimeText.setBackground(Color.red);
            startTimeText.setBackground(Color.red);
            return false;
        }
        startTimeText.setBackground(Color.WHITE);
        endTimeText.setBackground(Color.WHITE);
        return true;
    }

    // EFFECTS: return true if the given string match the time format
    private boolean matchTime(String timeInput) {
        return Pattern.matches("(0[1-9]|1[0-9]|2[0-4]):([0-5][0-9])", timeInput);
    }

    // EFFECTS: convert the string time to the time object
    private Time convertTime(String startTime, String endTime) {
        int startHour = Integer.parseInt(startTime.substring(0,2));
        int startMin = Integer.parseInt(startTime.substring(3,5));
        int endHour = Integer.parseInt(endTime.substring(0,2));
        int endMin = Integer.parseInt(endTime.substring(3,5));

        return new Time(startHour, startMin, endHour, endMin);
    }
}
