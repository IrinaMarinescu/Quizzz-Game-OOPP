package client.scenes.controllerrequirements;

import commons.Question;

public interface QuestionRequirements {
    void initialize(Question question);

    void revealCorrectAnswer(Question question);
}
