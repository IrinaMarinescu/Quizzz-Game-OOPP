package server.dependedoncomponents;

import commons.Question;
import java.util.List;
import java.util.Random;
import server.api.ActivityController;
import server.database.ActivityRepository;
import server.services.FileStorageService;

public class ActivityControllerDOC extends ActivityController {

    private final Question question;

    public ActivityControllerDOC(ActivityRepository repo, Random random, FileStorageService fileStorageService, Question question) {
        super(repo, random, fileStorageService);
        this.question = question;
    }

    @Override
    public List<Question> generateQuestions() {
        return List.of(question);
    }
}
