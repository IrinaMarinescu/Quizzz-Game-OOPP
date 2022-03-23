package client.scenes.controllerrequirements;

import commons.Question;
import javafx.scene.input.KeyCode;

public interface QuestionRequirements {
    void initialize(Question question);

    void revealCorrectAnswer();

    void removeIncorrectAnswer();

    void keyPressed(KeyCode e);
}
