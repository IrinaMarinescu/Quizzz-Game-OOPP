package server.api;

import commons.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import server.database.ActivityRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The @DataJpaTest annotation helps in creating an in-memory environment in which to test database queries.
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
        entityManager.persist(new Activity("a", 5, "b"));
    }

    @Test
    public void testSaveNewActivity() {
        Activity activity = repo.findByTitle("a");
        assertEquals(activity.title, "a");
    }

    @Test
    public void testGetActivities() {
        entityManager.persist(new Activity("b", 6, "c"));

        List<Activity> act = (List<Activity>) repo.findAll();
        assertTrue(act.size() == 2);
    }

    @Test
    public void testUpdateActivity() {
        Activity activity = repo.findByTitle("a");
        activity.consumptionInWh = 100;

        repo.save(activity);

        Activity newActivity = repo.findByTitle("a");
        assertEquals(activity.consumptionInWh, 100);
    }

    @Test
    public void testDeleteActivity() {
        Activity activity = repo.findByTitle("a");

        repo.deleteById(activity.id);

        Activity notFound = repo.findByTitle("a");
        assertNull(notFound);
    }
}