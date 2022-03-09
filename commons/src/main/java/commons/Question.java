package commons;

import java.util.List;

public class Question {

    private List<Activity> activities;
    private String question;
    private int correctAnswer;
    private String questionType;

    public Question(List<Activity> activities, String question, int correctAnswer) {
        this.activities = activities;
        this.question = question;
        this.correctAnswer = correctAnswer;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public String getQuestion() {
        return question;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public String getQuestionType() {
        return questionType;
    }
}
