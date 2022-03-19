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
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javafx.util.Pair;
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
        if (validateIP(serverIP) && !serverIP.equals("")) {
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
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(!serverIP.equals("") ? serverIP : this.serverIP).path("api/game/validate") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .get(String.class).equals("Connected");
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
        return new ArrayList<>();
    }

    public void sendNewEmoji(String username, String reaction) {
        Pair<String, String> details = new Pair<>(username, reaction);
        ClientBuilder.newClient(new ClientConfig()) //
            .target(serverIP).path("api/sendEmote/{gameId}") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .post(Entity.entity(details, APPLICATION_JSON), List.class);
    }

    public void halveTime(UUID gameId) {
        ClientBuilder.newClient(new ClientConfig()) //
            .target(serverIP).path("api/halveTime/{gameId}") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .post(Entity.entity(true, APPLICATION_JSON), Boolean.class);
    }

    public void disconnect(UUID gameId, LeaderboardEntry player) {
        // TODO send data to server that the player disconnected
    }
}