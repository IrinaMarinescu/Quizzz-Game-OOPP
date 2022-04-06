package client.dependedoncomponents;

import client.scenes.MainCtrl;
import client.scenes.QuestionFrameCtrl;
import client.scenes.questioncontrollers.QuestionThreePicturesCtrl;
import commons.Question;
import java.util.ArrayList;
import java.util.List;

public class QuestionThreePicturesCtrlDOC extends QuestionThreePicturesCtrl {

    String questionText;
    String answerA;
    String answerB;
    String answerC;

    public QuestionThreePicturesCtrlDOC(MainCtrl mainCtrl, QuestionFrameCtrl questionFrameCtrl) {
        super(mainCtrl, questionFrameCtrl);
    }

    @Override
    protected void setChosenAnswer() {
    }

    /**
     * Overrides the initialize method from the InsteadOfQuestionCtrl by removing the buttons to allow for easier
     * testing
     *
     * @param question the given question with the activity that this is about
     */
    @Override
    public void initialize(Question question) {
        super.setQuestion(question);
        this.questionText = "Which one consumes the most?";
        super.setPositionCorrectAnswer(question.getCorrectAnswer());
        String correctAnswer = question.getActivities().get(getPositionCorrectAnswer()).title;

        answerA = "";
        answerB = "";
        answerC = "";
        List<String> buttons = new ArrayList<>();
        buttons.add(answerA);
        buttons.add(answerB);
        buttons.add(answerC);

        answerA = question.getActivities().get(0).title;
        answerB = question.getActivities().get(1).title;
        answerC = question.getActivities().get(2).title;
    }

    /**
     * Gets the question text
     *
     * @return the String with the text of the question
     */
    public String getQuestionText() {
        return questionText;
    }
}
