package client.scenes.questioncontrollers;

import client.scenes.MainCtrl;
import client.scenes.QuestionFrameCtrl;
import client.scenes.controllerrequirements.QuestionRequirements;
import commons.Question;
import java.util.List;
import java.util.stream.IntStream;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    Button trueAnswer;

    @FXML
    Button falseAnswer;

    @FXML
    TextField questionOutput;

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
        this.answers = List.of(trueAnswer, falseAnswer);
        this.correct = List.of(correctTrue, correctFalse);
        this.wrong = List.of(wrongTrue, wrongFalse);

        this.positionCorrectAnswer = question.getCorrectAnswer();
        this.questionOutput.setText(question.getQuestion());
        this.imageOutput.setImage(new Image(question.getActivities().get(0).imagePath, 200, 186, true, false));
        trueAnswer.setText("True");
        falseAnswer.setText("False");
        IntStream.range(0, correct.size()).forEach(i -> {
            correct.get(i).setVisible(false);
            wrong.get(i).setVisible(false);
            answers.get(i).setOpacity(1);
            answers.get(i).setStyle("-fx-border-color:  #5CB4BF");
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
        answers.get(selectedAnswerButton).setStyle("-fx-border-color: #028090");
        for (int i = 0; i < 3; i++) {
            answers.get(i).setOnAction(null);
            if (i != selectedAnswerButton) {
                answers.get(i).setOpacity(0.5);
            }
        }
    }

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

    @Override
    public void removeIncorrectAnswer() {
    }

    public Question getQuestion() {
        return question;
    }

    public List<Button> getAnswers() {
        return answers;
    }

    public int getPositionCorrectAnswer() {
        return positionCorrectAnswer;
    }

    public int getSelectedAnswerButton() {
        return selectedAnswerButton;
    }

    public Button getTrueAnswer() {
        return trueAnswer;
    }

    public Button getFalseAnswer() {
        return falseAnswer;
    }

    public TextField getQuestionOutput() {
        return questionOutput;
    }

    public ImageView getImageOutput() {
        return imageOutput;
    }

}
