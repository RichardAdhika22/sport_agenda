package ui.tabs;

import model.SportEvent;
import ui.AgendaUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

// A tab that shows pictures of several sports and reminds some closest events
public class HomeTab extends Tab {
    private static final String TITLE = "HOME PAGE";

    private static final int LEFT_PADDING = 70;
    private static final int BOTTOM_PADDING = 5;
    private static final int NUM_IMAGES = 5;

    private JPanel suggestedEvents;
    private JLabel imageLabel;
    private final JLabel suggestionResult;
    private ArrayList<ImageIcon> images;
    private int imageIndex = 0;

    private JButton leftButton;
    private JButton rightButton;

    // EFFECTS: constructs a home tab for console
    public HomeTab(AgendaUI agendaUI) {
        super(agendaUI);

        JLabel title = new JLabel(TITLE);
        title.setFont(new Font("Arial", Font.PLAIN, 25));
        add(title);


        suggestionResult = new JLabel("No upcoming events!");

        loadImages();
        add(imagesField());
        add(refreshButton());
        add(suggestionField());
    }

    // MODIFIES: this
    // EFFECTS: import images and store them in a list
    private void loadImages() {
        String sep = System.getProperty("file.separator");
        images = new ArrayList<>();

        for (int ind = 0; ind < NUM_IMAGES; ind++) {
            ImageIcon imageIcon = new ImageIcon(System.getProperty("user.dir") + sep
                    + "images" + sep + "image" + ind + ".jpg");
            Image image = imageIcon.getImage().getScaledInstance(300,200, Image.SCALE_DEFAULT);
            imageIcon = new ImageIcon(image);
            images.add(imageIcon);
        }
    }

    // EFFECTS: construct a panel where an image is displayed and can be switched to other images
    //          by pressing the buttons.
    private JPanel imagesField() {
        JPanel imagesPanel = new JPanel();
        imagesPanel.setPreferredSize(new Dimension(650,200));
        imagesPanel.setBorder(BorderFactory.createEmptyBorder(0, LEFT_PADDING, BOTTOM_PADDING, 50));

        leftButton = new JButton("<");
        imageLabel = new JLabel(images.get(imageIndex));
        rightButton = new JButton(">");

        imageLabel.setBorder(BorderFactory.createLineBorder(Color.black));

        imagesPanel.add(leftButton);
        imagesPanel.add(imageLabel);
        imagesPanel.add(rightButton);

        addLeftAction();
        addRightAction();

        return imagesPanel;
    }

    // EFFECTS: construct a refresh button and the label to indicate a successful refresh
    private JPanel refreshButton() {
        JPanel refreshRow = initRow();
        JButton refreshButton = new JButton("Refresh");
        JLabel status = new JLabel("");

        refreshButton.addActionListener(e -> {
            status.setText("");
            status.setText("Refreshed!");
            suggestedEvents.removeAll();
            fillSuggestedPanel();
        });

        refreshRow.add(refreshButton);
        refreshRow.add(status);

        return refreshRow;
    }

    // EFFECTS: provide up to 3 suggestions the closest events from the current date
    //          and then display them in a list
    private JPanel suggestionField() {
        JPanel suggestionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel description = new JLabel("Upcoming (closest 3) events: ");
        suggestedEvents = new JPanel(new FlowLayout(FlowLayout.LEFT));

        suggestionPanel.setBorder(BorderFactory.createEmptyBorder(0, LEFT_PADDING, BOTTOM_PADDING, 0));
        suggestionPanel.setPreferredSize(new Dimension(AgendaUI.WIDTH, 350));
        suggestedEvents.setPreferredSize(new Dimension(AgendaUI.WIDTH, 300));

        suggestionPanel.add(description);
        suggestionPanel.add(suggestionResult);
        suggestionPanel.add(suggestedEvents);

        return suggestionPanel;
    }

    // MODIFIES: this
    // EFFECTS: print out a list of the filtered events (up to 3 closest events from the current date),
    //          does not print out anything if the filtered events is empty
    private void fillSuggestedPanel() {
        List<SportEvent> filtered = getAgendaUI().getAgenda().suggestedEventsFilter();
        if (!filtered.isEmpty()) {
            suggestionResult.setText("");
        }
        printEvents(filtered);
    }

    // MODIFIES: this
    // EFFECTS: showing previous image in the list if the button is pressed,
    //          showing the last image on the list if the button is pressed on the first image
    private void addLeftAction() {
        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imageIndex--;
                if (imageIndex == -1) {
                    imageIndex = NUM_IMAGES - 1;
                }
                imageLabel.setIcon(images.get(imageIndex));
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: showing next image in the list if the button is pressed,
    //          showing the first image in the list if the button is pressed on the last image
    private void addRightAction() {
        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imageIndex++;
                if (imageIndex == NUM_IMAGES) {
                    imageIndex = 0;
                }
                imageLabel.setIcon(images.get(imageIndex));
            }
        });
    }

    // EFFECTS: print out the given list of events on the console
    private void printEvents(List<SportEvent> events) {
        int ind = 1;
        for (SportEvent event : events) {
            JPanel eventPanel = new JPanel();

            eventPanel.add(labelInit(event, ind));
            eventPanel.add(barInit(event.getRating()));
            suggestedEvents.add(eventPanel);

            ind++;
        }
    }
}
