package server.api;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import commons.Activity;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import server.database.ActivityRepository;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

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
        var s = controller.add(new Activity(null, 0, null));
        assertEquals(BAD_REQUEST, s.getStatusCode());
    }

    @Test
    public void testAdd() {
        var s = controller.add(new Activity("a", 5, "b"));
        Activity activity = repo.findByTitle("a");

        assertEquals(activity.consumptionInWh, 5);
    }

}