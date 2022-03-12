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

    private static boolean nullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    @GetMapping("/validate")
    public String validateConnection() {
        return "Connected";
    }

    @GetMapping("/multiplayer/start")
    public Game startMultiplayerGame() {
        UUID gameId = UUID.randomUUID();
        Game newGame = new Game(gameId, activityController.generateQuestions(), lobbyController.getLobby());
        lobbyController.resetLobby();
        games.put(gameId, newGame);
        return newGame;
    }

    @GetMapping("/singleplayer/start")
    public Game startSingleplayer() {
        return new Game(UUID.randomUUID(), activityController.generateQuestions(), new ArrayList<>());
    }

}
