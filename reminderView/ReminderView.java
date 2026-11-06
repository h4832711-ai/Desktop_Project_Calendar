
package reminderView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import date.Date;
import reminderAPI.IReminderModel;
import reminderAPI.IReminderView;

public class ReminderView extends JFrame implements IReminderView, Observer
{
    private static final long serialVersionUID = 2526677148315437465L;

    // Observer Pattern - Student 2
    // Stores the Model reference to refresh reminder information.
    private final IReminderModel model;

    // Panels
    private final JPanel northPanel;
    private final JPanel southPanel;
    private final JPanel eastPanel;
    private final JPanel westPanel;
    private final JPanel centerPanel;
    private final JPanel controllsPanel;
    private final JPanel dateSelectorPanel;
    private final JPanel fileNamePanel;

    // ComboBoxes
    private final JComboBox<Integer> dayComboBox;
    private final JComboBox<String> monthComboBox;
    private final JComboBox<Integer> yearComboBox;

    // Buttons & Controls
    private JButton selectTextButton;
    private JButton saveButton;
    private JButton importButton;
    private JButton clearTextButton;
    private JButton deleteButton;
    private JButton importantButton;
    private JButton exportButton;

    // FR-03: Search Reminders - Student 2
    // Controls for keyword-based reminder searching.
    private final JTextField searchField;
    private final JButton searchButton;

    // FR-04: View All Reminders - Student 2
    // Button for displaying all saved reminders.
    private final JButton viewAllButton;

    private final JTextArea reminderText;
    private final JTextField fileName;

    // Labels
    private final JLabel ApplicationHeaderLabel;
    private final JLabel yearLabel;
    private final JLabel monthLabel;
    private final JLabel dayLabel;
    private final JLabel fileNameLabel;
    private final JLabel statusBar;
    private final JLabel reminderCountLabel;

    // Icons
    private final Icon calendarIcon;
    private final Icon selectIcon;
    private final Icon saveIcon;
    private final Icon importIcon;
    private final Icon clearTextIcon;

