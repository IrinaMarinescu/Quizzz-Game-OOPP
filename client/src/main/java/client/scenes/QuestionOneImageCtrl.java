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

public class QuestionOneImageCtrl implements QuestionRequirements {

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
        this.questionText.setText("How much does " + question.getActivities().get(0).title + " consume in Wh?");
        int actualConsumption = question.getActivities().get(0).consumptionInWh;
        this.positionCorrectAnswer = (new Random()).nextInt(3);

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

        //while loop to prevent multiple answer options from being the same number
        while (answerA.getText().equals(answerB.getText())
                || answerC.getText().equals(answerB.getText())
                || answerC.getText().equals(answerA.getText())) {
            buttons.get(positionCorrectAnswer).setText(String.valueOf(actualConsumption));
            for (int i = 0; i < 3; i++) {
                if (i != positionCorrectAnswer) {
                    buttons.get(i).setText(randomConsumption());
                }
            }
        }
    }

    /**
     * Generates a random consumption value within a 15% range of the consumption of the correct answer
     *
     * @return returns a String with the random value, so that it can be displayed in the buttons
     */
    private String randomConsumption() {
        int actualConsumption = this.question.getActivities().get(0).consumptionInWh;
        int zeros = countZeros(actualConsumption);
        double fifteenPercent = actualConsumption / 100.00 * 15.00;
        int max = (int) Math.ceil(actualConsumption + fifteenPercent);
        int min = (int) Math.floor(actualConsumption - fifteenPercent);
        int randomConsumption = (int) Math.floor(Math.random() * (max - min + 1) + min);
        //rounding the number to the appropriate number of zeroes at the end to make it harder to guess
        randomConsumption = (int) (randomConsumption / (Math.pow(10, zeros) * Math.pow(10, zeros)));
        return String.valueOf(randomConsumption);
    }

    /**
     * Counts the number of zeros at the end of the consumption to correctly round the options on the other two buttons
     *
     * @param actualConsumption The consumption of the activity for which the number of zeroes has to be counted
     * @return The number of zeroes in the consumption
     */
    public static int countZeros(int actualConsumption) {
        String number = String.valueOf(actualConsumption);
        int counter = 0;
        for (int i = 0; i < number.length(); i++) {
            if (i + 1 == number.length() || number.charAt(i + 1) == '0') {
                if (number.charAt(i) == '0') {
                    counter++;
                }
            } else if (i + 1 != number.length() && number.charAt(i + 1) != '0') {
                counter = 0;
            }
        }
        return counter;
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

    ;
}