package server.dependedoncomponents;

import commons.Question;
import java.util.List;
import java.util.Random;
import server.api.ActivityController;
import server.database.ActivityRepository;

public class ActivityControllerDOC extends ActivityController {

    private final Question question;

    public ActivityControllerDOC(ActivityRepository repo, Random random, Question question) {
        super(repo, random, null);
        this.question = question;
    }

    @Override
    public List<Question> generateQuestions() {
        return List.of(question);
    }
}
