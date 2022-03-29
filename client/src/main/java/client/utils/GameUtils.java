package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import client.scenes.MainCtrl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Game;
import commons.LeaderboardEntry;
import commons.Pair;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import javafx.application.Platform;
import javax.inject.Inject;
import org.glassfish.jersey.client.ClientConfig;

public class GameUtils {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;

    private final ObjectMapper mapper = new ObjectMapper();
    public boolean activeGame;
    public boolean activeFeatures;

    /**
     * Injects serverUtils and mainCtrl, so it's possible to call methods from there
     *
     * @param serverUtils The instance of ServerUtils
     * @param mainCtrl    The instance of MainCtrl
     */
    @Inject
    public GameUtils(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;

        this.activeGame = false;
        this.activeFeatures = false;
    }

    /**
     * Create new Game with the players that are currently in the lobby and list of 20 questions
     */
    public void startMultiplayerGame() {
        ClientBuilder.newClient(new ClientConfig()) //
            .target(serverUtils.getServerIP()).path("api/game/multiplayer/start") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .get(Game.class);
    }

    /**
     * Create new Game with a list of 20 questions
     *
     * @return newly created Game object with unique ID
     */
    public Game startSingleplayer() {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(serverUtils.getServerIP()).path("api/game/singleplayer/start") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .get(Game.class);
    }

    /**
     * Send feature to the server it can be an emoji, joker or score of the player
     *
     * @param type  The type of the send feature, it can be EMOJI, JOKER or SCORE
     * @param name  The name of the player
     * @param value The type of the emoji / joker or the score of the player
     */
    public void sendFeature(String type, String name, String value) {
        Pair<String, String> details = new Pair<>(name, value);
        ClientBuilder.newClient(new ClientConfig()) //
            .target(serverUtils.getServerIP()).path("api/game/send/" + type + "/" + mainCtrl.getGame().getId()) //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .post(Entity.entity(details, APPLICATION_JSON));
    }

    /**
     * Changes whether long polling is active on front-end, type determines what long polling request is executed
     * game - long polling for game
     * features - long polling for emojis, jokers and leaderboard scores
     *
     * @param type          Whether polling must for game or all others features
     * @param pollingActive Whether polling must be active on front-end
     */
    public void setActive(String type, boolean pollingActive) {
        switch (type) {
            case "game":
                if (!activeGame && pollingActive) {
                    activeGame = true;
                    new Thread(() -> {
                        while (activeGame) {
                            getGame();
                        }
                    }).start();
                }
                activeGame = pollingActive;
                break;
            case "features":
                if (!activeFeatures && pollingActive) {
                    activeFeatures = true;
                    new Thread(() -> {
                        while (activeFeatures) {
                            getFeatures();
                        }
                    }).start();
                }
                activeFeatures = pollingActive;
                break;
            default:
                break;
        }
    }

    /**
     * Executes long-polling loop for receiving the game
     */
    private void getGame() {
        Game game = ClientBuilder.newClient(new ClientConfig())
            .target(serverUtils.getServerIP())
            .path("api/game/" + mainCtrl.getLobby().getId())
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(Game.class);

        if (activeGame) {
            mainCtrl.setGame(game);
            Platform.runLater(() -> mainCtrl.startGame(true));
        }
    }

    /**
     * Executes long-polling loop for receiving all changed features
     */
    private void getFeatures() {
        String json = ClientBuilder.newClient(new ClientConfig())
            .target(serverUtils.getServerIP())
            .path("api/game/features/" + mainCtrl.getGame().getId())
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(String.class);

        try {
            if (activeFeatures) {
                performAction(mapper.readTree(json));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.err.println("Error while parsing JSON from long polling on client-side");
        }
    }

    /**
     * Calls appropriate method based on received JSON string
     *
     * @param response An object generated from the parsed JSON string that allows for value retrieval
     */
    public void performAction(JsonNode response) {
        String name = response.get("name").asText();
        String value = response.get("value").asText();
        switch (response.get("type").asText()) {
            case "EMOJI":
                Platform.runLater(() -> mainCtrl.displayNewEmoji(name, value));
                break;
            case "JOKER":
                if (value.equals("HALVE_TIME") && !mainCtrl.getPlayer().hasSameName(new LeaderboardEntry(name, 0))) {
                    Platform.runLater(mainCtrl::halveTime);
                }
                // TODO other jokers
                break;
            case "SCORE":
                break;
            default:
                System.err.println("Unknown long polling response type");
                break;
        }
    }
}