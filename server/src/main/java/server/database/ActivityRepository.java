package server.database;

import commons.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    Activity findByTitle(String title);

    @Query(value="SELECT * FROM activity ORDER BY random() LIMIT :limit", nativeQuery = true)
    List<Activity> fetchRandomActivities(int limit);
}
