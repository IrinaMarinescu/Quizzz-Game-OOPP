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

public class QuestionThreePicturesCtrl extends Question implements QuestionRequirements {

    private MainCtrl mainCtrl;
    private QuestionFrameCtrl questionFrameCtrl;

    @FXML
    Button answerA;

    @FXML
    Button answerB;

    @FXML
    Button answerC;

    @FXML
    TextField questionOutput;

    @FXML
    ImageView imageA;

    @FXML
    ImageView imageB;

    @FXML
    ImageView imageC;

    @FXML
    ImageView correctA;

    @FXML
    ImageView wrongA;

    @FXML
    ImageView correctB;

    @FXML
    ImageView wrongB;

    @FXML
    ImageView correctC;

    @FXML
    ImageView wrongC;

    @Inject
    public QuestionThreePicturesCtrl(List<Activity> activities, String question, int correctAnswer, MainCtrl mainCtrl,
                                     QuestionFrameCtrl questionFrameCtrl) {
        super(activities, question, correctAnswer);
        this.mainCtrl = mainCtrl;
        this.questionFrameCtrl = questionFrameCtrl;
    }

    /**
     * sets the text of the question
     * also calls the setAnswerFunctions and uses the data from the question
     * it hides the solution as well
     *
     * @param question - the question has all the data required from the database
     */
    @Override
    public void initialize(Question question) {
        questionOutput.setText(this.getQuestion());
        setAnswerA();
        setAnswerB();
        setAnswerC();
        correctA.setVisible(false);
        wrongA.setVisible(false);
        correctB.setVisible(false);
        wrongB.setVisible(false);
        correctC.setVisible(false);
        wrongC.setVisible(false);
    }

    /**
     * adds the title of the first activity set in question
     * adds the image based on the image path
     */
    @FXML
    public void setAnswerA() {
        String title = getActivities().get(0).title;
        String imagePath = getActivities().get(0).imagePath;
        Image image = new Image(imagePath, 200, 186, false, true);
        imageA.setImage(image);
        answerA.setText(title);
    }

    /**
     * adds the title of the second activity set in question
     * adds the image based on the image path
     */
    @FXML
    public void setAnswerB() {
        String title = getActivities().get(1).title;
        String imagePath = getActivities().get(1).imagePath;
        Image image = new Image(imagePath, 200, 186, false, true);
        imageB.setImage(image);
        answerB.setText(title);
    }

    /**
     * adds the title of the third activity set in question
     * adds the image based on the image path
     */
    @FXML
    public void setAnswerC() {
        String title = getActivities().get(2).title;
        String imagePath = getActivities().get(2).imagePath;
        Image image = new Image(imagePath, 200, 186, false, true);
        imageC.setImage(image);
        answerC.setText(title);
    }

    /**
     * if answer A is clicked, the other two buttons have a reduced opacity
     *
     * @param event - the click of a button, however i dont know if its properly declared
     */
    @FXML
    void answerASelected(ActionEvent event) {
        answerB.setOpacity(0.7);
        imageB.setOpacity(0.7);
        answerC.setOpacity(0.7);
        imageB.setOpacity(0.7);
        if (getCorrectAnswer() == 0) {
            mainCtrl.addPoints(100);
        }
        revealCorrectAnswer();
    }

    /**
     * if answer B is clicked, the other two buttons have a reduced opacity
     *
     * @param event - the click of a button, however i dont know if its properly declared
     */
    @FXML
    void answerBSelected(ActionEvent event) {
        answerA.setOpacity(0.7);
        imageA.setOpacity(0.7);
        answerC.setOpacity(0.7);
        imageC.setOpacity(0.7);
        if (getCorrectAnswer() == 1) {
            mainCtrl.addPoints(100);
        }
        revealCorrectAnswer();
    }

    /**
     * if answer C is clicked, the other two buttons have a reduced opacity
     *
     * @param event - the click of a button, however i dont know if its properly declared
     */
    @FXML
    void answerCSelected(ActionEvent event) {
        answerB.setOpacity(0.7);
        imageB.setOpacity(0.7);
        answerA.setOpacity(0.7);
        imageA.setOpacity(0.7);
        if (getCorrectAnswer() == 2) {
            mainCtrl.addPoints(100);
        }
        revealCorrectAnswer();
    }

    /**
     * reveals the correct answer by switching the visibility of the ticks and crosses
     */
    @Override
    public void revealCorrectAnswer() {
        switch (this.getCorrectAnswer()) {
            case 0:
                correctA.setVisible(true);
                wrongB.setVisible(true);
                break;
            case 1:
                correctB.setVisible(true);
                wrongA.setVisible(true);
                wrongC.setVisible(true);
                break;
            case 2:
                correctC.setVisible(true);
                wrongA.setVisible(true);
                wrongB.setVisible(true);
                break;
            default:
                //we should maybe set an error message here
                break;
        }
    }


}
