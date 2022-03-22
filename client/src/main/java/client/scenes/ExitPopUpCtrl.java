package client.scenes;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ExitPopUpCtrl {

    private final MainCtrl mainCtrl;

    @FXML
    Rectangle rectangle;

    @FXML
    Button yesButton;

    @FXML
    Button noButton;

    @FXML
    Text text;

    @Inject
    public ExitPopUpCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void buttonClick(ActionEvent e) {
        String button = ((Button) e.getSource()).getId();
        if (button.equals(yesButton.getId())) {
            mainCtrl.disconnect();
            return;
        }
        mainCtrl.deniedExit();
    }

}
