package server.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commons.Game;
import commons.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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

    private final Map<UUID, Game> games;

    private final ObjectMapper mapper = new ObjectMapper();
    private String json;
    private UUID receivingGameId;
    private UUID receivingFeaturesId;

    private final ActivityController activityController;
    private final LobbyController lobbyController;

    /**
     * Set up an empty game map, activityController and lobbyController, so it's possible to call methods from there
     *
     * @param activityController ActivityController object
     * @param lobbyController    LobbyController object
     */
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
     */
    @GetMapping("/multiplayer/start")
    public void startMultiplayerGame() {
        Game newGame = lobbyController.createGame(activityController.generateQuestions());
        games.put(newGame.getId(), newGame);
        dispatchGame(newGame.getId());
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
    synchronized ResponseEntity<Game> receiveGamePoll(@PathVariable UUID gameId) {
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
     * This is where front-end sends a request which gets stored
     *
     * @param gameId The id of the game to which data will be returned to
     * @return A JSON string corresponding to the result
     */
    @GetMapping(path = {"/features/{gameId}"})
    synchronized ResponseEntity<String> receiveFeaturesPoll(@PathVariable UUID gameId) {
        try {
            do {
                wait();
            } while (!gameId.equals(receivingFeaturesId));
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.err.println("Thread that was paused due to long polling on server got interrupted");
        }
        return ResponseEntity.ok(json);
    }

    /**
     * This must be called by server-side methods to send data to the client.
     * This generates a JSON Game and sends it to all connected players.
     *
     * @param receivingGameId The ID of the game to which the data must be sent to
     */
    final synchronized void dispatchGame(UUID receivingGameId) {
        this.receivingGameId = receivingGameId;
        notifyAll();
    }

    /**
     * This must be called by server-side methods to send data to the client.
     * This generates a JSON with features that changed and sends it to all connected players.
     *
     * @param receivingFeaturesId The ID of the game to which the data must be sent to
     */
    final synchronized void dispatchFeatures(UUID receivingFeaturesId, String type, Pair<String, String> keyValuePair) {
        this.receivingFeaturesId = receivingFeaturesId;
        ObjectNode res = mapper.createObjectNode();
        res.put("type", type);
        res.put("name", keyValuePair.getKey());
        res.put("value", keyValuePair.getValue());

        try {
            json = mapper.writeValueAsString(res);
            notifyAll();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.err.println("Error at Long polling JSON parsing on server");
        }
    }

    @PostMapping("/send/{type}/{gameId}")
    public void sendEmote(@PathVariable UUID gameId, @PathVariable String type,
                          @RequestBody Pair<String, String> keyValuePair) {
        dispatchFeatures(gameId, type, keyValuePair);
    }
}
