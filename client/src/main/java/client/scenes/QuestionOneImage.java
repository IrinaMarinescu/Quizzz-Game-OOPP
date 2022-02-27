package client.scenes;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javax.inject.Inject;

public class QuestionOneImage {

    private MainCtrl mainCtrl;
    private QuestionFrameCtrl questionFrameCtrl;

    @FXML
    Button answerA;

    @FXML
    Button answerB;

    @FXML
    Button answerC;

    /**
     * Injects necessary dependencies
     *
     * @param mainCtrl          - the main front-end controller
     * @param questionFrameCtrl - the scene into which it has to be injected
     */
    @Inject
    public QuestionOneImage(MainCtrl mainCtrl, QuestionFrameCtrl questionFrameCtrl) {
        this.mainCtrl = mainCtrl;
        this.questionFrameCtrl = questionFrameCtrl;
    }

    @FXML
    public void setAnswerA() {
        answerA.setText("Clicked!");
    }

    ;

    @FXML
    public void setAnswerB() {
        answerB.setText("Clicked!");
    }

    ;

    @FXML
    public void setAnswerC() {
        answerC.setText("Clicked!");
    }

    ;
}
