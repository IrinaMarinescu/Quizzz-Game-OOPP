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

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
