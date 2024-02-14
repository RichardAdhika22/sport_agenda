package ui.tabs;

import model.SportEvent;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.AgendaUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

// A tab to view, load, and save events
public class ViewEventsTab extends Tab {

    private static final String TITLE = "View, Save, and Load Your Sport Agenda";

    private String viewCategory;
    private JTextField viewText;
    private JPanel viewPanel;
    private final JPanel eventsField;

    private JLabel viewError;
    private final JLabel dataResult;
    private JLabel viewInput;

    private static final String JSON_STORE = "./data/events.json";
    private final JsonReader jsonReader;
    private final JsonWriter jsonWriter;

    // EFFECTS: construct a new tab to view events
    public ViewEventsTab(AgendaUI agendaUI) {
        super(agendaUI);

        JLabel title = new JLabel(TITLE);
        title.setFont(new Font("Arial", Font.PLAIN, 25));
        add(title);

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        dataResult = new JLabel("");

        eventsField = new JPanel();
        eventsField.setLayout(new BoxLayout(eventsField, BoxLayout.Y_AXIS));

        add(loadSaveButtonsField());
        add(filterField());
        add(eventsField);
    }

    // EFFECTS: create a field to containing load and save buttons
    private JPanel loadSaveButtonsField() {
        JButton loadButton = new JButton("Load events");
        JButton saveButton = new JButton("Save events");
        JPanel buttonsRow = initRow();

        buttonsRow.add(loadButton);
        buttonsRow.add(saveButton);
        buttonsRow.add(dataResult);
        buttonsRow.setSize(WIDTH, HEIGHT / 6);

        applyActionLoad(loadButton);
        applyActionSave(saveButton);

        return buttonsRow;
    }

    // EFFECTS: display the events based on the given option and ask for additional input
    //          if user choose to view by month or category
    private void displayEvents(String choice) {
        eventsField.removeAll();

        if (choice != null) {
            if (choice.equals("All")) {
                viewPanel.setVisible(false);
                getAgendaUI().getAgenda().viewAll();
                printEvents(getAgendaUI().getAgenda().getEvents());
            } else if (choice.equals("By Month")) {
                viewInput.setText("Month/year (mm/yyyy): ");
                viewPanel.setVisible(true);
            } else {
                viewInput.setText("Category: ");
                viewPanel.setVisible(true);
            }
        }
    }

    // EFFECTS: add the import data from Json file functionality to the load button
    private void applyActionLoad(JButton button) {
        button.addActionListener(e -> {
            try {
                getAgendaUI().getAgenda().loaded();
                getAgendaUI().setAgenda(jsonReader.read());
                dataResult.setText("Loaded your agenda from " + JSON_STORE);
            } catch (IOException error) {
                dataResult.setText("Unable to read from file: " + JSON_STORE);
            }
        });
    }

    // EFFECTS: add the save date to a Json file functionality to the save button
    private void applyActionSave(JButton button) {
        button.addActionListener(e -> {
            try {
                jsonWriter.open();
                jsonWriter.write(getAgendaUI().getAgenda());
                jsonWriter.close();
                dataResult.setText("Saved your events agenda to " + JSON_STORE);
            } catch (FileNotFoundException error) {
                dataResult.setText("Unable to write to file: " + JSON_STORE);
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: print events by the selected month if there exist any in the agenda,
    //          give a warning message and set the input box to red if the input is not appropriate
    private void displayByMonth() {
        String monthInput = viewText.getText();

        if (monthInput.length() != 0) {
            if (checkMonthInput(monthInput)) {
                int month = Integer.parseInt(monthInput.substring(0,2));
                int year = Integer.parseInt(monthInput.substring(3,7));

                List<SportEvent> result = getAgendaUI().getAgenda().eventsByMonth(month, year);
                if (result.isEmpty()) {
                    viewError.setText("No existing events!");
                }
                printEvents(result);
            } else {
                viewText.setBackground(Color.red);
                viewError.setText("Invalid month!");
            }
        } else {
            viewText.setBackground(Color.red);
            viewError.setText("Please input the month!");
        }
    }

    // EFFECTS: return true if the string input match the month-year format
    private boolean checkMonthInput(String month) {
        return Pattern.matches("(1[0-2]|0[1-9])/(19|20)[0-9][0-9]", month);
    }

    // MODIFIES: this
    // EFFECTS: print out the events that match the given category to the console,
    //          give a warning message and set the input box to red if the input is empty
    private void displayByCategory() {
        String categoryInput = viewText.getText();
        categoryInput = categoryInput.toLowerCase();

        if (categoryInput.length() != 0) {
            List<SportEvent> result = getAgendaUI().getAgenda().eventsByType(categoryInput);
            if (result.isEmpty()) {
                viewError.setText("No existing category/events!");
            }
            printEvents(result);
        } else {
            viewText.setBackground(Color.red);
            viewError.setText("Please input the month!");
        }
    }

    // MODIFIES: this
    // EFFECTS: print events from the given list to the console
    protected void printEvents(List<SportEvent> events) {
        int ind = 1;
        for (SportEvent event : events) {
            JPanel eventPanel = new JPanel();

            eventPanel.add(labelInit(event, ind));
            eventPanel.add(barInit(event.getRating()));
            eventsField.add(eventPanel);

            ind++;
        }
    }

    // EFFECTS: create a field that ask the user to choose the method to view the events
    private JPanel filterField() {
        JPanel filter = new JPanel(new GridLayout(1,2));
        filter.setPreferredSize(new Dimension(700, 80));
        filter.setBorder(BorderFactory.createEmptyBorder(0,0,0,80));

        filter.add(filterComboBox());
        filter.add(comboSelectionPanel());

        return filter;
    }

    // EFFECTS: create a drop-down menu that give user the filter options on how to view the events
    //          as well as adding the functionality to the button
    private JPanel filterComboBox() {
        JPanel filterComboRow = new JPanel(new FlowLayout());

        String[] choices = {"All", "By Month", "By Category"};
        JComboBox filterCombo = new JComboBox(choices);
        filterComboRow.add(filterCombo);

        filterCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewError.setText("Displaying...");
                viewError.setText("");
                viewCategory = (String) filterCombo.getSelectedItem();
                displayEvents(viewCategory);
            }
        });

        return filterComboRow;
    }

    // MODIFIES: this
    // EFFECTS: create a field that ask for additional input if the user choose to view by month or category
    //          hide the menu if the user choose to view all events
    private JPanel comboSelectionPanel() {
        viewPanel = new JPanel(new FlowLayout());
        viewInput = new JLabel("");
        viewError = new JLabel("");
        viewText = new JTextField(10);
        JButton viewButton = new JButton("Ok");

        viewPanel.add(viewInput);
        viewPanel.add(viewText);
        viewPanel.add(viewButton);
        viewPanel.add(viewError);
        addActionViewButton(viewButton);

        viewPanel.setVisible(false);
        return viewPanel;
    }

    // EFFECTS: add functionality to the button that ask for additional input, that is
    //          print out the events based on the additional input
    private void addActionViewButton(JButton button) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eventsField.removeAll();
                viewText.setBackground(Color.white);
                viewError.setText("");

                if (viewCategory.equals("By Month")) {
                    displayByMonth();
                } else {
                    displayByCategory();
                }
            }
        });
    }
}
