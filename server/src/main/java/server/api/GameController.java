package server.api;

import commons.Game;
import commons.Lobby;
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

    @GetMapping("/validate")
    public String validateConnection() {
        return "Connected";
    }

    @GetMapping("/multiplayer/start")
    public Game startMultiplayerGame() {
        Lobby lobby = lobbyController.getLobby();
        Game newGame = new Game(lobby.getId(), activityController.generateQuestions(), lobby.getPlayers());
        lobbyController.resetLobby();
        games.put(lobby.getId(), newGame);
        return newGame;
    }

    @GetMapping("/singleplayer/start")
    public Game startSingleplayer() {
        return new Game(UUID.randomUUID(), activityController.generateQuestions(), new ArrayList<>());
    }

}
