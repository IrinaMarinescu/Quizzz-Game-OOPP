package commons;

import java.util.List;

public abstract class Question {

    private List<Activity> activities;
    private String question;
    private int correctAnswer;

    public Question(List<Activity> activities, String question, int correctAnswer) {
        this.activities = activities;
        this.question = question;
        this.correctAnswer = correctAnswer;
    }
}
