package client.scenes;

import client.scenes.controllerrequirements.QuestionRequirements;
import commons.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.inject.Inject;

public class QuestionOneImage implements QuestionRequirements {

    private MainCtrl mainCtrl;
    private QuestionFrameCtrl questionFrameCtrl;
    private Question question;
    private char correctAnswerButton;

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

    /**
     * Injects necessary dependencies
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

    /**
     * Initializes the given question by setting the question text, the image and the answer buttons
     * @param question - the given question with the activity that this is about
     */
    @Override
    public void initialize(Question question) {
        this.question = question;
        this.questionText.setText("How much does " + question.getActivities().get(0).getTitle() + " consume in Wh?");
        int actualConsumption = question.getActivities().get(0).getConsumptionInWh();
        int positionCorrectAnswer = (int) Math.floor(Math.random()*3);

        String imagePath = question.getActivities().get(0).imagePath;
        Image image = new Image(imagePath, 480, 500, false, true);
        imageField.setImage(image);

        //while loop to prevent multiple answer options from being the same number
        while (answerA.getText().equals(answerB.getText()) || answerC.getText().equals(answerB.getText()) ||
                answerC.equals(answerA.getText())) {
            if (positionCorrectAnswer == 0) {
                this.correctAnswerButton = 'a';
                answerA.setText(String.valueOf(actualConsumption));
                answerB.setText(randomConsumption());
                answerC.setText(randomConsumption());
            } else if (positionCorrectAnswer == 1) {
                this.correctAnswerButton = 'b';
                answerB.setText(String.valueOf(actualConsumption));
                answerA.setText(randomConsumption());
                answerC.setText(randomConsumption());
            } else {
                this.correctAnswerButton = 'c';
                answerC.setText(String.valueOf(actualConsumption));
                answerB.setText(randomConsumption());
                answerA.setText(randomConsumption());
            }
        }
    }

    /**
     * Generates a random consumption value within a 15% range of the consumption of the correct answer
     * @return returns a String with the random value, so that it can be displayed in the buttons
     */
    private String randomConsumption() {
        int actualConsumption = this.question.getActivities().get(0).getConsumptionInWh();
        int fifteenPercent = actualConsumption/100*15;
        int max = actualConsumption+fifteenPercent;
        int min = actualConsumption-fifteenPercent;
        int randomConsumption = (int) Math.floor(Math.random()*(max-min+1)+min);
        return String.valueOf(randomConsumption);
    }

    @Override
    public void revealCorrectAnswer() {

    }

    ;
}
