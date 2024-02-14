package ui;

import model.Event;
import model.EventLog;
import model.EventsAgenda;
import persistence.JsonWriter;
import ui.tabs.*;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;

// Sport Events Agenda GUI
public class AgendaUI extends JFrame {

    public static final int HOME_TAB_INDEX = 0;
    public static final int ADD_EVENTS_TAB_INDEX = 1;
    public static final int VIEW_EVENTS_TAB_INDEX = 2;
    public static final int EDIT_EVENTS_TAB_INDEX = 3;
    public static final int SCHEDULE_EVENTS_TAB_INDEX = 4;
    public static final int LOG_EVENT_TAB_INDEX = 5;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 700;

    private final JsonWriter jsonWriter;
    private static final String JSON_STORE = "./data/events.json";
    private final JTabbedPane sidebar;
    private EventsAgenda agenda;

    // EFFECTS: instantiate a new frame for sport events agenda
    public AgendaUI() {
        super("Sport Events Agenda");

        initialize();
        agenda = new EventsAgenda();

        jsonWriter = new JsonWriter(JSON_STORE);

        sidebar = new JTabbedPane();
        sidebar.setTabPlacement(JTabbedPane.LEFT);
        loadTabs();
        add(sidebar);

        printWhenExit();
        setVisible(true);
    }

    // EFFECTS: set up essential elements for the frame
    private void initialize() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
    }

    // EFFECTS: add all tabs to the frame
    private void loadTabs() {
        JPanel homeTab = new HomeTab(this);
        JPanel addEventsTab = new AddEventsTab(this);
        JPanel viewEventsTab = new ViewEventsTab(this);
        JPanel editEventsTab = new EditEventsTab(this);
        JPanel scheduleEventsTab = new ScheduleEventsTab(this);
        JPanel logEventsTab = new LogEventsTab(this);

        sidebar.add(homeTab, HOME_TAB_INDEX);
        sidebar.setTitleAt(HOME_TAB_INDEX, "Home");

        sidebar.add(addEventsTab, ADD_EVENTS_TAB_INDEX);
        sidebar.setTitleAt(ADD_EVENTS_TAB_INDEX, "Add Events");

        sidebar.add(viewEventsTab, VIEW_EVENTS_TAB_INDEX);
        sidebar.setTitleAt(VIEW_EVENTS_TAB_INDEX, "View Events");

        sidebar.add(editEventsTab, EDIT_EVENTS_TAB_INDEX);
        sidebar.setTitleAt(EDIT_EVENTS_TAB_INDEX, "Edit Events");

        sidebar.add(scheduleEventsTab, SCHEDULE_EVENTS_TAB_INDEX);
        sidebar.setTitleAt(SCHEDULE_EVENTS_TAB_INDEX, "Schedule Events");

        sidebar.add(logEventsTab, LOG_EVENT_TAB_INDEX);
        sidebar.setTitleAt(LOG_EVENT_TAB_INDEX, "Log Events");
    }

    public EventsAgenda getAgenda() {
        return agenda;
    }

    // MODIFIES: this
    // EFFECTS: set new agenda
    public void setAgenda(EventsAgenda agenda) {
        this.agenda = agenda;
    }

    // EFFECTS: when the window (application) is closed, all actions that have been done will be printed out to
    //          the console
    private void printWhenExit() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                String[] objButtons = {"Exit and Save", "Exit", "No"};
                int promptResult = JOptionPane.showOptionDialog(null,
                        "Are you sure you want to exit?", "Sport Events Agenda",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                        objButtons, objButtons[1]);
                if (promptResult == 0 || promptResult == 1) {
                    if (promptResult == 0) {
                        saveFile();
                    }
                    for (Event evt : EventLog.getInstance()) {
                        System.out.println(evt.toString() + "\n");
                    }
                    System.exit(0);
                }
            }
        });
    }

    // EFFECTS: save the current state of the application to the JSON file.
    private void saveFile() {
        try {
            jsonWriter.open();
            jsonWriter.write(agenda);
            jsonWriter.close();
        } catch (FileNotFoundException error) {
            JOptionPane.showOptionDialog(null, "No such file exist!",
                    "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, null, null);
        }
    }
}
