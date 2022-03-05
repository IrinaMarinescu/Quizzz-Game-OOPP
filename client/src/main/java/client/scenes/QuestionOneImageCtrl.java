package client.scenes;

import client.scenes.controllerrequirements.QuestionRequirements;
import commons.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javax.inject.Inject;

public class QuestionOneImageCtrl implements QuestionRequirements {

    private MainCtrl mainCtrl;
    private QuestionFrameCtrl questionFrameCtrl;
    private Question question;
    private char correctAnswerButton;
    private char selectedAnswer;

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

    @FXML
    Text pointsField;

    /**
     * Injects necessary dependencies
     *
     * @param mainCtrl          - the main front-end controller
     * @param questionFrameCtrl - the scene into which it has to be injected
     */
    @Inject
    public QuestionOneImageCtrl(MainCtrl mainCtrl, QuestionFrameCtrl questionFrameCtrl) {
        this.mainCtrl = mainCtrl;
        this.questionFrameCtrl = questionFrameCtrl;
    }

    /**
     * Sets the selected answer as 'a' and pales and disables the other two buttons
     */
    @FXML
    public void setAnswerA() {
        this.selectedAnswer = 'A';
        answerA.setStyle("-fx-border-color: #028090");
        answerB.setOnAction(null);
        answerB.setOpacity(0.5);
        answerC.setOnAction(null);
        answerC.setOpacity(0.5);
    }

    ;
    /**
     * Sets the selected answer as 'b' and pales and disables the other two buttons
     */
    @FXML
    public void setAnswerB() {
        this.selectedAnswer = 'B';
        answerB.setStyle("-fx-border-color: #028090");
        answerA.setOnAction(null);
        answerA.setOpacity(0.5);
        answerC.setOnAction(null);
        answerC.setOpacity(0.5);
    }

    ;
    /**
     * Sets the selected answer as 'c' and pales and disables the other two buttons
     */
    @FXML
    public void setAnswerC() {
        this.selectedAnswer = 'C';
        answerC.setStyle("-fx-border-color: #028090");
        answerA.setOnAction(null);
        answerA.setOpacity(0.5);
        answerB.setOnAction(null);
        answerB.setOpacity(0.5);
    }

    /**
     * Initializes the given question by setting the question text, the image and the answer buttons
     *
     * @param question - the given question with the activity that this is about
     */
    @Override
    public void initialize(Question question) {
        this.question = question;
        this.questionText.setText("How much does " + question.getActivities().get(0).getTitle() + " consume in Wh?");
        int actualConsumption = question.getActivities().get(0).getConsumptionInWh();
        int positionCorrectAnswer = (int) Math.floor(Math.random() * 3);

        String imagePath = question.getActivities().get(0).imagePath;
        Image image = new Image(imagePath, 480, 500, false, true);
        imageField.setImage(image);

        //while loop to prevent multiple answer options from being the same number
        while (answerA.getText().equals(answerB.getText())
                || answerC.getText().equals(answerB.getText())
                || answerC.getText().equals(answerA.getText())) {
            if (positionCorrectAnswer == 0) {
                this.correctAnswerButton = 'A';
                answerA.setText(String.valueOf(actualConsumption));
                answerB.setText(randomConsumption());
                answerC.setText(randomConsumption());
            } else if (positionCorrectAnswer == 1) {
                this.correctAnswerButton = 'B';
                answerB.setText(String.valueOf(actualConsumption));
                answerA.setText(randomConsumption());
                answerC.setText(randomConsumption());
            } else {
                this.correctAnswerButton = 'C';
                answerC.setText(String.valueOf(actualConsumption));
                answerB.setText(randomConsumption());
                answerA.setText(randomConsumption());
            }
        }
    }

    /**
     * Generates a random consumption value within a 15% range of the consumption of the correct answer
     *
     * @return returns a String with the random value, so that it can be displayed in the buttons
     */
    private String randomConsumption() {
        int actualConsumption = this.question.getActivities().get(0).getConsumptionInWh();
        double fifteenPercent = actualConsumption / 100.00 * 15.00;
        int max = (int) Math.ceil(actualConsumption + fifteenPercent);
        int min = (int) Math.floor(actualConsumption - fifteenPercent);
        int randomConsumption = (int) Math.floor(Math.random() * (max - min + 1) + min);
        return String.valueOf(randomConsumption);
    }

    /**
     * Reveals ticks and crosses to indicate correct and wrong answers and displays points gained
     */
    @Override
    public void revealCorrectAnswer() {
        if (correctAnswerButton == 'A') {
            correctA.setVisible(true);
            wrongB.setVisible(true);
            wrongC.setVisible(true);
        } else if (correctAnswerButton == 'B') {
            correctB.setVisible(true);
            wrongC.setVisible(true);
            wrongA.setVisible(true);
        } else {
            correctC.setVisible(true);
            wrongA.setVisible(true);
            wrongB.setVisible(true);
        }

        if (selectedAnswer == correctAnswerButton) {
            mainCtrl.addPoints(100);
            //Should the addPoints method in MainCtrl return something that can be displayed here?
            pointsField.setText("");
        } else {
            mainCtrl.addPoints(0);
            pointsField.setText("+0 Points");
        }
    }

    ;
}
