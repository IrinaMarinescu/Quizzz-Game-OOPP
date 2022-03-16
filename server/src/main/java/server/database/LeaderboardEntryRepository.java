package server.database;

import commons.LeaderboardEntry;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LeaderboardEntryRepository extends JpaRepository<LeaderboardEntry, Long> {

    /**
     * Returns the specified number of top performers in the game.
     *
     * @param limit the number of entries to be retrieved.
     * @return a list containing the specified number of leaderboard entries, ordered by score
     */
    @Query(value = "SELECT * FROM leaderboard_entry ORDER BY score DESC LIMIT :limit", nativeQuery = true)
    List<LeaderboardEntry> findTopPlayers(int limit);

    List<LeaderboardEntry> findByName(String name);

}
