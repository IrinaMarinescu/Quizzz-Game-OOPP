package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class WaitingScreenCtrl {

    @FXML
    ImageView check;
    @FXML
    Button yesButton;

    public void reset() {
        yesButton.setDisable(false);
        check.setVisible(false);
    }

    @FXML
    public void yesClicked() {
        yesButton.setDisable(true);
        check.setVisible(true);
    }
}
