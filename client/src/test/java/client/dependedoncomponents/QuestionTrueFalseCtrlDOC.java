package client.dependedoncomponents;

import client.scenes.MainCtrl;
import client.scenes.QuestionFrameCtrl;
import client.scenes.questioncontrollers.QuestionTrueFalseCtrl;
import commons.Question;
import java.util.ArrayList;
import java.util.List;

public class QuestionTrueFalseCtrlDOC extends QuestionTrueFalseCtrl {

    String questionText;
    String trueAnswer;
    String falseAnswer;

    public QuestionTrueFalseCtrlDOC(MainCtrl mainCtrl, QuestionFrameCtrl questionFrameCtrl) {
        super(mainCtrl, questionFrameCtrl);
    }

    @Override
    protected void setChosenAnswer() {
    }

    @Override
    public void initialize(Question question) {
        super.setQuestion(question);
        ;
        this.questionText =
            question.getActivities().get(0).title + " consumes more than " + question.getActivities().get(1).title;
        super.setPositionCorrectAnswer(question.getCorrectAnswer());
        String correctAnswer = question.getActivities().get(getPositionCorrectAnswer()).title;

        trueAnswer = "";
        falseAnswer = "";

        List<String> buttons = new ArrayList<>();
        buttons.add(trueAnswer);
        buttons.add(falseAnswer);

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
