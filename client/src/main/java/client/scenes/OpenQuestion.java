package client.scenes;

import client.scenes.controllerrequirements.QuestionRequirements;
import commons.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


import javax.inject.Inject;

public class OpenQuestion implements QuestionRequirements {

    private MainCtrl mainCtrl;
    private QuestionFrameCtrl questionFrameCtrl;
    private Question question;
    private int answer;

    @FXML
    Button submitButton;

    @FXML
    Text questionField;

    @FXML
    Text answerText;

    @FXML
    Text pointsText;

    @FXML
    TextField entryField;

    /**
     * Injects necessary dependencies
     *
     * @param mainCtrl          - the main front-ent controller
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
    public void submit() {
        submitButton.setText("Submitted!");
    }

    @Override
    public void initialize(Question question) {
        this.question = question;
        questionField.setText("How many Wh does " + question.getActivities().get(0).getTitle() + " take?");
    }

    @Override
    public void revealCorrectAnswer(Question question) {
        answerText.setText("It takes " + question.getActivities().get(0).getConsumptionInWh() + "!");
    }

    ;
}
