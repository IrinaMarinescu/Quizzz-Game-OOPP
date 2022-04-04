package server.api;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import commons.Activity;
import commons.LeaderboardEntry;
import commons.Question;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for long polling functions on server side
 */
public class LobbyControllerTest {

    private LobbyController sut;

    @BeforeEach
    void setup() {
        sut = new LobbyController();
    }

    @Test
    void constructor() {
        assertNotNull(sut);
    }

    @Test
    void getLobby() {
        assertNotNull(sut.getLobby());
    }

    @Test
    void addPlayerToLobby() {
        assertFalse(sut.getLobby().isUsernameTaken("James"));
        sut.addPlayerToLobby(new LeaderboardEntry("James", 7));
        assertTrue(sut.getLobby().isUsernameTaken("James"));
    }

    @Test
    void removePlayerFromLobby() {
        assertFalse(sut.getLobby().isUsernameTaken("James"));
        sut.addPlayerToLobby(new LeaderboardEntry("James", 7));
        assertTrue(sut.getLobby().isUsernameTaken("James"));
        sut.removePlayerFromLobby(new LeaderboardEntry("James", 7));
        assertFalse(sut.getLobby().isUsernameTaken("James"));
    }

    @Test
    void createGame() {
        Activity activity = new Activity("id", "abc/abc.png", "Hello world?", 123, "www.google.com");
        Question question = new Question(List.of(activity), "world", 0, "TrueFalse");
        sut.addPlayerToLobby(new LeaderboardEntry("Per", 12));

        assertTrue(sut.getLobby().isUsernameTaken("Per"));
        sut.createGame(List.of(question));
        assertFalse(sut.getLobby().isUsernameTaken("Per"));
    }

    @Test
    void receivePoll() {
        UUID testId = UUID.randomUUID();
        CompletableFuture.delayedExecutor(10, TimeUnit.MILLISECONDS).execute(() -> {
            sut.dispatch(testId);
        });

        assertNotNull(sut.receivePoll(testId));
    }
}