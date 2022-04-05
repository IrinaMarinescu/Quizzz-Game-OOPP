package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the game class
 */
class GameTest {

    private Game sut;
    private UUID id;
    private Question question;
    private List<LeaderboardEntry> players;

    @BeforeEach
    void setUp() {
        id = new UUID(123, 444);
        Activity activity = new Activity("id", "abc/abc.png", "Hello world?", 123, "www.google.com");
        question = new Question(List.of(activity), "world", 0, "TrueFalse");
        players = List.of(new LeaderboardEntry("James", 10), new LeaderboardEntry("Michael", 3));
        sut = new Game(id, List.of(question), players);
    }

    @Test
    void constructor() {
        assertNotNull(sut);
    }

    @Test
    void getQuestions() {
        assertEquals(List.of(question), sut.getQuestions());
    }

    @Test
    void getId() {
        assertSame(id, sut.getId());
    }

    @Test
    void getPlayers() {
        assertSame(players, sut.getPlayers());
    }

    @Test
    void setPlayers() {
        assertSame(players, sut.getPlayers());
        sut.setPlayers(List.of(new LeaderboardEntry("Name", 123)));
        assertEquals(List.of(new LeaderboardEntry("Name", 123)), sut.getPlayers());
    }

    @Test
    void getRound() {
        assertSame(0, sut.getRound());
    }

    @Test
    void incrementRound() {
        assertSame(0, sut.getRound());
        sut.incrementRound();
        assertSame(1, sut.getRound());
        sut.incrementRound();
        sut.incrementRound();
        assertSame(3, sut.getRound());
    }

    @Test
    void getNextQuestionBeforeFirstRound() {
        assertThrows(IndexOutOfBoundsException.class, () -> sut.nextQuestion());
    }

    @Test
    void getNextQuestionFirstRound() {
        sut.incrementRound();
        assertSame(question, sut.nextQuestion());
    }

    @Test
    void updateScores() {
        assertSame(3, players.get(1).getScore());
        sut.updateScores(new LeaderboardEntry("Michael", 54));
        assertSame(54, players.get(1).getScore());
    }

    @Test
    void terminate() {
        assertNotNull(sut.getId());
        sut.terminate();
        assertNull(sut.getId());
    }

}