package client.scenes;

import client.scenes.controllerrequirements.AddActivityDialogCtrlRequirements;
import client.scenes.exceptions.InvalidDataException;
import client.utils.ServerUtils;
import commons.Activity;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javax.inject.Inject;

public class AddActivityDialogCtrl implements AddActivityDialogCtrlRequirements {

    @FXML
    private Button cancelButton;
    @FXML
    private TextField activityId;
    @FXML
    private TextField activityTitle;
    @FXML
    private TextField activityConsumption;
    @FXML
    private TextField activitySource;
    @FXML
    private Text feedbackText;
    @FXML
    private ImageView activityImage;

    private Stage stage;
    private File uploadedImage;

    private final AdminInterfaceCtrl adminInterfaceCtrl;
    private final ServerUtils serverUtils;

    private final String[] acceptedExtensions = {".png", ".jpg", ".jpeg", ".gif"};

    protected boolean test = false;

    @Inject
    public AddActivityDialogCtrl(AdminInterfaceCtrl adminInterfaceCtrl, ServerUtils serverUtils) {
        this.adminInterfaceCtrl = adminInterfaceCtrl;
        this.serverUtils = serverUtils;
    }

    /**
     * {@inheritDoc}
     */
    @FXML
    public void closeDialog() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * {@inheritDoc}
     *
     * @param stage the stage to bind the scene to
     */
    public void reset(Stage stage) {
        this.stage = stage;
        feedbackText.setVisible(false);
    }

    /**
     * {@inheritDoc}
     *
     * @param text the text to show in the feedback area.
     */
    public void showErrorText(String text) {
        showText(text, true);
    }


    /**
     * {@inheritDoc}
     *
     * @param text the text to show in the feedback area.
     */
    public void showSuccessText(String text) {
        showText(text, false);
    }

    /**
     * Internal helper method that shows the feedback text area with
     * a given text and a specific colour.
     *
     * @param text the text to show in the feedback area.
     * @param error a boolean value, indicating whether the text is an error or not.
     */
    protected void showText(String text, boolean error) {
        feedbackText.setText(text);
        feedbackText.setVisible(true);
        if (error) {
            feedbackText.setFill(Color.RED);
        } else {
            feedbackText.setFill(Color.GREEN);
        }
    }

    /**
     * {@inheritDoc}
     */
    @FXML
    public void openFileChooser() {
        try {
            final FileChooser fileChooser = new FileChooser();

            File upload = fileChooser.showOpenDialog(stage);

            if (upload != null) {
                validateFileProperties(upload);
                activityImage.setImage(
                    new Image(upload.toURI().toString(),
                        270, 200, false, false)
                );
                setUploadedImage(upload);
            }
        } catch (Exception e) {
            showErrorText(e.getMessage());
        }
    }

    protected void setUploadedImage(File file) {
        uploadedImage = file;
    }

    protected File getUploadedImage() {
        return uploadedImage;
    }

    /**
     * {@inheritDoc}
     *
     * @param file the file the user has uploaded
     * @throws InvalidDataException exception thrown if the file has some invalid properties (extension, size)
     * @throws IOException exception thrown if there were problems reading the file
     */
    public void validateFileProperties(File file) throws InvalidDataException, IOException {
        if (file == null) {
            throw new InvalidDataException("Invalid file!");
        }

        Optional<String> extension = getExtension(file.getName());
        if (extension.isEmpty()) {
            throw new InvalidDataException("The file must have an extension!");
        }

        boolean equalsOne = false;
        for (String s : acceptedExtensions) {
            if (s.equalsIgnoreCase(extension.get())) {
                equalsOne = true;
                break;
            }
        }
        if (!equalsOne) {
            throw new InvalidDataException("This file extension (" + extension.get() + ") is not accepted! ");
        }
        if (file.length() / 1024 > 500) {
            throw new InvalidDataException("The file size must be at most 500kb!");
        }

        BufferedImage img = ImageIO.read(file);
        if (img.getWidth() < 200 || img.getHeight() < 200) {
            throw new InvalidDataException(
                "The image size must be at least 200x200. Yours is " + img.getWidth() + "x" + img.getHeight());
        }
    }

    /**
     * Internal helper function. Returns the extension of a file based on the name.
     *
     * @param filename the name of the file to return the extension for.
     * @return an Optional containing a String, that might or might not be present
     * depending on whether the file name has an extension or not.
     */
    protected Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
            .filter(f -> f.contains("."))
            .map(f -> f.substring(filename.lastIndexOf(".")));
    }

    /**
     * {@inheritDoc}
     */
    @FXML
    public void addActivity() {
        try {
            validateFormData();
            Activity newActivity =
                new Activity(
                    activityId.getText(), createPath(activityId.getText(), uploadedImage),
                    activityTitle.getText(), Long.parseLong(activityConsumption.getText()),
                    activitySource.getText()
                );

            boolean success = serverUtils.addActivity(newActivity, uploadedImage);
            if (success) {
                adminInterfaceCtrl.addActivityToTable(newActivity);
                showSuccessText("Activity was added successfully!");

                resetActivityFields();
            }
        } catch (Exception e) {
            showErrorText(e.getMessage());
        }
    }

    /**
     * Internal helper method. Resets all the fields after adding a new activity.
     */
    private void resetActivityFields() {
        activityId.setText("");
        activityTitle.setText("");
        activityConsumption.setText("");
        activitySource.setText("");
        activityImage.setImage(
            new Image(
                "https://png.pngtree.com/png-vector/20210604/ourmid/pngtree-gray-network-placeholder-png-image_3416659.jpg",
                270, 200, false, false)
        );
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidDataException exception thrown if any of the fields are not valid
     * (either an already existing ID was written, or no image was uploaded)
     */
    public void validateFormData() throws InvalidDataException {
        validateFormDataHelper(activityId.getText(), activityConsumption.getText());
    }

    /**
     * Internal helper method that helps with form data validation.
     *
     * @param id the id of the activity to validate
     * @param consumption the consumption of the activity to validate
     * @throws InvalidDataException exception thrown if no image was
     * uploaded, the id is not alphanumeric or the consumption is
     * invalid (not an integer, a negative number, etc.)
     */
    protected void validateFormDataHelper(String id, String consumption) throws InvalidDataException {
        try {
            long c = Long.parseLong(consumption);
            if (c < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            throw new InvalidDataException("The consumption value is not valid! It should be a positive integer.");
        }

        if (!id.matches("^[a-zA-Z0-9]*$")) {
            throw new InvalidDataException("Only alphanumerical characters are accepted as activity IDs!");
        }

        if (!test) {
            for (Activity a : adminInterfaceCtrl.getActivities()) {
                if (a.id.equals(id)) {
                    throw new InvalidDataException("An activity with the same ID already exists in the database!");
                }
            }
            if (uploadedImage == null) {
                throw new InvalidDataException("You have to upload an image!");
            }
        }
    }

    /**
     * Internal helper method. Automatically creates a path to the image by parsing the ID.
     *
     * @param id the ID of the activity
     * @return the appropriate path for the image.
     */
    protected String createPath(String id, File image) {
        return id + getExtension(image.getName()).get();
    }

}
