package client.scenes;

import client.scenes.controllerrequirements.QuestionRequirements;
import commons.Activity;
import commons.Question;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.inject.Inject;


public class QuestionTrueFalseCtrl extends Question implements QuestionRequirements {

    private MainCtrl mainCtrl;
    private QuestionFrameCtrl questionFrameCtrl;

    @FXML
    Button trueAnswer;

    @FXML
    Button falseAnswer;

    @FXML
    TextField questionOutput;

    @FXML
    ImageView imageOutput;

    @FXML
    ImageView correctTrue;

    @FXML
    ImageView wrongTrue;

    @FXML
    ImageView correctFalse;

    @FXML
    ImageView wrongFalse;

    /**
     * injects the necessary dependencies
     *
     * @param activities    there should only be one activity
     * @param question      a generated question based on the activity
     * @param correctAnswer saved as 0 or 1; 0 being false, and 1 being true
     */

    @Inject
    public QuestionTrueFalseCtrl(MainCtrl mainCtrl, QuestionFrameCtrl questionFrameCtrl, List<Activity> activities,
                                 String question, int correctAnswer) {
        super(activities, question, correctAnswer);
        this.mainCtrl = mainCtrl;
        this.questionFrameCtrl = questionFrameCtrl;
    }

    /**
     * sets the text of the question
     * sets the image using the imagePath
     * sets the text of the buttons
     * hides the correct solution
     *
     * @param question - contains all the data of the question extracted from the database
     */
    @Override
    public void initialize(Question question) {
        questionOutput.setText(this.getQuestion());
        String imagePath = getActivities().get(0).imagePath;
        Image image = new Image(imagePath, 399, 362, true, false);
        imageOutput.setImage(image);
        setTrueAnswer();
        setFalseAnswer();
        correctTrue.setVisible(false);
        correctFalse.setVisible(false);
        wrongTrue.setVisible(false);
        wrongFalse.setVisible(false);
    }

    /**
     * sets the text true on the true button
     */
    @FXML
    public void setTrueAnswer() {
        trueAnswer.setText("True");
    }

    /**
     * sets the text false on the false button
     */
    @FXML
    public void setFalseAnswer() {
        falseAnswer.setText("False");
    }

    /**
     * if the true button is selected, the false one gets reduced opacity
     * if the answer is correct there are added points
     * in the end the correct answer is revealed using revealCorrectAnswer
     *
     * @param event - clicking the true button
     */
    @FXML
    void trueSelected(ActionEvent event) {
        falseAnswer.setOpacity(0.7);
        if (getCorrectAnswer() == 0) {
            mainCtrl.addPoints(100);
        }
        revealCorrectAnswer();
    }

    /**
     * if the false button is selected, the true one gets reduced opacity
     * if the answer is correct there are added points
     * in the end the correct answe is revealed using revealCorrectAnswer
     *
     * @param event - clicking the false button
     */
    @FXML
    void falseSelected(ActionEvent event) {
        trueAnswer.setOpacity(0.7);
        if (getCorrectAnswer() == 1) {
            mainCtrl.addPoints(100);
        }
        revealCorrectAnswer();
    }

    /**
     * reveals the ticks and crosses according to the registered correct answer
     */
    @Override
    public void revealCorrectAnswer() {
        if (this.getCorrectAnswer() == 0) {
            correctTrue.setVisible(true);
            wrongFalse.setVisible(true);
        } else {
            correctTrue.setVisible(true);
            wrongFalse.setVisible(true);
        }
    }
}
