package client.dependedoncomponents;

import client.scenes.MainCtrl;
import client.scenes.QuestionFrameCtrl;
import client.scenes.questioncontrollers.QuestionOneImageCtrl;
import commons.Question;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is only used for testing. It simplifies some methods from QuestionOneImageCtrl and removes the buttons to
 * allow for easier testing.
 */
public class QuestionOneImageCtrlDOC extends QuestionOneImageCtrl {

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
    public QuestionOneImageCtrlDOC(MainCtrl mainCtrl, QuestionFrameCtrl questionFrameCtrl) {
        super(mainCtrl, questionFrameCtrl);
    }

    /**
     * Initializes the given question by setting the question text, the image and the answer buttons
     *
     * @param question the given question with the activity that this is about
     */
    @Override
    public void initialize(Question question) {
        super.setQuestion(question);
        this.questionText = "How much does " + question.getActivities().get(0).title + " consume in Wh?";
        long actualConsumption = question.getActivities().get(0).consumptionInWh;
        super.setPositionCorrectAnswer((new Random()).nextInt(3));

        answerA = "";
        answerB = "";
        answerC = "";
        List<String> buttons = new ArrayList<>();
        buttons.add(answerA);
        buttons.add(answerB);
        buttons.add(answerC);

        //while loop to prevent multiple answer options from being the same number
        while (answerA.equals(answerB)
                || answerC.equals(answerB)
                || answerC.equals(answerA)) {
            buttons.set(0, String.valueOf(actualConsumption));
            for (int i = 0; i < 3; i++) {
                if (i != super.getPositionCorrectAnswer()) {
                    buttons.set(i, randomConsumption());
                }
            }
            answerA = buttons.get(0);
            answerB = buttons.get(1);
            answerC = buttons.get(2);
        }
    }

    /**
     * Generates a random consumption value within a 15% range of the consumption of the correct answer
     *
     * @return returns a String with the random value, so that it can be displayed in the buttons
     */
    private String randomConsumption() {
        long actualConsumption = super.getQuestion().getActivities().get(0).consumptionInWh;
        int zeros = countZeros(actualConsumption);
        double fifteenPercent = ((double) actualConsumption) / 100.00 * 15.00;
        int max = (int) Math.ceil(actualConsumption + fifteenPercent);
        int min = (int) Math.floor(actualConsumption - fifteenPercent);
        int randomConsumption = (int) Math.floor(Math.random() * (max - min + 1) + min);
        //rounding the number to the appropriate number of zeroes at the end to make it harder to guess
        randomConsumption = (int) ((int) (randomConsumption / Math.pow(10, zeros - 1)) * Math.pow(10, zeros - 1));
        return String.valueOf(randomConsumption);
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
     * @return the String with the question, as set in the initialize() method
     */
    public String getQuestionText() {
        return questionText;
    }
}
