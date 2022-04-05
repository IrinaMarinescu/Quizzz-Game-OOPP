package client.scenes.questioncontrollers;

import client.scenes.MainCtrl;
import client.scenes.QuestionFrameCtrl;
import client.scenes.controllerrequirements.QuestionRequirements;
import commons.Question;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javax.inject.Inject;

public class QuestionThreePicturesCtrl implements QuestionRequirements {

    private MainCtrl mainCtrl;
    private QuestionFrameCtrl questionFrameCtrl;
    private Question question;
    private List<Text> answers;
    private List<ImageView> images;
    private List<ImageView> wrong;
    private List<ImageView> correct;
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
    Label questionOutput;

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

    @FXML
    VBox boxA;

    @FXML
    VBox boxB;

    @FXML
    VBox boxC;

    /**
     * Injects the necessary dependencies
     *
     * @param mainCtrl          - the main front-end controller
     * @param questionFrameCtrl - the scene in which it has to be injected
     */
    @Inject
    public QuestionThreePicturesCtrl(MainCtrl mainCtrl, QuestionFrameCtrl questionFrameCtrl) {
        this.mainCtrl = mainCtrl;
        this.questionFrameCtrl = questionFrameCtrl;
    }

    /**
     * Groups the buttons and resets the visibility of the ticks and crosses
     * set the question text
     *
     * @param question - the Question to be shown on the screen
     */
    @Override
    public void initialize(Question question) {
        this.question = question;
        this.answers = List.of(answerA, answerB, answerC);
        this.images = List.of(imageA, imageB, imageC);
        this.correct = List.of(correctA, correctB, correctC);
        this.wrong = List.of(wrongA, wrongB, wrongC);
        this.boxes = List.of(boxA, boxB, boxC);

        this.positionCorrectAnswer = question.getCorrectAnswer();
        this.questionOutput.setText(question.getQuestion());
        setAnswers(question);
        IntStream.range(0, correct.size()).forEach(i -> {
            correct.get(i).setVisible(false);
            wrong.get(i).setVisible(false);
            boxes.get(i).setOpacity(1);
            boxes.get(i).setDisable(false);
        });
    }

    /**
     * Sets the text and the images of the buttons
     *
     * @param question - the Question to be shown on the screen
     */
    public void setAnswers(Question question) {
        Platform.runLater(() -> {
            for (int i = 0; i < question.getActivities().size(); i++) {
                String title = question.getActivities().get(i).title;
                placeImage(i);
                answers.get(i).setText(title);
            }
        });
    }

    /**
     * Sets the path of the activity images and adds the image to the answer
     *
     * @param i - the number of the activity
     */
    private void placeImage(int i) {
        String imagePath = mainCtrl.getServerUtils().getServerIP() + "api/activities/image/"
            + question.getActivities().get(i).id;
        Image image = new Image(imagePath, 480, 500, true, false);
        switch (i) {
            case 0:
                imageA.setImage(image);
                break;
            case 1:
                imageB.setImage(image);
                break;
            case 2:
                imageC.setImage(image);
                break;
            default:
                break;
        }
    }

    /**
     * Sets the chosen answer as A
     */
    @FXML
    void answerASelected() {
        this.selectedAnswerButton = 0;
        setChosenAnswer();
    }

    /**
     * Sets the chosen answer as B
     */
    @FXML
    void answerBSelected() {
        this.selectedAnswerButton = 1;
        setChosenAnswer();
    }

    /**
     * Sets the chosen answer as C
     */
    @FXML
    void answerCSelected() {
        this.selectedAnswerButton = 2;
        setChosenAnswer();
    }

    /**
     * Checks to see if the selected answer was correct or not and disables the buttons
     */
    protected void setChosenAnswer() {
        if (boxes.get(selectedAnswerButton).isDisabled()) {
            return;
        }
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
     * Reveals the correct answer by switching the visibility of the ticks and crosses
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
     * Method that removes on of the wrong answers when the removeIncorrectAnswer joker is used
     */
    @Override
    public void removeIncorrectAnswer() {
        int upperBound = 3;
        Random rand = new Random();
        int removedAnswer = rand.nextInt(upperBound);
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
    }

    /**
     * Getter of the question
     *
     * @return question - the current question
     */
    public Question getQuestion() {
        return question;
    }

    /**
     * Shortcuts
     *
     * @param e - the key that is pressed
     */
    public void keyPressed(KeyCode e) {
        switch (e) {
            case DIGIT1:
            case NUMPAD1:
            case A:
                answerASelected();
                break;
            case DIGIT2:
            case NUMPAD2:
            case B:
                answerBSelected();
                break;
            case DIGIT3:
            case NUMPAD3:
            case C:
                answerCSelected();
                break;
            default:
                break;
        }
    }

    /**
     * Returns the main controller of the question
     *
     * @return the main front-end controller
     */
    public MainCtrl getMainCtrl() {
        return mainCtrl;
    }

    /**
     * Returns the selected answer button
     *
     * @return the number of the button the user selected, where 0 is button A, 1 is button B and 2 is button C
     */
    public int getSelectedAnswerButton() {
        return selectedAnswerButton;
    }

    /**
     * Returns the position of the correct answer
     *
     * @return the number of the button with the answer, where 0 is button A, 1 is button B and 2 is button C
     */
    public int getPositionCorrectAnswer() {
        return positionCorrectAnswer;
    }

    /**
     * Sets the position of the correct answer
     *
     * @param positionCorrectAnswer the button with the correct answer, where 0 is button A,
     *                              1 is button B and 2 is button C
     */
    public void setPositionCorrectAnswer(int positionCorrectAnswer) {
        this.positionCorrectAnswer = positionCorrectAnswer;
    }

    /**
     * Sets the question displayed in the controller
     *
     * @param question the Question that should be shown in the screen
     */
    public void setQuestion(Question question) {
        this.question = question;
    }

}
