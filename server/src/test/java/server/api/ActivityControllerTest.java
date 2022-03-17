package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import commons.Activity;
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
    private Activity activity;

    @BeforeEach
    public void setUp() {
        controller = new ActivityController(repo);
        activity = new Activity("00-a", "ss/ss.png", "a", 5, "b");
    }

    @Test
    public void cannotAddNullTitleOrSource() {
        var s = controller.add(new Activity(null, null, null, 0, null));
        assertEquals(BAD_REQUEST, s.getStatusCode());
    }

    @Test
    public void testAdd() {
        var s = controller.add(activity);
        Activity activity = repo.findById("00-a");

        assertEquals(activity.consumptionInWh, 5);
    }

    @Test
    public void testDeleteNotFound() {
        var s = controller.deleteActivity("00-x");
        assertEquals(BAD_REQUEST, s.getStatusCode());
    }

    @Test
    public void testDelete() {
        var s = controller.add(activity);
        controller.deleteActivity("00-a");
        assertEquals(activity, s.getBody());
    }
}