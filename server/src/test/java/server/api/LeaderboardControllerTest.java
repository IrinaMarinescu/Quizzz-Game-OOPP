package server.api;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import commons.LeaderboardEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import server.database.LeaderboardEntryRepository;

@DataJpaTest
class LeaderboardControllerTest {

    @Autowired
    private LeaderboardEntryRepository repo;

    private LeaderboardController controller;

    @BeforeEach
    void setUp() {
        controller = new LeaderboardController(repo);
    }

    @Test
    public void cannotAddNullName() {
        var s = controller.add(new LeaderboardEntry(null, 0));
        assertEquals(BAD_REQUEST, s.getStatusCode());
    }

    @Test
    public void testAdd() {
        var s = controller.add(new LeaderboardEntry("test", 1));
        var entry = repo.findByName("test").get(0);

        assertEquals(1, entry.getScore());
    }

    @Test
    public void testFetchTop() {
        controller.add(new LeaderboardEntry("test", 1));
        controller.add(new LeaderboardEntry("test2", 5));
        controller.add(new LeaderboardEntry("test3", 3));

        var list = controller.fetchTopPerformers(5);
        assertAll("Top performers should be fetched ordered by score in descending order.",
            () -> assertEquals(5, list.get(0).getScore()),
            () -> assertEquals(3, list.get(1).getScore()),
            () -> assertEquals(1, list.get(2).getScore())
        );
    }
}