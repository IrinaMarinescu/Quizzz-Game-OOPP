package server.database;

import commons.Activity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    String retrievalQuery = "SELECT * FROM activity "
        + "WHERE consumption_in_wh >= :lowerBound "
        + "AND consumption_in_wh <= :upperBound "
        + "ORDER BY random() "
        + "LIMIT :limit";

    /**
     * Find an activity by its title in the database. The Spring Data JPA takes care of the implementation.
     *
     * @param title - the title you are looking for in the DB
     * @return an Activity entity with the specified title, or null if one activity doesn't exist.
     * @see <a href="https://www.baeldung.com/spring-data-derived-queries">Spring Data Derived Queries</a>
     */
    Activity findByTitle(String title);

    /**
     * Returns specified number of random entries from the activity table.
     *
     * @param limit the number of entries
     * @return some randomly chosen entries from the activity table.
     * @see <a href="https://www.baeldung.com/spring-data-jpa-query">Spring Data JPA @Query</a>
     */
    @Query(value = retrievalQuery, nativeQuery = true)
    List<Activity> fetchRandomActivities(int limit, long lowerBound, long upperBound);

    Activity findById(String id);

    void deleteById(String id);

    long count();
}
