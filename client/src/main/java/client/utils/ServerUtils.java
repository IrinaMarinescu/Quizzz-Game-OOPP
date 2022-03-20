/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package client.utils;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import commons.Game;
import commons.LeaderboardEntry;
import commons.Lobby;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;
import org.glassfish.jersey.client.ClientConfig;

/**
 * Not relevant for now
 */
public class ServerUtils {

    private String serverIP = "http://localhost:8080/";

    /**
     * Get the IP of the server
     *
     * @return the IP of the server
     */
    public String getServerIP() {
        return serverIP;
    }

    /**
     * Set server IP
     *
     * @param serverIP IP to set
     */
    public void setServerIP(String serverIP) {
        if (validateIP(serverIP)) {
            this.serverIP = serverIP;
        }
    }

    /**
     * Check if the provided server IP is correct
     *
     * @param serverIP IP to validate
     * @return true if serverIP is correct, false otherwise
     */
    public boolean validateIP(String serverIP) {
        try {
            return ClientBuilder.newClient(new ClientConfig()) //
                    .target(serverIP).path("api/game/validate") //
                    .request(APPLICATION_JSON) //
                    .accept(APPLICATION_JSON) //
                    .get(String.class).equals("Connected");
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * Check if another user in lobby already uses this name
     *
     * @param username provided by player in the TextField
     * @return true if the username is not used yet, false otherwise
     */
    public boolean validateUsername(String username) {
        return !ClientBuilder.newClient(new ClientConfig()) //
            .target(serverIP).path("api/lobby") //
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
            .target(serverIP).path("api/lobby/add") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .post(Entity.entity(player, APPLICATION_JSON), Lobby.class);
    }

    /**
     * Create new Game with the players that are currently in the lobby and list of 20 questions
     *
     * @return newly created Game object with unique ID
     */
    public Game startMultiplayerGame() {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(serverIP).path("api/game/multiplayer/start") //
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
            .target(serverIP).path("api/game/singleplayer/start") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .get(Game.class);
    }

    public List<LeaderboardEntry> getSoloLeaderboard(int limit) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(serverIP).path("api/leaderboard/" + limit) //
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<>() {
            });
    }

    public LeaderboardEntry addLeaderboardEntry(LeaderboardEntry entry) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(serverIP).path("api/leaderboard/add") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .post(Entity.entity(entry, APPLICATION_JSON), LeaderboardEntry.class);
    }

    public Game getGame() {
        // TODO make request to server to get game object
        return null;
    }

    public void sendPointsGained(UUID gameId, LeaderboardEntry player, int pointsGained) {
        // TODO send to server the number of points that have been gained
    }

    public List<LeaderboardEntry> getUpdatedScores(UUID gameId) {
        // TODO retrieve updated scores for this game from the server
        return null;
    }

    public void disconnect(UUID gameId, LeaderboardEntry player) {
        // TODO send data to server that the player disconnected
    }
}