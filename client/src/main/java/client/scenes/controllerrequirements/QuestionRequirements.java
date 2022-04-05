package client.scenes.controllerrequirements;

import commons.Question;
import javafx.scene.input.KeyCode;

public interface QuestionRequirements {

    /**
     * Initializes the question screen with the correct question and it resets the buttons and texts that need
     * to be resetted
     *
     * @param question the Question to be shown on the screen
     */
    void initialize(Question question);

    /**
     * Reveals the correct answer by revealing ticks and crosses
     */
    void revealCorrectAnswer();

    /**
     * Is called when the 'remove incorrect answer' joker is called. Removes one incorrect answer from the multiple
     * choice questions, but is disabled for the open answer questions and the true-false questions
     */
    void removeIncorrectAnswer();

    /**
     * Enables key pressing in addition to clicking
     *
     * @param e the key that is pressed
     */
    void keyPressed(KeyCode e);
}
