package server.api;

import commons.LeaderboardEntry;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.LeaderboardEntryRepository;

/**
 * This controller provides the logic behind solo leaderboard API endpoints.
 * It should only support two operations:
 * <ul>
 *     <li>adding a new entry in the leaderboard</li>
 *     <li>fetching the top N entries in the leaderboard</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final LeaderboardEntryRepository repo;

    public LeaderboardController(LeaderboardEntryRepository repo) {
        this.repo = repo;
    }

    private static boolean nullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    /**
     * Uses the function defined in {@link LeaderboardEntryRepository} to fetch the top performers from the leaderboard.
     *
     * @param limit the number of entries to fetch
     * @return a list containing the specified number of leaderboard entries, ordered by score
     */
    @GetMapping("/{limit}")
    public List<LeaderboardEntry> fetchTopPerformers(@PathVariable("limit") int limit) {
        return repo.findTopPlayers(limit);
    }

    /**
     * Takes a JSON-encoded LeaderboardEntry and stores it in the database.
     *
     * @param entry the entry to insert in the database
     * @return a response object, containing the saved entity.
     */
    @PostMapping("add")
    public ResponseEntity<LeaderboardEntry> add(@RequestBody LeaderboardEntry entry) {
        if (nullOrEmpty(entry.getName())) {
            return ResponseEntity.badRequest().build();
        }

        LeaderboardEntry saved = repo.save(entry);
        return ResponseEntity.ok(saved);
    }
}
