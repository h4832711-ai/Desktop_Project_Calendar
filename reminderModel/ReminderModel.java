
package reminderModel;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Observable;

import javax.swing.JFileChooser;

import date.Date;
import reminderAPI.IReminderModel;

import java.io.PrintWriter;

public class ReminderModel extends Observable
        implements IReminderModel, Serializable
{
    // Static variables
    private static final long serialVersionUID = 1L;

    // Singleton Pattern - Student 1
    private static final ReminderModel instance =
            new ReminderModel();

    public static ReminderModel getInstance()
    {
        return instance;
    }

    // Instance variables
    private Hashtable<Date, String> calendar;
    private String fileName;
    private Hashtable<Date, Boolean> importantReminders;

    // Private constructor for Singleton
    private ReminderModel()
    {
        super();
        calendar = new Hashtable<Date, String>();
        importantReminders = new Hashtable<Date, Boolean>();
        fileName = "calendar.ser";
    }

    // Basic Getters
    @Override
    public String getReminder(Date date)
    {
        return calendar.get(date);
    }

    @Override
    public int getReminderCount()
    {
        return calendar.size();
    }

    
    // FR-03: Search Reminders - Student 2
    // Searches all reminder texts using a keyword.
    // The search is case-insensitive and returns matching reminders.
    @Override
    public Hashtable<Date, String> searchReminders(String keyword)
    {
        Hashtable<Date, String> results = new Hashtable<>();

        if (keyword == null || keyword.trim().isEmpty())
        {
            return results;
        }

        String searchText = keyword.trim().toLowerCase(java.util.Locale.ROOT);

        for (Date date : calendar.keySet())
        {
            String reminder = calendar.get(date);

            if (reminder != null &&
                reminder.toLowerCase(java.util.Locale.ROOT).contains(searchText))
            {
                results.put(date, reminder);
            }
        }

        return results;
    }

    // FR-04: View All Reminders - Student 2
// Returns a copy of all saved reminders.
// Protects the original calendar from external modifications.
@Override
public Hashtable<Date, String> getAllReminders()
{
    return new Hashtable<Date, String>(calendar);
}

    public Hashtable<Date, String> getCalendar()
    {
        return calendar;
    }

    @Override
    public String getFileName()
    {
        return fileName;
    }

    @Override
    public Date getCurrentDate()
    {
        return new Date();
    }

    // Basic Setters

    // Observer Pattern - Student 2
    // Notify registered observers whenever a reminder is
    // added or updated, so the View can refresh automatically.
    @Override
    public void setReminder(Date date, String text)
    {
        calendar.put(date, text);

        setChanged();
        notifyObservers();
    }

    public void setCalendar(
            Hashtable<Date, String> databaseCalendar)
    {
        this.calendar = databaseCalendar;
    }

    @Override
    public void setFileName(String fileName)
    {
        if (fileName != null && !fileName.isEmpty())
        {
            this.fileName = fileName;
        }
    }

    // Delete Reminder - Student 1
    @Override
    public boolean deleteReminder(Date date)
    {
        if (date == null || !calendar.containsKey(date))
        {
            return false;
        }

        calendar.remove(date);
        importantReminders.remove(date);

        setChanged();
        notifyObservers();

        return true;
    }

    @Override
    public void setImportant(Date date, boolean important)
    {
        if (date != null && calendar.containsKey(date))
        {
            importantReminders.put(date, important);
            setChanged();
            notifyObservers();
        }
    }

    @Override
    public boolean isImportant(Date date)
    {
        return importantReminders.getOrDefault(date, false);
    }

    // Get list of years
    @Override
    public ArrayList<Integer> getYears()
    {
        int startingYear = 2000;
        int maxYear = 2050;

        ArrayList<Integer> years =
                new ArrayList<Integer>();

        for (int year = startingYear; year <= maxYear; year++)
        {
            years.add(year);
        }

        return years;
    }

    // Get list of months
    @Override
    public ArrayList<String> getMonths()
    {
        String[] monthsArray = {
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        };

        return new ArrayList<String>(
            Arrays.asList(monthsArray)
        );
    }

    // Get list of valid days
    @Override
    public ArrayList<Integer> getValidDays(Date date)
    {
        int maxDay = Date.maxDayOfMonthAndYear(
            date.getYear(),
            date.getMonth()
        );

        ArrayList<Integer> validDays =
                new ArrayList<Integer>();

        for (int day = 1; day <= maxDay; day++)
        {
            validDays.add(day);
        }

        return validDays;
    }

    // Save reminders to database file
    @Override
    public void WriteCalendarToDatabase() throws IOException
    {
        try (ObjectOutputStream outputfile =
                new ObjectOutputStream(
                    Files.newOutputStream(Paths.get(fileName))
                ))
        {
            // Save reminders
            outputfile.writeObject(calendar);

            // Save Important status
            outputfile.writeObject(importantReminders);
        }
    }

    @Override
    public void exportRemindersToTXT() throws IOException
    {
        try (PrintWriter writer = new PrintWriter(
                Files.newBufferedWriter(Paths.get("Reminders.txt"))))
        {
            for (Date date : calendar.keySet())
            {
                writer.println("Date: " + date);
                writer.println("Reminder: " + calendar.get(date));
                writer.println("Important: " + isImportant(date));
                writer.println("--------------------------------");
            }
        }
    }

    // Import reminders from database file
    @Override
    @SuppressWarnings("unchecked")
    public boolean readCalendarFromDatabase()
            throws IOException, ClassNotFoundException
    {
        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setFileSelectionMode(
            JFileChooser.FILES_ONLY
        );

        if (fileChooser.showOpenDialog(null)
                != JFileChooser.APPROVE_OPTION)
        {
            return false;
        }

        File inputFile = fileChooser.getSelectedFile();

        try (ObjectInputStream inputStream =
                new ObjectInputStream(
                    Files.newInputStream(inputFile.toPath())
                ))
        {
            Object data = inputStream.readObject();

            if (!(data instanceof Hashtable))
            {
                throw new ClassNotFoundException(
                    "Unable to interpret file"
                );
            }

            this.calendar = (Hashtable<Date, String>) data;

            // Read Important status
            try
            {
                Object importantData = inputStream.readObject();

                if (importantData instanceof Hashtable)
                {
                    this.importantReminders =
                        (Hashtable<Date, Boolean>) importantData;
                }
                else
                {
                    this.importantReminders =
                        new Hashtable<Date, Boolean>();
                }
            }
            catch (java.io.EOFException e)
            {
                // Support old files that contain reminders only
                this.importantReminders =
                    new Hashtable<Date, Boolean>();
            }

            this.fileName = inputFile.getName();

            setChanged();
            notifyObservers(fileName);

            return true;
        }
    }
}
