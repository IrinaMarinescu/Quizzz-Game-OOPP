package server.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import commons.Activity;
import server.database.ActivityRepository;

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

    @PostMapping("add")
    public ResponseEntity<Activity> add(@RequestBody Activity activity) {
        if(nullOrEmpty(activity.source) || nullOrEmpty(activity.title))
            return ResponseEntity.badRequest().build();

        Activity saved = repo.save(activity);
        return ResponseEntity.ok(saved);
    }

}
