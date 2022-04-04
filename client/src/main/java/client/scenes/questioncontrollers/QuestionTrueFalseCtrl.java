package client.scenes.questioncontrollers;

import client.scenes.MainCtrl;
import client.scenes.QuestionFrameCtrl;
import client.scenes.controllerrequirements.QuestionRequirements;
import commons.Question;
import java.util.List;
import java.util.stream.IntStream;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javax.inject.Inject;


public class QuestionTrueFalseCtrl implements QuestionRequirements {

    private MainCtrl mainCtrl;
    private QuestionFrameCtrl questionFrameCtrl;
    private Question question;
    private List<Button> answers;
    private List<ImageView> wrong;
    private List<ImageView> correct;
    private int positionCorrectAnswer;
    private int selectedAnswerButton;

    @FXML
    Button trueButton;

    @FXML
    Button falseButton;

    @FXML
    Text questionOutput;

    @FXML
    ImageView imageOutput;

    @FXML
    ImageView correctTrue;

    @FXML
    ImageView wrongTrue;

    @FXML
    ImageView correctFalse;

    @FXML
    ImageView wrongFalse;

    @Inject
    public QuestionTrueFalseCtrl(MainCtrl mainCtrl, QuestionFrameCtrl questionFrameCtrl) {
        this.mainCtrl = mainCtrl;
        this.questionFrameCtrl = questionFrameCtrl;
    }

    @Override
    public void initialize(Question question) {
        this.question = question;
        this.answers = List.of(trueButton, falseButton);
        this.correct = List.of(correctTrue, correctFalse);
        this.wrong = List.of(wrongTrue, wrongFalse);

        this.positionCorrectAnswer = question.getCorrectAnswer();

        Platform.runLater(() -> {
            this.questionOutput.setText(question.getQuestion());
            String imagePath = mainCtrl.getServerUtils().getServerIP() + "api/activities/image/"
                + question.getActivities().get(0).id;
            Image image = new Image(imagePath, 400, 400, true, false);
            imageOutput.setImage(image);
            trueButton.setText("True");
            falseButton.setText("False");

            IntStream.range(0, correct.size()).forEach(i -> {
                correct.get(i).setVisible(false);
                wrong.get(i).setVisible(false);
                answers.get(i).setOpacity(1);
                answers.get(i).setDisable(false);
            });
        });
    }

    @FXML
    void trueSelected() {
        this.selectedAnswerButton = 0;
        setChosenAnswer();
    }

    @FXML
    void falseSelected() {
        this.selectedAnswerButton = 1;
        setChosenAnswer();
    }

    private void setChosenAnswer() {
        for (int i = 0; i < 2; i++) {
            answers.get(i).setDisable(true);
            if (i != selectedAnswerButton) {
                answers.get(i).setOpacity(0.5);
            }
        }

        if (selectedAnswerButton == positionCorrectAnswer) {
            mainCtrl.addPoints(100);
        } else {
            mainCtrl.addPoints(0);
        }
    }

    @Override
    public void revealCorrectAnswer() {
        correct.get(positionCorrectAnswer).setVisible(true);
        for (int i = 0; i < 2; i++) {
            answers.get(i).setDisable(true);
            if (i != positionCorrectAnswer) {
                wrong.get(i).setVisible(true);
            }
        }
    }

    @Override
    public void removeIncorrectAnswer() {
    }

    public Question getQuestion() {
        return question;
    }

    public void keyPressed(KeyCode e) {
        switch (e) {
            case DIGIT1:
            case NUMPAD1:
            case T:
                trueSelected();
                break;
            case DIGIT2:
            case NUMPAD2:
            case F:
                falseSelected();
                break;
            default:
                break;
        }
    }
}
