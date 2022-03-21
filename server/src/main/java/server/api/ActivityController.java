package server.api;

import commons.Activity;
import commons.Question;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import javax.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.ActivityRepository;

/**
 * This controller creates API endpoints for activities.
 */
@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    public static final double SIMILARITY_FACTOR = 1.5;
    private long totalRecords;

    private final ActivityRepository repo;
    private final Random rand;

    public ActivityController(ActivityRepository repo, Random random) {
        this.repo = repo;
        this.rand = random;
        this.totalRecords = this.repo.count();
    }

    private static boolean nullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @GetMapping(path = {"", "/"})
    public List<Activity> getAll() {
        return repo.findAll();
    }

    /**
     * Uses the function defined {@link ActivityRepository} to fetch a number of random entries from the activity table.
     * <p>
     * <p>
     * The consumptionInWh values for the retrieved activities should be similar:
     * Let X be an arbitrary activity.
     * All retrieved activities Y will satisfy
     * <p>
     * X.consumptionInWh / SIMILARITY_FACTOR <= Y.consumptionInWh <= X.consumptionInWh * SIMILARITY_FACTOR
     * <p>
     * Note that while SIMILARITY_FACTOR is constant, the resultant bounds will be gradually relaxed
     * until sufficiently many results are found.
     * This may happen when the initial selected activity has extreme consumptionInWh (E.g., largest in the DB).
     * <p>
     *
     * @param limit the number of entries
     * @return the specified amount of activities, randomly chosen from the DB;
     * returns fewer entries if limit is greater than the number of total activities.
     */
    @GetMapping("random/{limit}")
    public List<Activity> fetchRandom(@PathVariable("limit") int limit) {
        long initialConsumption = repo.fetchRandomActivities(1, 0, Long.MAX_VALUE).get(0).consumptionInWh;
        double lowerBound = initialConsumption;
        double upperBound = initialConsumption;

        limit = (int) Math.min(limit, totalRecords);
        List<Activity> res;
        do {
            lowerBound /= SIMILARITY_FACTOR;
            upperBound *= SIMILARITY_FACTOR;
            res = repo.fetchRandomActivities(limit, (long) lowerBound, (long) upperBound);
        } while (res.size() < limit);

        return res;
    }

    /**
     * Used to delete an activity from the DB, by ID
     *
     * @param activity the activity to delete
     * @return a bad request error, if an activity does not exist, or the deleted activity otherwise
     */
    @PostMapping("del")
    @Transactional
    public ResponseEntity<Activity> deleteActivity(@RequestBody Activity activity) {
        Activity candidate = repo.findById(activity.id);
        if (candidate == null) {
            return ResponseEntity.badRequest().build();
        }

        repo.deleteById(activity.id);
        return ResponseEntity.ok(candidate);
    }


    /**
     * Gets an activity object and updates in accordingly in the database.
     *
     * @param activity the activity object to update in the DB.
     * @return the same object, if the operation is successful.
     */
    @PostMapping("update")
    public ResponseEntity<Activity> updateActivity(@RequestBody Activity activity) {
        repo.save(activity);
        return ResponseEntity.ok(activity);
    }

    /**
     * This function will get a json-encoded Activity and will save it in the DB.
     * We can test this endpoint with Postman until we have a working UI.
     *
     * @param activity the activity to be inserted in the DB
     * @return a response containing the data of the inserted entry.
     */
    @PostMapping("add")
    public ResponseEntity<Activity> add(@RequestBody Activity activity) {
        if (nullOrEmpty(activity.source) || nullOrEmpty(activity.title) || nullOrEmpty(activity.id)
            || nullOrEmpty(activity.imagePath)) {
            return ResponseEntity.badRequest().build();
        }

        totalRecords++;
        Activity saved = repo.save(activity);
        return ResponseEntity.ok(saved);
    }

    /**
     * This function gets a list of activities to add to the database in bulk.
     *
     * @param activities the list of the activities to add
     * @return the same list, if the operation is successful.
     */
    @PostMapping("import")
    public ResponseEntity<List<Activity>> importActivities(@RequestBody List<Activity> activities) {
        for (var activity : activities) {
            if (nullOrEmpty(activity.source) || nullOrEmpty(activity.title) || nullOrEmpty(activity.id)
                || nullOrEmpty(activity.imagePath)) {
                return ResponseEntity.badRequest().build();
            }
        }

        List<Activity> saved = new ArrayList<>();
        for (var activity : activities) {
            saved.add(repo.save(activity));
        }

        totalRecords = repo.count();
        return ResponseEntity.ok(saved);
    }


    /**
     * generates 20 questions of random types
     * based on the random number generated between 0 and 4, a different specific method is called for
     * each type of question
     * 0 - true false
     * 1 - open question
     * 2 - three pictures question
     * 3 - one image question
     * 4 - instead of question
     *
     * @return a list of all the questions for one game
     */
    @GetMapping("generate")
    public List<Question> generateQuestions() {
        List<Question> questions = new ArrayList<>();
        Random rand = new Random();
        int upperBound = 5;
        for (int i = 0; i < 20; i++) {
            int typeOfQuestion = rand.nextInt(upperBound);
            switch (typeOfQuestion) {
                case 0:
                    generateTrueFalseQuestion(typeOfQuestion, questions);
                    break;
                case 1:
                    generateOpenQuestion(typeOfQuestion, questions);
                    break;
                case 2:
                    generateThreePicturesQuestion(typeOfQuestion, questions);
                    break;
                case 3:
                    generateOneImageQuestion(typeOfQuestion, questions);
                    break;
                case 4:
                    generateInsteadOfQuestion(typeOfQuestion, questions);
                    break;
                default:
                    break;
            }
        }
        return questions;
    }

    /**
     * used for the questionType/id attribute
     * associates a randomly generated question from the method generateQuestions to a descriptive string
     *
     * @param number - the randomly generated number
     * @return - a string with the question string identifier
     */
    public String associateQuestion(int number) {
        switch (number) {
            case 0:
                return "trueFalseQuestion";
            case 1:
                return "openQuestion";
            case 2:
                return "threePicturesQuestion";
            case 3:
                return "oneImageQuestion";
            case 4:
                return "insteadOfQuestion";
            default:
                break;
        }
        return null;
    }

    /**
     * randomizes the number of activities taken from the database
     * if there are two activities taken, a question with the string "x consumes more than y" is created
     * otherwise a question with the string "X consumes number" is created where the number is
     * "randomized" to be true or false
     * the question is created with all the according parameters and added to the list
     *
     * @param typeOfQuestion - the randomly generated number;
     * @param questions      - the list of all the questions;
     */
    public void generateTrueFalseQuestion(int typeOfQuestion, List<Question> questions) {
        String id = associateQuestion(typeOfQuestion);
        int numberOfActivities = rand.nextInt(2) + 1;
        List<Activity> activities = fetchRandom(numberOfActivities);
        String question;
        int correctAnswer = 0;

        if (numberOfActivities == 1) {
            long correctNumber = activities.get(0).consumptionInWh;
            long wrongNumber = correctNumber * 3;
            if (wrongNumber % 2 == 0) {
                question = activities.get(0).title + " consumes " + wrongNumber + "Wh.";
                correctAnswer = 1;
            } else {
                question = activities.get(0).title + " consumes " + correctNumber + "Wh.";
            }
        } else {
            question = activities.get(0).title + " consumes more than " + activities.get(1).title + ".";
            if (activities.get(0).consumptionInWh < activities.get(1).consumptionInWh) {
                correctAnswer = 1;
            }
        }

        Question trueFalseQuestion = new Question(activities, question, correctAnswer, id);
        questions.add(trueFalseQuestion);
    }

    /**
     * Generates an open question by fetching one random activity
     *
     * @param typeOfQuestion the randomly generated number
     * @param questions      the list of all the questions
     */
    public void generateOpenQuestion(int typeOfQuestion, List<Question> questions) {
        String id = associateQuestion(typeOfQuestion);
        List<Activity> activities = fetchRandom(1);
        String question = "How much energy in Wh does " + activities.get(0).title + " consume?";
        Question openQuestion = new Question(activities, question, 0, id);
        questions.add(openQuestion);
    }

    /**
     * takes 3 randomized activities from the database
     * the string of the question will be "Which activity consumes more?"
     * the correct answer is the one with the highest wh consumption
     * the question is created with all the according parameters and added to the list
     *
     * @param typeOfQuestion - the randomly generated number
     * @param questions      - the list of all the questions
     */
    public void generateThreePicturesQuestion(int typeOfQuestion, List<Question> questions) {
        String id = associateQuestion(typeOfQuestion);

        List<Activity> activities = fetchRandom(3);
        String question = "What consumes the most?";
        int correctAnswer = 0;
        for (int i = 1; i < 3; i++) {
            if (activities.get(correctAnswer).consumptionInWh < activities.get(i).consumptionInWh) {
                correctAnswer = i;
            }
        }
        Question threePicturesQuestion = new Question(activities, question, correctAnswer, id);
        questions.add(threePicturesQuestion);
    }

    /**
     * Generates a question with one image by fetching one random activity
     *
     * @param typeOfQuestion the randomly generated number
     * @param questions      the list of all the questions
     */
    public void generateOneImageQuestion(int typeOfQuestion, List<Question> questions) {
        String id = associateQuestion(typeOfQuestion);
        List<Activity> activities = fetchRandom(1);
        String question = "How much energy in Wh does " + activities.get(0).title + " consume?";
        Question questionOneImage = new Question(activities, question, 0, id);
        questions.add(questionOneImage);
    }

    /**
     * Generates an instead of question by fetching four random activities
     *
     * @param typeOfQuestion the randomly generated number
     * @param questions      the list of all the questions
     */
    public void generateInsteadOfQuestion(int typeOfQuestion, List<Question> questions) {
        List<Activity> activities = fetchRandom(4);
        int correctAnswer = 0;
        //Find the smallest consumption value
        for (int j = 1; j < 4; j++) {
            if (activities.get(j).consumptionInWh < activities.get(correctAnswer).consumptionInWh) {
                correctAnswer = j;
            }
        }

        // Find the second-smallest consumption value
        int i = 0;
        if (i == correctAnswer) {
            i = 1;
        }
        for (int j = 1; j < 4; j++) {
            if (j != correctAnswer
                && activities.get(j).consumptionInWh < activities.get(i).consumptionInWh) {
                i = j;
            }
        }
        String id = associateQuestion(typeOfQuestion);
        String question = "What can you do instead of " + activities.get(i).title + "?";
        activities.remove(i);
        if (correctAnswer > i) {
            correctAnswer--;
        }
        Question questionInsteadOf = new Question(activities, question, correctAnswer, id);
        questions.add(questionInsteadOf);
    }
}
