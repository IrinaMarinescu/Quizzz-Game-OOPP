package server.api;

import commons.Activity;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
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

    @Autowired
    private LongPollingController longPollingController;

    private final ActivityRepository repo;

    public ActivityController(ActivityRepository repo) {
        this.repo = repo;
    }

    private static boolean nullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @GetMapping(path = {"", "/"})
    public List<Activity> getAll() {
        return repo.findAll();
    }

    /**
     * FOR DEMONSTRATION OF HOW LONG POLLING WORKS
     */
    @GetMapping(path = {"test", "test/"})
    public void sendHelloEmojiToAll() {
        longPollingController.dispatch("EMOJI", Pair.of("name", "Per"), Pair.of("reaction", "happy"));
    }

    /**
     * FOR DEMONSTRATION OF HOW LONG POLLING WORKS
     */
    @GetMapping(path = {"test2", "test2/"})
    public void halveTime() {
        longPollingController.dispatch("JOKER", Pair.of("sort", "halveTime"));
    }

    /**
     * Uses the function defined {@link ActivityRepository} to fetch a number of random entries from the activity table.
     *
     * @param limit the number of entries
     * @return the specified amount of activities, randomly chosen from the DB;
     * returns fewer entries if limit is greater than the number of total activities.
     */
    @GetMapping("random/{limit}")
    public List<Activity> fetchRandom(@PathVariable("limit") int limit) {
        return repo.fetchRandomActivities(limit);
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

        //return ResponseEntity.ok(activity);
        Activity saved = repo.save(activity);
        return ResponseEntity.ok(saved);
    }
}
