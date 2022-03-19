package server.api;

import commons.Game;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller creates API endpoints for activities.
 */
@RestController
@RequestMapping("/api/game")
public class GameController {

    private Map<UUID, Game> games;
    public UUID receivingGameId = UUID.randomUUID();

    private final ActivityController activityController;
    private final LobbyController lobbyController;
    private final LongPollingController longPollingController;

    public GameController(ActivityController activityController,
                          LobbyController lobbyController,
                          LongPollingController longPollingController) {

        games = new HashMap<>();
        this.activityController = activityController;
        this.lobbyController = lobbyController;
        this.longPollingController = longPollingController;
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
        dispatch(newGame.getId());
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

    /**
     * This is where front-end sends a request which gets stored
     *
     * @param gameId The id of the game to which data will be returned to
     * @return A JSON string corresponding to the result
     */
    @GetMapping(path = {"/{gameId}"})
    synchronized ResponseEntity<Game> receivePoll(@PathVariable UUID gameId) {
        try {
            do {
                wait();
            } while (!gameId.equals(receivingGameId));
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.err.println("Thread that was paused due to long polling on server got interrupted");
        }
        return ResponseEntity.ok(games.get(receivingGameId));
    }

    /**
     * This must be called by server-side methods to send data to the client.
     * This generates a JSON Lobby and sends it to all connected players.
     *
     * @param receivingGameId The ID of the game to which the data must be sent to
     */

    final synchronized void dispatch(UUID receivingGameId) {
        this.receivingGameId = receivingGameId;
        notifyAll();
    }
    @PostMapping("/sendEmote/{gameID}")
    public void sendNewEmoteToAll(@PathVariable UUID gameId, @RequestBody String username,
                                  @RequestBody String typeReaction) {
        longPollingController.dispatch(gameId, "EMOJI", Pair.of("name", username), Pair.of("reaction", typeReaction));
    }

    @GetMapping(path = "/halveTime/{gameId}")
    public void halveTimeToAll(@PathVariable UUID gameId) {
        longPollingController.dispatch(gameId, "HALVE_TIME");
    }


}
