package server.api;

import commons.Activity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ActivityRepository;

import java.util.List;

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

    @GetMapping("random/{limit}")
    public List<Activity> fetchRandom(@PathVariable("limit") int limit) {
        return repo.fetchRandomActivities(limit);
    }

    @PostMapping("add")
    public ResponseEntity<Activity> add(@RequestBody Activity activity) {
        if(nullOrEmpty(activity.source) || nullOrEmpty(activity.title))
            return ResponseEntity.badRequest().build();

        Activity saved = repo.save(activity);
        return ResponseEntity.ok(saved);
    }
}
