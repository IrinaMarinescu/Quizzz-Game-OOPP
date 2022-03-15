package server.api;

import commons.Game;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller creates API endpoints for activities.
 */
@RestController
@RequestMapping("/api/game")
public class GameController {

    private Map<UUID, Game> games;
    private final ActivityController activityController;
    private final LobbyController lobbyController;

    public GameController(ActivityController activityController, LobbyController lobbyController) {
        games = new HashMap<>();
        this.activityController = activityController;
        this.lobbyController = lobbyController;
    }

    /**
     * Return a String, used to check if the client successfully connected with the server
     *
     * @return String "Connected"
     */
    @GetMapping("/validate")
    public String validateConnection() {
        return "Connected";
    }

    /**
     * Create a new Game with the players that are currently in the lobby and randomly generated list of questions
     *
     * @return Game object with unique id, list of 20 questions and list of players
     */
    @GetMapping("/multiplayer/start")
    public Game startMultiplayerGame() {
        Game newGame = lobbyController.createGame(activityController.generateQuestions());
        games.put(newGame.getId(), newGame);
        return newGame;
    }

    /**
     * Create a new Game with randomly generated list of questions
     *
     * @return Game object with unique id, list of 20 questions and empty list of players
     */
    @GetMapping("/singleplayer/start")
    public Game startSingleplayer() {
        return new Game(UUID.randomUUID(), activityController.generateQuestions(), new ArrayList<>());
    }

}
