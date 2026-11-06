
package reminderController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import reminderAPI.IReminderController;
import reminderAPI.IReminderModel;
import reminderAPI.IReminderView;

public class ReminderController<M extends Observable & IReminderModel,
                                V extends Observer & IReminderView>
        implements IReminderController
{
    // Utility enumerations
    public static enum comboBoxType
    {
        YEAR, MONTH, DAY
    };

    // Instance variables
    private M model;
    private V view;

    // Constructor
    public ReminderController(M model, V view)
    {
        // Register model and view in the controller
        this.model = model;
        this.view = view;

        // Register view as an observer of the model
        model.addObserver(view);

        // Register event handlers for view
        view.addDayListener(
            new comboBoxListener(comboBoxType.DAY)
        );

        view.addMonthListener(
            new comboBoxListener(comboBoxType.MONTH)
        );

        view.addYearListener(
            new comboBoxListener(comboBoxType.YEAR)
        );

        view.addSelectTextListener(new SelectTextHandler());
        view.addSaveListener(new SaveHandler());
        view.addImportListener(new ImportHandler());
        view.addClearListener(new ClearTextHandler());
        view.addImportantListener(new ImportantHandler());

        // Connects the Export TXT button to its handler.
        view.addExportListener(new ExportHandler());

        // Delete - Student 1
        view.addDeleteListener(new DeleteHandler());

        view.addFileNameListener(new fileNameHandler());

        // FR-03: Search Reminders - Student 2
        // Connects the keyword search button to SearchHandler.
        view.addSearchListener(new SearchHandler());

        // FR-04: View All Reminders - Student 2
        // Connects the View All button to its event handler.
        view.addViewAllListener(new ViewAllHandler());
    }

    // Set default data and initialize GUI
    @Override
    public void initializeApplication()
    {
        view.setDate(model.getCurrentDate());

        view.setReminderInputText(
            "Select dates,\n" +
            "enter your reminders text here,\n" +
            "and save to file."
        );

        // Initialize reminder count - Student 1
        view.setReminderCount(model.getReminderCount());

        view.initGui();
    }

    // Handler for writing reminders to database file
    private class SaveHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            String modelFileName = model.getFileName();
            String inputFileName = view.getInputFileName();

            if (inputFileName == null ||
                inputFileName.length() == 0)
            {
                view.displayErrorMessage(
                    "Error: Cannot save to a file with no name"
                );
            }
            else
            {
                if (!modelFileName.equals(inputFileName))
                {
                    model.setFileName(inputFileName);
                }

                // Save reminder in the model
                model.setReminder(
                    view.getInputDate(),
                    view.getInputText()
                );

                // Update reminder count - Student 1
                view.setReminderCount(
                    model.getReminderCount()
                );

                try
                {
                    model.WriteCalendarToDatabase();

                    view.setStatusBar(
                        String.format(
                            "Reminder for date %s successfully saved in file \"%s\"",
                            view.getInputDate(),
                            model.getFileName()
                        )
                    );
                }
                catch (IOException e)
                {
                    view.displayErrorMessage(
                        "Application failed saving data to file!"
                    );
                }
            }
        }
    }

    // Handler for loading data from database file
    private class ImportHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            try
            {
                if (model.readCalendarFromDatabase())
                {
                    view.setReminderInputText(
                        model.getReminder(view.getInputDate())
                    );

                    // Update reminder count after import
                    view.setReminderCount(
                        model.getReminderCount()
                    );

                    view.setStatusBar(
                        String.format(
                            "File %s imported successfully",
                            model.getFileName()
                        )
                    );
                }
            }
            catch (ClassNotFoundException e)
            {
                view.displayErrorMessage(
                    "Error: Invalid file format"
                );
            }
            catch (IOException e)
            {
                view.displayErrorMessage(
                    "Error: Could not open file"
                );
            }
            finally
            {
                view.setInputFileName(
                    model.getFileName()
                );
            }
        }
    }

    // Handler for retrieving text for selected date
    private class SelectTextHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            view.setReminderInputText(
                model.getReminder(view.getInputDate())
            );

            view.setStatusBar(
                String.format(
                    "Text for date %s selected successfully",
                    view.getInputDate()
                )
            );
        }
    }

    // Handler for clearing text area
    private class ClearTextHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            view.setReminderInputText(null);
        }
    }

    // Important Reminder - Student 3
    private class ImportantHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            boolean newStatus =
                !model.isImportant(view.getInputDate());

            model.setImportant(
                view.getInputDate(),
                newStatus
            );

            if (newStatus)
            {
                view.setStatusBar(
                    "Reminder marked as important!"
                );
            }
            else
            {
                view.setStatusBar(
                    "Reminder is no longer important!"
                );
            }
        }
    }

    // Handles exporting reminders to a TXT file.
    private class ExportHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            try
            {
                model.exportRemindersToTXT();

                view.setStatusBar(
                    "Reminders exported successfully to Reminders.txt"
                );
            }
            catch (IOException ex)
            {
                view.setStatusBar(
                    "Error exporting reminders: " + ex.getMessage()
                );
            }
        }
    }

    // Delete Reminder - Student 1
    private class DeleteHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            if (model.deleteReminder(view.getInputDate()))
            {
                // Clear text after deletion
                view.setReminderInputText(null);

                // Update reminder count
                view.setReminderCount(
                    model.getReminderCount()
                );

                try
                {
                    // Save updated calendar
                    model.WriteCalendarToDatabase();

                    view.setStatusBar(
                        "Reminder deleted successfully!"
                    );
                }
                catch (IOException e)
                {
                    view.displayErrorMessage(
                        "Reminder deleted, but failed to save changes to file!"
                    );
                }
            }
            else
            {
                view.displayErrorMessage(
                    "No reminder found for the selected date!"
                );
            }
        }
    }

    // Handler for setting a new file name
    private class fileNameHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            String newFileName = view.getInputFileName();

            if (newFileName != null &&
                newFileName.length() != 0)
            {
                model.setFileName(newFileName);

                view.setStatusBar(
                    String.format(
                        "File name Successfully changed to %s",
                        newFileName
                    )
                );
            }
            else
            {
                view.setInputFileName(
                    model.getFileName()
                );

                view.displayErrorMessage(
                    "You cannot enter empty file name!\n" +
                    "File name is restored to previous one"
                );
            }
        }
    }

    // Handler for date combo boxes
    private class comboBoxListener implements ItemListener
    {
        private comboBoxType changedComboBox;

        public comboBoxListener(comboBoxType changedComboBox)
        {
            this.changedComboBox = changedComboBox;
        }

        @Override
        public void itemStateChanged(ItemEvent event)
        {
            // Adjust day list and clear old text
            if (event.getStateChange() == ItemEvent.SELECTED)
            {
                if (this.changedComboBox != comboBoxType.DAY)
                {
                    view.setDayList(
                        model.getValidDays(
                            view.getInputDate()
                        )
                    );
                }

                view.setReminderInputText(null);
            }
        }
    }

    // FR-03: Search Reminders - Student 2
    // Handles keyword search requests from the View.
    // Calls the Model to find matching reminders and
    // sends the results back to the View for display.
    private class SearchHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            String keyword = view.getSearchKeyword();

            // FR-03: Search Reminders - Student 2
            // Prevent searching with an empty keyword.
            if (keyword == null || keyword.trim().isEmpty())
            {
                view.displayErrorMessage(
                    "Please enter a keyword to search."
                );
                return;
            }

            // FR-03: Search Reminders - Student 2
            // Retrieve matching reminders and display them.
            view.showSearchResults(
                model.searchReminders(keyword)
            );
        }
    }

    // FR-04: View All Reminders - Student 2
    // Handles the View All button click.
    // Retrieves all saved reminders from the Model
    // and sends them to the View for display.
    private class ViewAllHandler implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent event)
        {
            view.showAllReminders(
                model.getAllReminders()
            );
        }
    }
}
