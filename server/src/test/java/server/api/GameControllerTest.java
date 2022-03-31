package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import commons.Activity;
import commons.Game;
import commons.Question;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import server.database.ActivityRepository;
import server.dependedoncomponents.ActivityControllerDOC;
import server.dependedoncomponents.RandomDOC;
import server.services.FileStorageService;

@DataJpaTest
public class GameControllerTest {

    @Autowired
    private ActivityRepository repo;

    @MockBean
    private FileStorageService fileStorageService;

    private GameController sut;

    @BeforeEach
    void setup() {
        Activity activity = new Activity("id", "abc/abc.png", "Hello world?", 123, "www.google.com");
        Question question = new Question(List.of(activity), "world", 0, "TrueFalse");
        ActivityControllerDOC activityControllerDOC = new ActivityControllerDOC(repo, new RandomDOC(0), fileStorageService, question);

        sut = new GameController(activityControllerDOC, null, null);
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
}
