package server.api;

import commons.Activity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ActivityRepository;

import java.util.List;

/**
 * This controller creates API endpoints for activities.
 */
@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityRepository repo;

    public ActivityController(ActivityRepository repo) {
        this.repo = repo;
    }

    private static boolean nullOrEmpty(String s) { return s == null || s.isEmpty(); }

    @GetMapping(path = { "", "/" })
    public List<Activity> getAll() {
        return repo.findAll();
    }

    /**
     * Uses the function defined {@link ActivityRepository} to fetch a number of random entries from the activity table.
     * @param limit the number of entries
     * @return the specified amount of activities, randomly chosen from the DB; returns fewer entries if limit is greater than the number of total activities.
     */
    @GetMapping("random/{limit}")
    public List<Activity> fetchRandom(@PathVariable("limit") int limit) {
        return repo.fetchRandomActivities(limit);
    }

    /**
     * This function will get a json-encoded Activity and will save it in the DB.
     * We can test this endpoint with Postman until we have a working UI.
     * @param activity the activity to be inserted in the DB
     * @return a response containing the data of the inserted entry.
     */
    @PostMapping("add")
    public ResponseEntity<Activity> add(@RequestBody Activity activity) {
        if(nullOrEmpty(activity.source) || nullOrEmpty(activity.title) || nullOrEmpty(activity.id) || nullOrEmpty(activity.imagePath))
            return ResponseEntity.badRequest().build();

        //return ResponseEntity.ok(activity);
        Activity saved = repo.save(activity);
        return ResponseEntity.ok(saved);
    }
}