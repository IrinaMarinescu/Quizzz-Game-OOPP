package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import commons.Activity;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import server.database.ActivityRepository;

/**
 * The @DataJpaTest annotation helps in creating an in-memory environment in which to test database queries.
 *
 * @see <a href="https://www.bezkoder.com/spring-boot-unit-test-jpa-repo-datajpatest/">Spring Data Repository Unit Test</a>
 */
@DataJpaTest
class ActivityControllerTest {

    @Autowired
    private ActivityRepository repo;

    private ActivityController controller;

    @BeforeEach
    public void setUp() {
        controller = new ActivityController(repo);
    }

    @Test
    public void cannotAddNullTitleOrSource() {
        var s = controller.add(new Activity(null, null, null, 0, null));
        assertEquals(BAD_REQUEST, s.getStatusCode());
    }

    @Test
    public void testAdd() {
        var s = controller.add(new Activity("00-a", "ss/ss.png", "a", 5, "b"));
        Activity activity = repo.findById("00-a");

        assertEquals(activity.consumptionInWh, 5);
    }

    @Test
    public void testImport() {
        var s = controller.importActivities(Arrays.asList(new Activity("00-a", "ss/ss.png", "a", 5, "b"), new Activity("00-b", "ss/ss.png", "a", 10, "b")));
        Activity a1 = repo.findById("00-a");
        Activity a2 = repo.findById("00-b");

        assertEquals(a1.consumptionInWh, 5);
        assertEquals(a2.consumptionInWh, 10);
    }

    @Test
    public void testImportInvalidData() {
        var s = controller.importActivities(
            Arrays.asList(
                new Activity(null, null, null, 0, null),
                new Activity("00-b", "ss/ss.png", "a", 10, "b"))
        );

        assertEquals(repo.findById("00-b"), null);
    }

}