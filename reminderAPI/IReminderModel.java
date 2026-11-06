
package reminderAPI;

import date.Date;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public interface IReminderModel
{
    // Constants
    public final int maxDays = 30;
    public final int maxMonths = 12;
    public final int maxYears = 50;

    // Getters
    public Date getCurrentDate();
    public String getReminder(Date date);
    public int getReminderCount();
    public String getFileName();
    public Hashtable<Date, String> getCalendar();
    public ArrayList<Integer> getYears();
    public ArrayList<String> getMonths();
    public ArrayList<Integer> getValidDays(Date date);

    // Setters
    public void setCalendar(Hashtable<Date, String> databaseCalendar);
    public void setFileName(String fileName);
    public void setReminder(Date date, String text);
    public boolean deleteReminder(Date date);

    // FR-03: Search Reminders - Student 2
    // Defines keyword-based searching for reminders.
    public Hashtable<Date, String> searchReminders(String keyword);

    // FR-04: View All Reminders - Student 2
    // Returns a copy of all saved reminders for display.
    // Prevents external code from modifying the original calendar.
    public Hashtable<Date, String> getAllReminders();

    // Important - Student 3
    public void setImportant(Date date, boolean important);
    public boolean isImportant(Date date);

    // I/O Interface
    public void WriteCalendarToDatabase() throws IOException;
    public boolean readCalendarFromDatabase()
            throws IOException, ClassNotFoundException;

    public void exportRemindersToTXT() throws IOException;
}
