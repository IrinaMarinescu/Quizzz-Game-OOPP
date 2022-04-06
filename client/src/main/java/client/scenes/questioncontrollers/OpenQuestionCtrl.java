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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javax.inject.Inject;

public class OpenQuestionCtrl implements QuestionRequirements {

    private MainCtrl mainCtrl;
    private QuestionFrameCtrl questionFrameCtrl;
    private Question question;
    private long answer;
    private static final int MISS_VALUE = 50;

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

    @FXML
    ImageView imageField;

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
            double correctAnswer = this.question.getActivities().get(0).consumptionInWh;
            double percentageOff = ((Math.abs(correctAnswer - (double) this.answer)) / correctAnswer) * 100;
            double baseScore = 100 - (percentageOff * (100 / MISS_VALUE));
            if (baseScore < 0) {
                baseScore = 0;
            }
            mainCtrl.addPoints((long) baseScore);
        } else {
            errorMessage.setVisible(true);
        }
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
            errorMessage.setVisible(false);
            entryField.setDisable(false);
            Platform.runLater(() -> entryField.requestFocus());
        });

        String imagePath = mainCtrl.getServerUtils().getServerIP() + "api/activities/image/"
            + question.getActivities().get(0).id;
        Image image = new Image(imagePath, 480, 500, true, false);
        imageField.setImage(image);
    }

    /**
     * Reveals the correct answer by displaying what the correct answer was and printing the points.
     * The points are calculates as percentages off from the correct answer, so if you are 1% off you get 99/100 points,
     * 2% off gives you 98/100 points and 100% off leaves you with 0 points.
     */
    @Override
    public void revealCorrectAnswer() {
        entryField.setDisable(true);
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

    /**
     * Returns the main controller of the screen
     *
     * @return the main front-end controller
     */
    public MainCtrl getMainCtrl() {
        return mainCtrl;
    }

    /**
     * Returns the question of the screen
     *
     * @return the Question that is shown in the screen
     */
    public Question getQuestion() {
        return question;
    }

    /**
     * Returns the answer to the question
     *
     * @return the correct consumption value as a long
     */
    public long getAnswer() {
        return answer;
    }

    /**
     * Returns the error message text
     *
     * @return the error message that is displayed when wrong input is entered
     */
    public Text getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the answer
     *
     * @param answer the correct consumption value
     */
    public void setAnswer(long answer) {
        this.answer = answer;
    }

    /**
     * Sets the question to be displayed in the screen
     *
     * @param question the Question that is shown in the screen
     */
    public void setQuestion(Question question) {
        this.question = question;
    }

    /**
     * Allows the user to use the enter button to submit the answer
     *
     * @param e the key that is pressed
     */
    public void keyPressed(KeyCode e) {
        if (e == KeyCode.ENTER) {
            submit();
        }
    }
}
