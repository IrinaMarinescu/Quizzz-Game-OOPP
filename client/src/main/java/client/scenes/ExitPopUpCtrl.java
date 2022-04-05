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
    Text text;

    /**
     * Injects the necessary dependencies
     *
     * @param mainCtrl - - the main front-end controller
     */
    @Inject
    public ExitPopUpCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.type = 1;
    }

    /**
     * Sends to the mainCtrl the type of exit that is attempted and which button is chosen
     *
     * @param e - the click of the button
     */
    public void buttonClick(ActionEvent e) {
        String buttonID = ((Button) e.getSource()).getId();
        mainCtrl.disconnect(this.type, buttonID);
    }

    /**
     * Sets what type of exit is attempted
     *
     * @param type - 0 means that its a complete exit of the window, while 1 is an exit to the main menu
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Ties the escape shortcut to this controller
     *
     * @param code - the code of the key
     */
    public void keyPressed(KeyCode code) {
        if (code == KeyCode.ESCAPE) {
            mainCtrl.toggleModalVisibility();
        }
    }

    /**
     * Getter of the type of exit
     *
     * @return type - 0 or 1
     */
    public int getType() {
        return this.type;
    }

    /**
     * Getter of the MainCtrl
     *
     * @return mainCtrl
     */
    public MainCtrl getMainCtrl() {
        return this.mainCtrl;
    }
}
