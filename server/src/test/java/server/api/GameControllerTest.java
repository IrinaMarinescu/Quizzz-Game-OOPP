package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import commons.Activity;
import commons.Game;
import commons.Pair;
import commons.Question;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.ResponseEntity;
import server.database.ActivityRepository;
import server.dependedoncomponents.ActivityControllerDOC;
import server.dependedoncomponents.LobbyCtrlDOC;
import server.dependedoncomponents.RandomDOC;

@DataJpaTest
public class GameControllerTest {

    @Autowired
    private ActivityRepository repo;

    private GameController sut;
    private ActivityControllerDOC activityControllerDOC;
    private LobbyCtrlDOC lobbyCtrlDOC;

    UUID testId = UUID.randomUUID();

    @BeforeEach
    void setup() {
        Activity activity = new Activity("id", "abc/abc.png", "Hello world?", 123, "www.google.com");
        Question question = new Question(List.of(activity), "world", 0, "TrueFalse");
        activityControllerDOC = new ActivityControllerDOC(repo, new RandomDOC(0), question);
        lobbyCtrlDOC = new LobbyCtrlDOC();

        sut = new GameController(activityControllerDOC, lobbyCtrlDOC);
    }

    @Test
    void constructor() {
        assertEquals(activityControllerDOC, sut.activityController);
        assertTrue(sut.games instanceof HashMap);
    }

    @Test
    void validateConnection() {
        assertEquals("Connected", sut.validateConnection());
    }

    @Test
    void startSingleplayerGame() {
        Game game = sut.startSingleplayer();

        assertNotNull(game.getId());
        assertEquals("world", game.getQuestions().get(0).getQuestion());
    }

    @Test
    void startMultiplayerGame() {
        assertNull(sut.games.get(lobbyCtrlDOC.testId));
        sut.startMultiplayerGame();
        assertSame(1, sut.games.size());
        assertNotNull(sut.games.get(lobbyCtrlDOC.testId));
    }

    @Test
    void receiveGamePoll() {
        CompletableFuture.delayedExecutor(10, TimeUnit.MILLISECONDS).execute(() -> {
            sut.dispatchFeatures(testId, "TEST_TYPE", new Pair<>("key", "value"));
        });

        ResponseEntity<String> res = sut.receiveFeaturesPoll(testId);
        assertEquals(ResponseEntity.ok("{\"type\":\"TEST_TYPE\",\"name\":\"key\",\"value\":\"value\"}"), res);
    }

    @Test
    void receiveGamePollSendEmote() {
        CompletableFuture.delayedExecutor(10, TimeUnit.MILLISECONDS).execute(() -> {
            sut.sendEmote(testId, "TEST_TYPE", new Pair<>("key", "value"));
        });

        ResponseEntity<String> res = sut.receiveFeaturesPoll(testId);
        assertEquals(ResponseEntity.ok("{\"type\":\"TEST_TYPE\",\"name\":\"key\",\"value\":\"value\"}"), res);
    }

    @Test
    void waitUntilDispatchCalled() {
        AtomicBoolean noDispatchYet = new AtomicBoolean(true);
        CompletableFuture.delayedExecutor(10, TimeUnit.MILLISECONDS).execute(() -> {
            sut.dispatchFeatures(testId, "TEST_TYPE", new Pair<>("key", "value"));
            noDispatchYet.set(false);
        });

        sut.receiveFeaturesPoll(testId);
        // Getting to this point is only possible if the thread is unfrozen
        if (noDispatchYet.get()) {
            fail();
        }
    }

    @Test
    void receivingGameIdMismatch() {
        UUID otherId = UUID.randomUUID();
        AtomicReference<UUID> senderId = new AtomicReference<>();

        CompletableFuture.delayedExecutor(10, TimeUnit.MILLISECONDS).execute(() -> {
            sut.dispatchFeatures(otherId, "TEST_TYPE", new Pair<>("key", "value"));
            senderId.set(otherId);
        });
        CompletableFuture.delayedExecutor(20, TimeUnit.MILLISECONDS).execute(() -> {
            sut.dispatchFeatures(testId, "TEST_TYPE", new Pair<>("key", "value"));
            senderId.set(testId);
        });

        sut.receiveFeaturesPoll(testId);
        // The assertion is only reached when the delayedExecutor with matching gameId dispatches
        assertEquals(testId, senderId.get());
    }

}