    // Constructor
    public ReminderView(IReminderModel model)
    {
        super("Callendar Reminder Application");

        // Observer Pattern - Student 2
        // Keep the Model reference for automatic updates.
        this.model = model;

        this.setSize(500, 500);

        // Create panels
        northPanel = new JPanel(new BorderLayout());
        southPanel = new JPanel(new BorderLayout());
        eastPanel = new JPanel(new BorderLayout());
        westPanel = new JPanel(new BorderLayout());
        centerPanel = new JPanel(new BorderLayout());
        controllsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        fileNamePanel = new JPanel(new GridLayout(1, 2, 1, 1));

        // Set panel locations
        this.add(northPanel, BorderLayout.NORTH);
        this.add(southPanel, BorderLayout.SOUTH);
        this.add(eastPanel, BorderLayout.EAST);
        this.add(westPanel, BorderLayout.WEST);
        this.add(centerPanel, BorderLayout.CENTER);

        // Application title
        calendarIcon = new ImageIcon(
            getClass().getResource("calendar.png")
        );

        ApplicationHeaderLabel = new JLabel(
            "Calendar reminder application",
            calendarIcon,
            SwingConstants.LEFT
        );

        ApplicationHeaderLabel.setFont(
            new Font("Comic Sans MS", Font.BOLD, 22)
        );

        ApplicationHeaderLabel.setBackground(
            new Color(238, 203, 173)
        );

        ApplicationHeaderLabel.setOpaque(true);

        northPanel.add(
            ApplicationHeaderLabel,
            BorderLayout.PAGE_START
        );

        // Date selector section
        yearLabel = new JLabel("Year");
        yearLabel.setOpaque(true);
        yearLabel.setBackground(new Color(205, 175, 149));
        yearLabel.setFont(
            new Font("Comic Sans MS", Font.BOLD, 16)
        );
        yearLabel.setToolTipText(
            "Select year from the dropdown list"
        );

        monthLabel = new JLabel("Month");
        monthLabel.setOpaque(true);
        monthLabel.setBackground(new Color(222, 184, 135));
        monthLabel.setFont(
            new Font("Comic Sans MS", Font.BOLD, 16)
        );
        monthLabel.setToolTipText(
            "Select month from the dropdown list"
        );

        dayLabel = new JLabel("Day");
        dayLabel.setOpaque(true);
        dayLabel.setBackground(new Color(245, 222, 179));
        dayLabel.setFont(
            new Font("Comic Sans MS", Font.BOLD, 16)
        );
        dayLabel.setToolTipText(
            "Select day from the dropdown list"
        );

        Integer[] yearArray =
            new Integer[IReminderModel.maxYears];

        yearArray = model.getYears().toArray(yearArray);

        String[] monthArray =
            new String[IReminderModel.maxMonths];

        monthArray = model.getMonths().toArray(monthArray);

        Integer[] dayArray =
            new Integer[IReminderModel.maxDays];

        dayArray = model.getValidDays(
            new Date(1, 1, 2018)
        ).toArray(dayArray);

        yearComboBox = new JComboBox<Integer>(yearArray);
        yearComboBox.setBackground(
            new Color(205, 175, 149)
        );
        yearComboBox.setFont(
            new Font("Comic Sans MS", Font.PLAIN, 14)
        );

        monthComboBox = new JComboBox<String>(monthArray);
        monthComboBox.setBackground(
            new Color(222, 184, 135)
        );
        monthComboBox.setFont(
            new Font("Comic Sans MS", Font.PLAIN, 14)
        );

        dayComboBox = new JComboBox<Integer>(dayArray);
        dayComboBox.setBackground(
            new Color(245, 222, 179)
        );
        dayComboBox.setFont(
            new Font("Comic Sans MS", Font.PLAIN, 14)
        );

        dateSelectorPanel = new JPanel(
            new GridLayout(0, 2, 2, 2)
        );

        dateSelectorPanel.setOpaque(true);
        dateSelectorPanel.setBackground(Color.BLACK);

        dateSelectorPanel.add(yearLabel);
        dateSelectorPanel.add(yearComboBox);
        dateSelectorPanel.add(monthLabel);
        dateSelectorPanel.add(monthComboBox);
        dateSelectorPanel.add(dayLabel);
        dateSelectorPanel.add(dayComboBox);

        selectIcon = new ImageIcon(
            getClass().getResource("search.png")
        );

        selectTextButton = new JButton(
            "Search reminder text for selected date",
            selectIcon
        );

        selectTextButton.setAlignmentY(LEFT_ALIGNMENT);
        selectTextButton.setBackground(
            new Color(255, 215, 0)
        );
        selectTextButton.setFont(
            new Font("Comic Sans MS", Font.BOLD, 18)
        );
        selectTextButton.setToolTipText(
            "Get text saved for current selected date"
        );

        northPanel.add(
            dateSelectorPanel,
            BorderLayout.CENTER
        );

        northPanel.add(
            selectTextButton,
            BorderLayout.PAGE_END
        );

        // Text area section
        String defaultText =
            "Enter your reminder text here...";

        reminderText = new JTextArea(defaultText, 15, 20);
        reminderText.setBackground(
            new Color(255, 255, 225)
        );
        reminderText.setFont(
            new Font("Comic Sans MS", Font.PLAIN, 20)
        );

        centerPanel.add(
            new JScrollPane(reminderText),
            BorderLayout.CENTER
        );

        // Filename section
        fileName = new JTextField("Calendar.ser");
        fileName.setFont(
            new Font("David", Font.PLAIN, 20)
        );

        fileNameLabel = new JLabel("File name:");
        fileNameLabel.setFont(
            new Font("David", Font.BOLD, 20)
        );
        fileNameLabel.setToolTipText(
            "Name of file to be saved on disk"
        );

        fileNamePanel.add(fileNameLabel);
        fileNamePanel.add(fileName);

        centerPanel.add(
            fileNamePanel,
            BorderLayout.SOUTH
        );

        // Controls section
        saveIcon = new ImageIcon(
            getClass().getResource("save.png")
        );

        saveButton = new JButton("Save", saveIcon);
        saveButton.setToolTipText(
            "Download calendar data to file in serialized format"
        );
        controllsPanel.add(saveButton);

        importIcon = new ImageIcon(
            getClass().getResource("import.png")
        );

        importButton = new JButton("Import", importIcon);
        importButton.setToolTipText(
            "Import calendar data from serialized file"
        );
        controllsPanel.add(importButton);

        clearTextIcon = new ImageIcon(
            getClass().getResource("clearText.png")
        );

        clearTextButton = new JButton(
            "Clear text",
            clearTextIcon
        );

        clearTextButton.setToolTipText(
            "Clear text for current date"
        );
        controllsPanel.add(clearTextButton);

        // Delete button - Student 1
        deleteButton = new JButton("Delete");
        deleteButton.setToolTipText(
            "Delete reminder for selected date"
        );
        controllsPanel.add(deleteButton);

        // Important button - Student 3
        importantButton = new JButton("Important");
        importantButton.setToolTipText(
            "Mark or unmark reminder as important"
        );
        controllsPanel.add(importantButton);

        exportButton = new JButton("Export TXT");
        exportButton.setToolTipText(
            "Export reminders to a TXT file"
        );
        controllsPanel.add(exportButton);

        // FR-03: Search Reminders - Student 2
        // Create the keyword search field and button.
        searchField = new JTextField();
        searchField.setToolTipText(
            "Enter a keyword to search all reminders"
        );

        searchButton = new JButton("Search by Keyword");
        searchButton.setToolTipText(
            "Find reminders containing the entered keyword"
        );

        controllsPanel.add(new JLabel("Search Keyword:"));
        controllsPanel.add(searchField);
        controllsPanel.add(searchButton);

        // FR-04: View All Reminders - Student 2
        // Creates a button to display all saved reminders.
        viewAllButton = new JButton("View All Reminders");
        viewAllButton.setToolTipText(
            "Display all saved reminders"
        );
        controllsPanel.add(viewAllButton);

        eastPanel.add(controllsPanel);
        this.add(centerPanel, BorderLayout.CENTER);

        // Status bar
        statusBar = new JLabel(
            "@Chanan Welt: Maman14 - Question 2"
        );

        reminderCountLabel = new JLabel("Total Reminders: 0");

        reminderCountLabel.setFont(
            new Font("Comic Sans MS", Font.BOLD, 16)
        );

        reminderCountLabel.setOpaque(true);
        reminderCountLabel.setBackground(
            new Color(238, 203, 173)
        );

        southPanel.add(
            reminderCountLabel,
            BorderLayout.NORTH
        );

        statusBar.setFont(
            new Font("David", Font.PLAIN, 16)
        );

        statusBar.setOpaque(true);
        statusBar.setBackground(Color.LIGHT_GRAY);

        southPanel.add(
            statusBar,
            BorderLayout.SOUTH
        );
    }

