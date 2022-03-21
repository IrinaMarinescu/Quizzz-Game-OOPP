package server.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import commons.Activity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * The @DataJpaTest annotation helps in creating an in-memory environment in which to test database queries.
 *
 * @see <a href="https://www.bezkoder.com/spring-boot-unit-test-jpa-repo-datajpatest/">Spring Data Repository Unit Test</a>
 */
@DataJpaTest
class ActivityRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ActivityRepository repo;

    @BeforeEach
    void setUp() {
        entityManager.persist(new Activity("00-a", "ss/ss.png", "a", 5, "b"));
    }

    @Test
    public void testSaveNewActivity() {
        Activity activity = repo.findByTitle("a");
        assertEquals(activity.title, "a");
    }

    @Test
    public void testGetActivities() {
        entityManager.persist(new Activity("00-b", "ss/ss.png", "b", 6, "c"));

        List<Activity> act = repo.findAll();
        assertEquals(act.size(), 2);
    }

    @Test
    public void testUpdateActivity() {
        Activity activity = repo.findById("00-a");
        activity.consumptionInWh = 100L;

        repo.save(activity);

        Activity newActivity = repo.findById("00-a");
        assertEquals(activity.consumptionInWh, 100);
    }

    @Test
    public void testDeleteActivity() {
        Activity activity = repo.findById("00-a");

        repo.deleteById(activity.id);

        Activity notFound = repo.findById("00-a");
        assertNull(notFound);
    }
}