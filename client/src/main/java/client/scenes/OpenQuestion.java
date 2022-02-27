package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.inject.Inject;

public class OpenQuestion {

    private MainCtrl mainCtrl;
    private QuestionFrameCtrl questionFrameCtrl;

    @FXML
    Button submitButton;

    /**
     * Injects necessary dependencies
     * @param mainCtrl - the main front-ent controller
     * @param questionFrameCtrl - the scene into which it has to be injected
     */
    @Inject
    public OpenQuestion(MainCtrl mainCtrl, QuestionFrameCtrl questionFrameCtrl) {
        this.mainCtrl = mainCtrl;
        this.questionFrameCtrl = questionFrameCtrl;
    }


    /**
     * Changes the text on the "Submit" button to "Submitted!" if it is clicked
     */
    @FXML
    public void submit(){submitButton.setText("Submitted!");};
}
