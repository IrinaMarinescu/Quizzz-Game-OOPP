package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class FinalScreenCtrl {

    private MainCtrl mainCtrl;
    private QuestionFrameCtrl questionFrameCtrl;

    @FXML
    Text scoreField;

    /**
     * Injects necessary dependencies
     *
     * @param mainCtrl          the main front-end controller
     * @param questionFrameCtrl the scene into which it has to be injected
     */
    @Inject
    public FinalScreenCtrl(MainCtrl mainCtrl, QuestionFrameCtrl questionFrameCtrl) {
        this.mainCtrl = mainCtrl;
        this.questionFrameCtrl = questionFrameCtrl;
    }

    @FXML
    public void setPoints(int points) {
        scoreField.setText(String.valueOf(points));
    }
}
