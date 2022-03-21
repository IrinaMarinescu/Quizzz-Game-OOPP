package client.scenes.questioncontrollers;

import client.scenes.MainCtrl;
import client.scenes.QuestionFrameCtrl;
import client.scenes.controllerrequirements.QuestionRequirements;
import commons.Question;
import java.util.Scanner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javax.inject.Inject;

public class OpenQuestionCtrl implements QuestionRequirements {

    private MainCtrl mainCtrl;
    private QuestionFrameCtrl questionFrameCtrl;
    private Question question;
    private long answer;

    @FXML
    Button submitButton;

    @FXML
    Text questionField;

    @FXML
    Text answerText;

    @FXML
    TextField entryField;

    @FXML
    Text errorMessage;

    /**
     * Injects necessary dependencies
     *
     * @param mainCtrl          the main front-ent controller
     * @param questionFrameCtrl the scene into which it has to be injected
     */
    @Inject
    public OpenQuestionCtrl(MainCtrl mainCtrl, QuestionFrameCtrl questionFrameCtrl) {
        this.mainCtrl = mainCtrl;
        this.questionFrameCtrl = questionFrameCtrl;
    }


    /**
     * Reads the submitted answer and checks whether a number has actually been entered
     * Then changes the text on the button to 'Submitted!'
     */
    @FXML
    public void submit() {
        String ans = entryField.getText();
        Scanner scanner = new Scanner(ans);
        if (scanner.hasNextLong()) {
            this.answer = scanner.nextLong();
            errorMessage.setVisible(false);
            submitButton.setText("Submitted!");
            submitButton.setDisable(true);
        } else {
            errorMessage.setVisible(true);
        }

        long correctAnswer = this.question.getActivities().get(0).consumptionInWh;
        long percentageOff = ((Math.abs(correctAnswer - this.answer)) / correctAnswer) * 100;
        long baseScore = 100 - percentageOff / 2;
        if (baseScore < 0) {
            baseScore = 0;
        }
        mainCtrl.addPoints(baseScore);
    }

    /**
     * Initializes the question and sets the corresponding text as the question on screen
     *
     * @param question the activity that this question is about
     */
    @Override
    public void initialize(Question question) {
        this.question = question;
        Platform.runLater(() -> {
            questionField.setText(question.getQuestion());
            answerText.setText("");
            submitButton.setText("Submit");
            submitButton.setDisable(false);
            entryField.setText("");
        });

        //String imagePath = question.getActivities().get(0).imagePath;
        //Image image = new Image(imagePath, 480, 500, false, true);
        //imageField.setImage(image);
    }

    /**
     * Reveals the correct answer by displaying what the correct answer was and printing the points.
     * The points are calculates as percentages off from the correct answer, so if you are 1% off you get 99/100 points,
     * 2% off gives you 98/100 points and 100% off leaves you with 0 points.
     */
    @Override
    public void revealCorrectAnswer() {
        submitButton.setDisable(true);
        long correctAnswer = this.question.getActivities().get(0).consumptionInWh;
        answerText.setText("It takes " + correctAnswer + "Wh!");
    }

    /**
     * Disables the removeIncorrectAnswer joker
     */
    @Override
    public void removeIncorrectAnswer() {
        //Disable joker for this question
    }

    ;
}
