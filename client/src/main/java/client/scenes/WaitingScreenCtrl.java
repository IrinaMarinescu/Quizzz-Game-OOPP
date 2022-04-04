package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

/**
 * The controller for the mock button on the waiting screen
 */
public class WaitingScreenCtrl {

    @FXML
    ImageView check;
    @FXML
    Button yesButton;

    /**
     * Hides the check and enables button
     */
    public void reset() {
        yesButton.setDisable(false);
        check.setVisible(false);
    }

    /**
     * Shows the check and disabled button
     */
    @FXML
    public void yesClicked() {
        yesButton.setDisable(true);
        check.setVisible(true);
    }
}
