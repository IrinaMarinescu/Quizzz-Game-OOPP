package client.scenes;

import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    Text text;

    @Inject
    public ExitPopUpCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.type = 0;
    }

    public void buttonClick(ActionEvent e) {
        String buttonID = ((Button) e.getSource()).getId();
        if (buttonID.equals("yesButton")) {
            if (this.type == 0) {
                mainCtrl.disconnect();
                return;
            }
            System.exit(0);
        }
        if (buttonID.equals("noButton")) {
            mainCtrl.deniedExit();
        }
    }

    public void setType(int type) {
        this.type = type;
    }
}
