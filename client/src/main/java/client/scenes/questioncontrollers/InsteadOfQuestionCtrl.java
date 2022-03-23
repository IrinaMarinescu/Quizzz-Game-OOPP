package client.scenes.questioncontrollers;

import client.scenes.MainCtrl;
import client.scenes.QuestionFrameCtrl;
import client.scenes.controllerrequirements.QuestionRequirements;
import commons.Question;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javax.inject.Inject;

public class InsteadOfQuestionCtrl implements QuestionRequirements {

    private MainCtrl mainCtrl;
    private QuestionFrameCtrl questionFrameCtrl;
    private Question question;
    private List<Text> buttons;
    private List<ImageView> correct;
    private List<ImageView> wrong;
    private List<VBox> boxes;
    private int positionCorrectAnswer;
    private int selectedAnswerButton;

    @FXML
    Text answerA;

    @FXML
    Text answerB;

    @FXML
    Text answerC;

    @FXML
    Text questionText;

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

    @FXML
    VBox boxA;

    @FXML
    VBox boxB;

    @FXML
    VBox boxC;

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
        for (int i = 0; i < 3; i++) {
            boxes.get(i).setDisable(true);
            if (i != selectedAnswerButton) {
                boxes.get(i).setOpacity(0.5);
            }
        }

        if (selectedAnswerButton == positionCorrectAnswer) {
            mainCtrl.addPoints(100);
        } else {
            mainCtrl.addPoints(0);
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
        Platform.runLater(() -> this.questionText.setText(question.getQuestion()));
        this.positionCorrectAnswer = question.getCorrectAnswer();
        String correctAnswer = question.getActivities().get(positionCorrectAnswer).title;

        //String imagePath = question.getActivities().get(0).imagePath;
        //Image image = new Image(imagePath, 480, 500, false, true);
        //imageField.setImage(image);

        this.buttons = new ArrayList<>();
        Collections.addAll(buttons, answerA, answerB, answerC);

        this.correct = new ArrayList<>();
        Collections.addAll(correct, correctA, correctB, correctC);

        this.wrong = new ArrayList<>();
        Collections.addAll(wrong, wrongA, wrongB, wrongC);

        this.boxes = new ArrayList<>();
        Collections.addAll(boxes, boxA, boxB, boxC);

        for (int i = 0; i < 3; i++) {
            correct.get(i).setVisible(false);
            wrong.get(i).setVisible(false);
            boxes.get(i).setOpacity(1);
            boxes.get(i).setDisable(false);
        }

        Platform.runLater(() -> {
            answerA.setText(question.getActivities().get(0).title);
            answerB.setText(question.getActivities().get(1).title);
            answerC.setText(question.getActivities().get(2).title);
        });
    }

    /**
     * Reveals ticks and crosses to indicate correct and wrong answers and displays points gained
     */
    @Override
    public void revealCorrectAnswer() {
        correct.get(positionCorrectAnswer).setVisible(true);
        for (int i = 0; i < 3; i++) {
            boxes.get(i).setDisable(true);
            if (i != positionCorrectAnswer) {
                wrong.get(i).setVisible(true);
            }
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

        boxes.get(removedAnswer).setOpacity(0.5);
        boxes.get(removedAnswer).setDisable(true);
    }
}
