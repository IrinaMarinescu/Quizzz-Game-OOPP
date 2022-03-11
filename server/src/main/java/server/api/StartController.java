package server.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.ActivityRepository;

/**
 * This controller creates API endpoints for activities.
 */
@RestController
@RequestMapping("/api/start")
public class StartController {


    public StartController(ActivityRepository repo) {
    }

    private static boolean nullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @GetMapping("/validate")
    public String validateConnection() {
        return "Connected";
    }


}
