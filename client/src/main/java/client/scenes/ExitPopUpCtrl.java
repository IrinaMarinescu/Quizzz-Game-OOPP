package client.scenes;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ExitPopUpCtrl {

    private final MainCtrl mainCtrl;
    private int type;

    @FXML
    Rectangle rectangle;

    @FXML
    Button yesButton;

    @FXML
    Button noButton;

    @FXML
    Text question;

    @Inject
    public ExitPopUpCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.type = 1;
    }

    public void buttonClick(ActionEvent e) {
        String buttonID = ((Button) e.getSource()).getId();
        mainCtrl.disconnect(this.type, buttonID);
    }

    public void setType(int type) {
        this.type = type;
        if (type == 1) {
            question.setText("Are you sure you want to close the application?");
        } else {
            question.setText("Are you sure you want to leave the game?");
        }
    }

    public void keyPressed(KeyCode code) {
        if (code == KeyCode.ESCAPE) {
            mainCtrl.toggleModalVisibility();
        }
    }
}
