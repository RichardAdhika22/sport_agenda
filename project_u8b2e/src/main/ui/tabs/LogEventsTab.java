package ui.tabs;

import model.EventLog;
import model.Event;
import ui.AgendaUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// A tab to display all actions that have been done since the application runs
public class LogEventsTab extends Tab {
    private static final String TITLE = "ACTION HISTORY";

    private final JTextArea events;

    // EFFECTS: construct a home tab for console
    public LogEventsTab(AgendaUI agendaUI) {
        super(agendaUI);

        JLabel title = new JLabel(TITLE);
        title.setFont(new Font("Arial", Font.PLAIN, 25));
        add(title);

        addPrintLogAction();

        events = new JTextArea();
        events.setPreferredSize(new Dimension(500,400));
        add(events);
    }

    // EFFECTS: construct a button to print actions history when pressed
    public void addPrintLogAction() {
        JPanel printLogField = initRow();
        JButton printLog = new JButton("Print Log");

        printLog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Event evt : EventLog.getInstance()) {
                    events.setText(events.getText() + evt.toString() + "\n\n");
                }
            }
        });

        printLogField.add(printLog);
        add(printLogField);
    }
}
