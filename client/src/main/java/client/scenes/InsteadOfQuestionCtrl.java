package client.scenes;

import client.scenes.controllerrequirements.QuestionRequirements;
import commons.Question;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.inject.Inject;

public class InsteadOfQuestionCtrl implements QuestionRequirements {
    private MainCtrl mainCtrl;
    private QuestionFrameCtrl questionFrameCtrl;
    private Question question;
    private List<Button> buttons;
    private List<ImageView> correct;
    private List<ImageView> wrong;
    private int positionCorrectAnswer;
    private int selectedAnswerButton;

    @FXML
    Button answerA;

    @FXML
    Button answerB;

    @FXML
    Button answerC;

    @FXML
    TextField questionText;

    @FXML
    ImageView imageField;

    @FXML
    ImageView correctA;

    @FXML
    ImageView correctB;

    @FXML
    ImageView correctC;

    @FXML
    ImageView wrongA;

    @FXML
    ImageView wrongB;

    @FXML
    ImageView wrongC;

    /**
     * Injects necessary dependencies
     *
     * @param mainCtrl          the main front-end controller
     * @param questionFrameCtrl the scene into which it has to be injected
     */
    @Inject
    public InsteadOfQuestionCtrl(MainCtrl mainCtrl, QuestionFrameCtrl questionFrameCtrl) {
        this.mainCtrl = mainCtrl;
        this.questionFrameCtrl = questionFrameCtrl;
    }

    /**
     * Sets the selected answer as '0'
     */
    @FXML
    public void setAnswerA() {
        this.selectedAnswerButton = 0;
        setChosenAnswer();
    }

    ;
    /**
     * Sets the selected answer as '1'
     */
    @FXML
    public void setAnswerB() {
        this.selectedAnswerButton = 1;
        setChosenAnswer();
    }

    ;
    /**
     * Sets the selected answer as '2'
     */
    @FXML
    public void setAnswerC() {
        this.selectedAnswerButton = 2;
        setChosenAnswer();
    }

    /**
     * Disables all buttons now that an answer has been chosen, makes the button of the chosen answer white and pales
     * the other answers
     */
    private void setChosenAnswer() {
        buttons.get(selectedAnswerButton).setStyle("-fx-border-color: #028090");
        for (int i = 0; i < 3; i++) {
            buttons.get(i).setDisable(true);
            if (i != selectedAnswerButton) {
                buttons.get(i).setOpacity(0.5);
            }
        }
    }

    /**
     * Initializes the given question by setting the question text, the image and the answer buttons
     *
     * @param question the given question with the activity that this is about
     */
    @Override
    public void initialize(Question question) {
        this.question = question;
        this.questionText.setText("Instead of " + question.getActivities().get(0).title + ", what could you do?");
        this.positionCorrectAnswer = question.getCorrectAnswer();
        String correctAnswer = question.getActivities().get(positionCorrectAnswer).title;

        String imagePath = question.getActivities().get(0).imagePath;
        Image image = new Image(imagePath, 480, 500, false, true);
        imageField.setImage(image);

        this.buttons = new ArrayList<>();
        Collections.addAll(buttons, answerA, answerB, answerC);

        this.correct = new ArrayList<>();
        Collections.addAll(correct, correctA, correctB, correctC);

        this.wrong = new ArrayList<>();
        Collections.addAll(wrong, wrongA, wrongB, wrongC);

        for (int i = 0; i < 3; i++) {
            correct.get(i).setVisible(false);
            wrong.get(i).setVisible(false);
            buttons.get(i).setOpacity(1);
            buttons.get(i).setStyle("-fx-border-color:  #5CB4BF");
            buttons.get(i).setDisable(false);
        }

        answerA.setText(question.getActivities().get(1).title);
        answerB.setText(question.getActivities().get(2).title);
        answerC.setText(question.getActivities().get(3).title);
    }

    /**
     * Reveals ticks and crosses to indicate correct and wrong answers and displays points gained
     */
    @Override
    public void revealCorrectAnswer() {
        correct.get(positionCorrectAnswer).setVisible(true);
        for (int i = 0; i < 3; i++) {
            if (i != positionCorrectAnswer) {
                wrong.get(i).setVisible(true);
            }
        }

        if (selectedAnswerButton == positionCorrectAnswer) {
            mainCtrl.addPoints(100);
        } else {
            mainCtrl.addPoints(0);
        }
    }

    /**
     * Removes one incorrect answer when that joker is chosen
     */
    @Override
    public void removeIncorrectAnswer() {
        int removedAnswer = new Random().nextInt(3);
        if (positionCorrectAnswer == removedAnswer) {
            switch (removedAnswer) {
                case 0:
                    removedAnswer = 1;
                    break;
                case 1:
                    removedAnswer = 2;
                    break;
                case 2:
                    removedAnswer = 0;
                    break;
                default:
                    break;
            }
        }

        buttons.get(removedAnswer).setOpacity(0.5);
        buttons.get(removedAnswer).setDisable(true);
    }
}