    // Initialize GUI
    @Override
    public void initGui()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Observer Pattern - Student 2
        // Display the current reminder count when the GUI starts.
        setReminderCount(model.getReminderCount());

        setVisible(true);
    }

    // Register event handlers
    @Override
    public void addSaveListener(ActionListener saveActionListener)
    {
        saveButton.addActionListener(saveActionListener);
    }

    @Override
    public void addYearListener(ItemListener handler)
    {
        yearComboBox.addItemListener(handler);
    }

    @Override
    public void addMonthListener(ItemListener handler)
    {
        monthComboBox.addItemListener(handler);
    }

    @Override
    public void addDayListener(ItemListener handler)
    {
        dayComboBox.addItemListener(handler);
    }

    @Override
    public void addSelectTextListener(
            ActionListener selectTextHandler)
    {
        selectTextButton.addActionListener(selectTextHandler);
    }

    @Override
    public void addImportListener(ActionListener importHandler)
    {
        importButton.addActionListener(importHandler);
    }

    @Override
    public void addClearListener(ActionListener clearActionListener)
    {
        clearTextButton.addActionListener(clearActionListener);
    }

    // Delete Listener - Student 1
    @Override
    public void addDeleteListener(ActionListener deleteActionListener)
    {
        deleteButton.addActionListener(deleteActionListener);
    }

    // Important Listener - Student 3
    @Override
    public void addImportantListener(ActionListener importantActionListener)
    {
        importantButton.addActionListener(importantActionListener);
    }

    // Adds an action listener to the Export TXT button.
    @Override
    public void addExportListener(ActionListener exportActionListener)
    {
        exportButton.addActionListener(exportActionListener);
    }

    @Override
    public void addFileNameListener(ActionListener fileNameHandler)
    {
        fileName.addActionListener(fileNameHandler);
    }

    // FR-03: Search Reminders - Student 2
    // Registers the search button listener.
    @Override
    public void addSearchListener(ActionListener searchActionListener)
    {
        searchButton.addActionListener(searchActionListener);
    }

    // FR-04: View All Reminders - Student 2
    // Registers the View All button listener.
    @Override
    public void addViewAllListener(ActionListener viewAllActionListener)
    {
        viewAllButton.addActionListener(viewAllActionListener);
    }

    // Getters
    @Override
    public Date getInputDate()
    {
        return new Date(
            getInputDay(),
            getInputMonth(),
            getInputYear()
        );
    }

    @Override
    public int getInputYear()
    {
        return (Integer) yearComboBox.getSelectedItem();
    }

    @Override
    public int getInputMonth()
    {
        return 1 + monthComboBox.getSelectedIndex();
    }

    @Override
    public int getInputDay()
    {
        return (Integer) dayComboBox.getSelectedItem();
    }

    @Override
    public String getInputText()
    {
        return reminderText.getText();
    }

    @Override
    public String getInputFileName()
    {
        return fileName.getText();
    }

    // FR-03: Search Reminders - Student 2
    // Returns the keyword entered by the user.
    @Override
    public String getSearchKeyword()
    {
        return searchField.getText();
    }

    // Setters
    @Override
    public void setReminderInputText(String newText)
    {
        reminderText.setText(newText);
    }

    @Override
    public void setInputFileName(String newFileName)
    {
        fileName.setText(newFileName);
    }

    @Override
    public void setDayList(ArrayList<Integer> days)
    {
        dayComboBox.removeAllItems();

        for (int day : days)
        {
            dayComboBox.addItem(day);
        }
    }

    @Override
    public void setDate(Date date)
    {
        yearComboBox.setSelectedItem(date.getYear());
        monthComboBox.setSelectedIndex(
            date.getMonth() - 1
        );
        dayComboBox.setSelectedItem(date.getDay());
    }

    @Override
    public void setStatusBar(String message)
    {
        SecureRandom randomNumbers = new SecureRandom();

        int randomRedColor = randomNumbers.nextInt(256);
        int randomBlueColor = randomNumbers.nextInt(256);

        statusBar.setOpaque(true);
        statusBar.setText(message);
        statusBar.setBackground(
            new Color(randomRedColor, 255, randomBlueColor)
        );
    }

    // Reminder count display - Student 1
    public void setReminderCount(int count)
    {
        reminderCountLabel.setText(
            "Total Reminders: " + count
        );
    }

    // Observer Pattern - Student 2
    // Refreshes the reminder count when the Model changes.
    @Override
    public void update(Observable observable, Object changedObject)
    {
        setReminderCount(model.getReminderCount());

        if (changedObject != null)
        {
            setStatusBar(
                "File " + changedObject
                + " has been imported successfully"
            );
        }
    }

    // FR-03: Search Reminders - Student 2
    // Displays matching reminders in a scrollable dialog.
    @Override
    public void showSearchResults(Hashtable<Date, String> results)
    {
        if (results.isEmpty())
        {
            JOptionPane.showMessageDialog(
                this,
                "No matching reminders found.",
                "Search Results",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        StringBuilder output = new StringBuilder();

        for (Date date : results.keySet())
        {
            output.append("Date: ")
                  .append(date)
                  .append("\nReminder: ")
                  .append(results.get(date))
                  .append("\n----------------------\n");
        }

        JTextArea resultsArea = new JTextArea(output.toString());
        resultsArea.setEditable(false);
        resultsArea.setLineWrap(true);
        resultsArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setPreferredSize(
            new java.awt.Dimension(400, 250)
        );

        JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "Search Results",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    // FR-04: View All Reminders - Student 2
    // Displays every saved reminder in a scrollable dialog.
    // Shows a message when no reminders are available.
    @Override
    public void showAllReminders(Hashtable<Date, String> reminders)
    {
        if (reminders.isEmpty())
        {
            JOptionPane.showMessageDialog(
                this,
                "No reminders saved yet.",
                "All Reminders",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        StringBuilder output = new StringBuilder();

        for (Date date : reminders.keySet())
        {
            output.append("Date: ")
                  .append(date)
                  .append("\nReminder: ")
                  .append(reminders.get(date))
                  .append("\n----------------------\n");
        }

        JTextArea resultsArea = new JTextArea(output.toString());
        resultsArea.setEditable(false);
        resultsArea.setLineWrap(true);
        resultsArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setPreferredSize(
            new java.awt.Dimension(400, 250)
        );

        JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "All Reminders",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    // Display error message
    @Override
    public void displayErrorMessage(String message)
    {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Reminder Application",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
