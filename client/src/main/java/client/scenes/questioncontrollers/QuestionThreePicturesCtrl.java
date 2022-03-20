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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javax.inject.Inject;

public class QuestionThreePicturesCtrl implements QuestionRequirements {

    private MainCtrl mainCtrl;
    private QuestionFrameCtrl questionFrameCtrl;
    private Question question;
    private List<Button> answers;
    private List<ImageView> images;
    private List<ImageView> wrong;
    private List<ImageView> correct;
    private int positionCorrectAnswer;
    private int selectedAnswerButton;

    @FXML
    Button answerA;

    @FXML
    Button answerB;

    @FXML
    Button answerC;

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

    @Inject
    public QuestionThreePicturesCtrl(MainCtrl mainCtrl, QuestionFrameCtrl questionFrameCtrl) {
        this.mainCtrl = mainCtrl;
        this.questionFrameCtrl = questionFrameCtrl;
    }

    @Override
    public void initialize(Question question) {
        this.question = question;
        this.answers = List.of(answerA, answerB, answerC);
        this.images = List.of(imageA, imageB, imageC);
        this.correct = List.of(correctA, correctB, correctC);
        this.wrong = List.of(wrongA, wrongB, wrongC);

        this.positionCorrectAnswer = question.getCorrectAnswer();
        this.questionOutput.setText(question.getQuestion());
        setAnswers(question);
        IntStream.range(0, correct.size()).forEach(i -> {
            correct.get(i).setVisible(false);
            wrong.get(i).setVisible(false);
            answers.get(i).setOpacity(1);
            answers.get(i).setStyle("-fx-border-color:  #5CB4BF");
            answers.get(i).setDisable(false);
        });
    }

    public void setAnswers(Question question) {
        Platform.runLater(() -> {
            for (int i = 0; i < question.getActivities().size(); i++) {
                String title = question.getActivities().get(i).title;
                //String imagePath = question.getActivities().get(i).imagePath;
                //Image image = new Image(imagePath, 200, 186, true, false);
                //images.get(i).setImage(image);
                answers.get(i).setText(title);
            }
        });
    }

    @FXML
    void answerASelected() {
        this.selectedAnswerButton = 0;
        setChosenAnswer();
    }

    @FXML
    void answerBSelected() {
        this.selectedAnswerButton = 1;
        setChosenAnswer();
    }

    @FXML
    void answerCSelected() {
        this.selectedAnswerButton = 2;
        setChosenAnswer();
    }

    private void setChosenAnswer() {
        answers.get(selectedAnswerButton).setStyle("-fx-border-color: #028090");
        for (int i = 0; i < 3; i++) {
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

    /**
     * reveals the correct answer by switching the visibility of the ticks and crosses
     */
    @Override
    public void revealCorrectAnswer() {
        correct.get(positionCorrectAnswer).setVisible(true);
        for (int i = 0; i < 3; i++) {
            if (i != positionCorrectAnswer) {
                wrong.get(i).setVisible(true);
            }
        }
    }

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
        answers.get(removedAnswer).setOpacity(0.5);
        images.get(removedAnswer).setOpacity(0.5);
    }

    public Question getQuestion() {
        return question;
    }

    public List<Button> getAnswers() {
        return answers;
    }

    public List<ImageView> getImages() {
        return images;
    }

    public int getPositionCorrectAnswer() {
        return positionCorrectAnswer;
    }

    public int getSelectedAnswerButton() {
        return selectedAnswerButton;
    }

    public Button getAnswerA() {
        return answerA;
    }

    public Button getAnswerB() {
        return answerB;
    }

    public Button getAnswerC() {
        return answerC;
    }

    public Label getQuestionOutput() {
        return questionOutput;
    }

    public ImageView getImageA() {
        return imageA;
    }

    public ImageView getImageB() {
        return imageB;
    }

    public ImageView getImageC() {
        return imageC;
    }

}
