package client.dependedoncomponents;

import client.scenes.MainCtrl;
import client.scenes.QuestionFrameCtrl;
import client.scenes.questioncontrollers.OpenQuestionCtrl;
import commons.Question;
import java.util.Scanner;

/**
 * Testing the original OpenQuestionCtrl class is very difficult, so this class ensures that testing becomes a bit
 * easier by removing buttons and text field
 */
public class OpenQuestionCtrlDOC extends OpenQuestionCtrl {
    String entryAnswer;
    public String text;

    /**
     * Injects necessary dependencies
     *
     * @param mainCtrl          the main front-ent controller
     * @param questionFrameCtrl the scene into which it has to be injected
     */
    public OpenQuestionCtrlDOC(MainCtrl mainCtrl, QuestionFrameCtrl questionFrameCtrl) {
        super(mainCtrl, questionFrameCtrl);
    }

    /**
     * Initializes the question and sets the corresponding text as the question on screen,
     * like super but without buttons
     *
     * @param question the activity that this question is about
     */
    @Override
    public void initialize(Question question) {
        super.setQuestion(question);
        text = "How many Wh does " + question.getActivities().get(0).title + " take?";
    }

    /**
     * Reads the submitted answer and checks whether a number has actually been entered, but without the buttons and
     * the text field
     */
    public void submit() {
        String ans = entryAnswer;
        Scanner scanner = new Scanner(ans);
        if (scanner.hasNextInt()) {
            super.setAnswer(scanner.nextInt());
            super.getErrorMessage().setVisible(false);
        } else {
            super.getErrorMessage().setVisible(true);
        }
    }

    /**
     * Sets the String entryAnswer
     *
     * @param entryAnswer the answer that is to be injected in the answer field
     */
    public void setEntryAnswer(String entryAnswer) {
        this.entryAnswer = entryAnswer;
    }
}
