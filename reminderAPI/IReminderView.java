
package reminderAPI;

import date.Date;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Observer;

public interface IReminderView extends Observer
{
    // Getters:
    public int getInputDay();
    public int getInputMonth();
    public int getInputYear();
    public Date getInputDate();
    public String getInputText();
    public String getInputFileName();

    // FR-03: Search Reminders - Student 2
    // Gets the keyword entered by the user.
    public String getSearchKeyword();

    // Setters:
    public void setDate(Date date);
    public void setStatusBar(String message);
    public void setInputFileName(String newFileName);
    public void setDayList(ArrayList<Integer> days);
    public void setReminderInputText(String newText);
    public void setReminderCount(int count);

    // FR-03: Search Reminders - Student 2
    // Displays the reminders matching the search keyword.
    public void showSearchResults(Hashtable<Date, String> results);

    // FR-04: View All Reminders - Student 2
    // Displays all saved reminders in the View.
    public void showAllReminders(Hashtable<Date, String> reminders);

    // GUI general operations:
    public void displayErrorMessage(String message);
    public void initGui();

    // Event listeners registration interface:
    public void addDayListener(ItemListener dayItemListener);
    public void addMonthListener(ItemListener monthItemListener);
    public void addYearListener(ItemListener yearItemListener);
    public void addSaveListener(ActionListener saveHandler);
    public void addImportListener(ActionListener loadHandler);
    public void addSelectTextListener(ActionListener selectTextHandler);
    public void addClearListener(ActionListener clearActionListener);
    public void addDeleteListener(ActionListener deleteActionListener);
    public void addImportantListener(ActionListener importantActionListener);
    public void addFileNameListener(ActionListener fileNameHandler);
    public void addExportListener(ActionListener exportActionListener);

    // FR-03: Search Reminders - Student 2
    // Registers the event handler for the keyword search button.
    public void addSearchListener(ActionListener searchActionListener);

    // FR-04: View All Reminders - Student 2
    // Registers the View All button with the Controller.
    public void addViewAllListener(ActionListener viewAllActionListener);
}
