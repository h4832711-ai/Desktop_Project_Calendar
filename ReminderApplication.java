
import reminderController.ReminderController;
import reminderModel.ReminderModel;
import reminderView.ReminderView;

public class ReminderApplication
{
    public static void main(String[] args)
    {
        // Get the single Model instance - Singleton
        ReminderModel model = ReminderModel.getInstance();

        // Create the View
        ReminderView view = new ReminderView(model);

        // Create the Controller
        ReminderController<ReminderModel, ReminderView> controller;

        controller = new ReminderController<ReminderModel, ReminderView>(
            model,
            view
        );

        // Initialize application
        controller.initializeApplication();
    }
}
