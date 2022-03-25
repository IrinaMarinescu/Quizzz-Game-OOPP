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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javax.inject.Inject;

public class QuestionOneImageCtrl implements QuestionRequirements {

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
    public QuestionOneImageCtrl(MainCtrl mainCtrl, QuestionFrameCtrl questionFrameCtrl) {
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
        if (boxes.get(selectedAnswerButton).isDisabled()) {
            return;
        }

        buttons.get(selectedAnswerButton).setStyle("-fx-border-color: #028090");

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
        Platform.runLater(() -> {
            this.questionText.setText("How much does " + question.getActivities().get(0).title + " consume in Wh?");
        });
        long actualConsumption = question.getActivities().get(0).consumptionInWh;
        this.positionCorrectAnswer = (new Random()).nextInt(3);

        String imagePath = mainCtrl.getServerUtils().getServerIP() + "images/"
                + question.getActivities().get(0).imagePath;
        Image image = new Image(imagePath, 480, 500, true, false);
        imageField.setImage(image);

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

        String[] values = new String[3];
        values[positionCorrectAnswer] = String.valueOf(question.getActivities().get(0).consumptionInWh);

        int j = 1;
        for (int i = 0; i < 3; i++) {
            if (i != positionCorrectAnswer) {
                values[i] = String.valueOf(question.getActivities().get(j).consumptionInWh);
                j++;
            }
        }

        Platform.runLater(() -> {
            for (int i = 0; i < 3; i++) {
                buttons.get(i).setText(values[i]);
            }
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

    ;

    /**
     * Sets the question displayed in the scene
     *
     * @param question the question displayed in the scene
     */
    public void setQuestion(Question question) {
        this.question = question;
    }

    /**
     * Sets the position of the correct answer on the buttons
     *
     * @param positionCorrectAnswer the position of the correct answer, where 0 is button A, 1 is button B and 2
     *                              is button C
     */
    public void setPositionCorrectAnswer(int positionCorrectAnswer) {
        this.positionCorrectAnswer = positionCorrectAnswer;
    }

    /**
     * Returns the position of the correct answer on the buttons
     *
     * @return the position of the correct answer, where 0 is button A, 1 is button B and 2 is button C
     */
    public int getPositionCorrectAnswer() {
        return positionCorrectAnswer;
    }

    /**
     * Gets the button that the user selected
     *
     * @return the button that the user selected, where 0 is button A, 1 is button B and 2 is button C
     */
    public int getSelectedAnswerButton() {
        return selectedAnswerButton;
    }

    /**
     * Returns the question
     *
     * @return the Question displayed in the screen
     */
    public Question getQuestion() {
        return question;
    }

    /**
     * Returns the main controller of the question
     *
     * @return the main front-end controller
     */
    public MainCtrl getMainCtrl() {
        return mainCtrl;
    }

    public void keyPressed(KeyCode e) {
        switch (e) {
            case DIGIT1:
            case NUMPAD1:
            case A:
                setAnswerA();
                break;
            case DIGIT2:
            case NUMPAD2:
            case B:
                setAnswerB();
                break;
            case DIGIT3:
            case NUMPAD3:
            case C:
                setAnswerC();
                break;
            default:
                break;
        }
    }
}
