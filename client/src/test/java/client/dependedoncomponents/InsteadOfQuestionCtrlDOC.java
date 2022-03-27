package client.dependedoncomponents;

import client.scenes.MainCtrl;
import client.scenes.QuestionFrameCtrl;
import client.scenes.questioncontrollers.InsteadOfQuestionCtrl;
import commons.Question;
import java.util.ArrayList;
import java.util.List;

public class InsteadOfQuestionCtrlDOC extends InsteadOfQuestionCtrl {

    String questionText;
    String answerA;
    String answerB;
    String answerC;

    /**
     * Injects necessary dependencies
     *
     * @param mainCtrl          the main front-end controller
     * @param questionFrameCtrl the scene into which it has to be injected
     */
    public InsteadOfQuestionCtrlDOC(MainCtrl mainCtrl, QuestionFrameCtrl questionFrameCtrl) {
        super(mainCtrl, questionFrameCtrl);
    }

    /**
     * Overrides the initialize method from the InsteadOfQuestionCtrl by removing the buttons to allow for easier
     * testing
     *
     * @param question the given question with the activity that this is about
     */
    @Override
    public void initialize(Question question) {
        super.setQuestion(question);;
        this.questionText = "Instead of " + question.getActivities().get(0).title + ", what could you do?";
        super.setPositionCorrectAnswer(question.getCorrectAnswer());
        String correctAnswer = question.getActivities().get(getPositionCorrectAnswer()).title;

        answerA = "";
        answerB = "";
        answerC = "";
        List<String> buttons = new ArrayList<>();
        buttons.add(answerA);
        buttons.add(answerB);
        buttons.add(answerC);

        answerA = question.getActivities().get(1).title;
        answerB = question.getActivities().get(2).title;
        answerC = question.getActivities().get(3).title;
    }

    /**
     * Overrides setChosenAnswer() from QuestionOneImageCtrl and empties it to allow for easier testing
     */
    @Override
    protected void setChosenAnswer() {
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
