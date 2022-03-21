package commons;

import java.util.List;

public class Question {

    private List<Activity> activities;
    private String question;
    private int correctAnswer;
    private String questionType;

    /**
     * Empty constructor need to create an instance from JSON file
     */
    public Question() {
    }

    /**
     * Constructor
     *
     * @param activities    The list of activities that make up the question
     * @param question      The text of the question (E.g., "How much energy does this activity use?")
     * @param correctAnswer The index of the correct answer
     * @param questionType  The type of the question (E.g., "openQuestion")
     */
    public Question(List<Activity> activities, String question, int correctAnswer, String questionType) {
        this.activities = activities;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.questionType = questionType;
    }

    /**
     * @return The list of activities
     */
    public List<Activity> getActivities() {
        return activities;
    }

    /**
     * @return The text of the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Setter for question field
     *
     * @param question The updated text of the question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * @return The index of the correct answer
     */
    public int getCorrectAnswer() {
        return correctAnswer;
    }

    /**
     * @return The type of the question
     */
    public String getQuestionType() {
        return this.questionType;
    }
}
