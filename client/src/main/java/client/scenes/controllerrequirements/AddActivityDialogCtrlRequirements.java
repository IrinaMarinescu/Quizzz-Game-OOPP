package client.scenes.controllerrequirements;

import client.scenes.exceptions.InvalidDataException;
import java.io.File;
import java.io.IOException;
import javafx.stage.Stage;

public interface AddActivityDialogCtrlRequirements {

    /**
     * Closes the current open dialog.
     */
    void closeDialog();

    /**
     * Binds the scene to a new stage and resets the error message shown.
     *
     *  @param stage the stage to bind the scene to
     */
    void reset(Stage stage);

    /**
     * Sets the error text area visible with a given text.
     *
     * @param text the text to show in the error area.
     */
    void showErrorText(String text);

    /**
     * Opens a file chooser window to prompt the user to upload an image.
     */
    void openFileChooser();

    /**
     * Validates the properties of the uploaded file: extension, size (in both KB and pixels).
     *
     * @param file the file the user has uploaded
     * @throws InvalidDataException exception thrown if the file has some invalid properties (extension, size)
     * @throws IOException exception thrown if there were problems reading the file
     */
    void validateFileProperties(File file) throws InvalidDataException, IOException;

    /**
     * Executed when the user clicks the "add" button. Adds the activity to the database,
     * updates the table and uploads the image.
     */
    void addActivity();

    /**
     * Validates the data written in the form (unique ID, ensures user has uploaded a picture)
     *
     * @throws InvalidDataException exception thrown if any of the fields are not valid
     * (either an already existing ID was written, or no image was uploaded)
     */
    void validateFormData() throws InvalidDataException;
}
