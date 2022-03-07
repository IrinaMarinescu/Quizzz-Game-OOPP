package server.database;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import commons.LeaderboardEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class LeaderboardEntryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LeaderboardEntryRepository repo;

    @BeforeEach
    void setUp() {
        entityManager.persist(new LeaderboardEntry("NoobMaster69", 69420));
    }

    @Test
    public void testSaveNewEntry() {
        LeaderboardEntry entry = repo.findByName("NoobMaster69").get(0);
        assertEquals(entry.getScore(), 69420);
    }

    @Test
    public void shouldSupportNameAndScoreDuplicates() {
        entityManager.persist(new LeaderboardEntry("NoobMaster69", 69420));
        var list = repo.findTopPlayers(5);
        assertEquals(list.get(0), list.get(1));
    }

    @Test
    public void testFindTopN() {
        entityManager.persist(new LeaderboardEntry("Thor", 694200));
        entityManager.persist(new LeaderboardEntry("Test", 10));

        var list = repo.findTopPlayers(5);

        assertAll("Records should be ordered by score in descending order.",
            () -> assertEquals("Thor", list.get(0).getName()),
            () -> assertEquals("NoobMaster69", list.get(1).getName()),
            () -> assertEquals("Test", list.get(2).getName())
        );
    }
}