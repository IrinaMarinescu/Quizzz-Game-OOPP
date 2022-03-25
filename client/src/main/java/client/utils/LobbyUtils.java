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
     * Injects serverUtils and mainCtrl, so it's possible to call methods from there
     *
     * @param serverUtils ServerUtils object
     * @param mainCtrl    MainCtrl object
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
     * @param username The username provided by the player in the TextField
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
     * @param player The player that has to be added to the lobby
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
     * @param player The player that has to be removed to the lobby
     */
    public void leaveLobby(LeaderboardEntry player) {
        ClientBuilder.newClient(new ClientConfig()) //
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
            mainCtrl.setLobby(lobby);
        }
    }
}
