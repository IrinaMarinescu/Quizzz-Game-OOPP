package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import client.scenes.MainCtrl;
import commons.LeaderboardEntry;
import commons.Lobby;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import javax.inject.Inject;
import org.glassfish.jersey.client.ClientConfig;

public class LobbyUtils {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;

    public boolean active;

    /**
     * Constructor
     *
     * @param serverUtils ServerUtils object
     */
    @Inject
    public LobbyUtils(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;

        this.active = false;
    }

    /**
     * Check if another user in lobby already uses this name
     *
     * @param username provided by player in the TextField
     * @return true if the username is not used yet, false otherwise
     */
    public boolean validateUsername(String username) {
        return !ClientBuilder.newClient(new ClientConfig()) //
            .target(serverUtils.getServerIP()).path("api/lobby") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .get(Lobby.class).isUsernameTaken(username);
    }

    /**
     * Add player to the lobby
     *
     * @param player that has to be added to the lobby
     * @return The Lobby object that player has been added to
     */
    public Lobby joinLobby(LeaderboardEntry player) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(serverUtils.getServerIP()).path("api/lobby/add") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .post(Entity.entity(player, APPLICATION_JSON), Lobby.class);
    }

    /**
     * Remove player to the lobby
     *
     * @param player that has to be removed to the lobby
     * @return The Lobby object that player has been removed to
     */
    public boolean leaveLobby(LeaderboardEntry player) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(serverUtils.getServerIP()).path("api/lobby/remove") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .post(Entity.entity(player, APPLICATION_JSON), Boolean.class);
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
                    updateLobby();
                }
            }).start();
        }
        active = pollingActive;
    }

    /**
     * Executes long-polling loop
     */
    private void updateLobby() {
        Lobby lobby = ClientBuilder.newClient(new ClientConfig())
            .target(serverUtils.getServerIP())
            .path("api/lobby/" + mainCtrl.getLobby().getId())
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(Lobby.class);

        if (active) {
            mainCtrl.updateLobby(lobby);
        }
    }
}
