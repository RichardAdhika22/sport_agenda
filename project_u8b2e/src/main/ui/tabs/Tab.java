package ui.tabs;

import model.EventDate;
import model.EventsAgenda;
import model.SportEvent;
import ui.AgendaUI;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

// An abstract class that captures all similarities between different tabs
public abstract class Tab extends JPanel {
    private static final int ROW_HEIGHT = 60;
    private static final int LEFT_PADDING = 70;
    private static final int BOTTOM_PADDING = 5;

    private final AgendaUI agendaUI;
    protected EventsAgenda agenda;

    // EFFECTS: construct a tab
    public Tab(AgendaUI agendaUI) {
        this.agendaUI = agendaUI;
    }

    //EFFECTS: returns the SmartHomeUI controller for this tab
    public AgendaUI getAgendaUI() {
        return agendaUI;
    }

    // EFFECTS: initialize row for many tab elements
    protected JPanel initRow() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setPreferredSize(new Dimension(AgendaUI.WIDTH, ROW_HEIGHT));
        row.setBorder(BorderFactory.createEmptyBorder(0, LEFT_PADDING, BOTTOM_PADDING, 50));
        return row;
    }

    // EFFECTS: construct a label containing the detail information of an event
    protected JLabel labelInit(SportEvent event, int ind) {
        int day = event.getDate().getDay();
        int month = event.getDate().getMonth();
        int year = event.getDate().getYear();
        int startHour = event.getTime().getStartHour();
        int startMin = event.getTime().getStartMin();
        int endHour = event.getTime().getEndHour();
        int endMin = event.getTime().getEndMin();
        return new JLabel(ind + ". " + event.getType() + ": " + event.getName() + ", Date: "
                + printFormat(day) + "-" + printFormat(month) + "-" + year + ", Time: "
                + printFormat(startHour) + ":" + printFormat(startMin) + " - "
                + printFormat(endHour) + ":" + printFormat(endMin)
                + ", Rating: ");
    }

    // EFFECTS: construct a new progress bar with demonstrating the given value out of 10
    protected JProgressBar barInit(int value) {
        JProgressBar bar = new JProgressBar();
        bar.setValue(value);
        bar.setMaximum(10);
        bar.setMinimum(0);
        bar.setFont(new Font("MV Boli", Font.BOLD, 10));
        bar.setForeground(Color.red);
        bar.setStringPainted(true);
        bar.setString(value + "/10");

        return bar;
    }

    // EFFECTS: print a proper time or date format
    //          adding a leading 0 if the given number is one digit
    //          otherwise return the same number back as a string
    protected String printFormat(int number) {
        if (number / 10 == 0) {
            return "0" + number;
        } else {
            return Integer.toString(number);
        }
    }

    // EFFECTS: create a text box
    protected JTextField textBox() {
        return new JTextField(10);
    }

    // EFFECTS: return true if the given string match the date format
    protected boolean matchDate(String dateInput) {
        return Pattern.matches("(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/(19|20)[0-9][0-9]", dateInput);
    }

    // EFFECTS: convert the string date to the date object
    protected EventDate convertDate(String dateInput) {
        int day = Integer.parseInt(dateInput.substring(0,2));
        int month = Integer.parseInt(dateInput.substring(3,5));
        int year = Integer.parseInt(dateInput.substring(6,10));

        return new EventDate(day, month, year);
    }
}
