package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import client.scenes.MainCtrl;
import commons.Game;
import jakarta.ws.rs.client.ClientBuilder;
import javafx.application.Platform;
import javax.inject.Inject;
import org.glassfish.jersey.client.ClientConfig;

public class GameUtils {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;

    public boolean active;

    /**
     * Constructor
     *
     * @param serverUtils ServerUtils object
     */
    @Inject
    public GameUtils(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;

        this.active = false;
    }

    /**
     * Create new Game with the players that are currently in the lobby and list of 20 questions
     *
     * @return newly created Game object with unique ID
     */
    public Game startMultiplayerGame() {
        return ClientBuilder.newClient(new ClientConfig()) //
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
     * Changes whether long polling is active on front-end
     *
     * @param pollingActive Whether polling must be active on front-end
     */
    public void setActive(boolean pollingActive) {
        if (!active && pollingActive) {
            new Thread(() -> {
                while (active) {
                    getGame();
                }
            }).start();
        }
        active = pollingActive;
    }

    /**
     * Executes long-polling loop
     */
    private void getGame() {
        Game game = ClientBuilder.newClient(new ClientConfig())
            .target(serverUtils.getServerIP())
            .path("api/game/" + mainCtrl.getLobby().getId())
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(Game.class);

        if (active) {
            mainCtrl.setGame(game);
            Platform.runLater(mainCtrl::startMultiplayerGame);
        }
    }
}
