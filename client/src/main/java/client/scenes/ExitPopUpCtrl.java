package client.scenes;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;

public class ExitPopUpCtrl {

    @FXML
    Rectangle rectangle;

    @FXML
    Button yesButton;

    @FXML
    Button noButton;

    final MainCtrl mainCtrl;

    @Inject
    public ExitPopUpCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void buttonClick(ActionEvent e) {
        Button button = (Button) e.getSource();
        if (button.equals(yesButton)) {
            mainCtrl.disconnect();
            return;
        }
        mainCtrl.deniedExit();
    }

}
