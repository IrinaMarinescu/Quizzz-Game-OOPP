package client.scenes;

import client.scenes.controllerrequirements.QuestionRequirements;
import commons.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javax.inject.Inject;

public class QuestionOneImage implements QuestionRequirements {

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

    /**
     * Sets the text on Button A to 'Clicked!' and disables buttons B and C
     */
    @FXML
    public void setAnswerA() {
        answerA.setText("Clicked!");
        answerB.setOnAction(null);
        answerC.setOnAction(null);
    }

    ;
    /**
     * Sets the text on Button B to 'Clicked!' and disables buttons A and C
     */
    @FXML
    public void setAnswerB() {
        answerB.setText("Clicked!");
        answerA.setOnAction(null);
        answerC.setOnAction(null);
    }

    ;
    /**
     * Sets the text on Button C to 'Clicked!' and disables buttons A and B
     */
    @FXML
    public void setAnswerC() {
        answerC.setText("Clicked!");
        answerA.setOnAction(null);
        answerB.setOnAction(null);
    }

    @Override
    public void initialize(Question question) {

    }

    @Override
    public void revealCorrectAnswer() {

    }

    ;
}
