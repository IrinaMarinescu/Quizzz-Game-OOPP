package client.scenes.controllerrequirements;

import commons.Activity;
import java.util.List;

public interface AdminInterfaceCtrlRequirements {

    /**
     * Initializes the page with a set list of activities.
     *
     * @param activities the list of activities to populate the table with.
     */
    void initialize(List<Activity> activities);

    /**
     * Used to update the activity data on the page.
     *
     * @param newActivity the new activity to show on the left hand side of the page.
     */
    void updateActivityData(Activity newActivity);

    /**
     * Redirects back to the main frame.
     */
    void showMainFrame();

    /**
     * Issues an update to the selected activity, based on the data written in the text fields.
     */
    void updateSelectedActivity();

    /**
     * Issues a delete to an activity in the database, based on the activity the user has selected from the table.
     */
    void deleteSelectedActivity();
}
