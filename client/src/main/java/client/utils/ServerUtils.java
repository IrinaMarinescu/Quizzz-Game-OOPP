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
import java.util.List;
import org.glassfish.jersey.client.ClientConfig;

/**
 * Not relevant for now
 */
public class ServerUtils {

    private String serverIP = "http://localhost:8080/";

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
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(serverIP).path("api/game/validate") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .get(String.class).equals("Connected");
    }

    /**
     * Check if another user in lobby already uses this name
     *
     * @param username username provided by player in the TextField
     * @return true if the username is not used yet, false otherwise
     */
    public boolean validateUsername(String username) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(serverIP).path("api/lobby") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .get(new GenericType<List<LeaderboardEntry>>() {
            }).contains(username);
    }

    public void joinLobby(LeaderboardEntry player) {
        ClientBuilder.newClient(new ClientConfig()) //
            .target(serverIP).path("api/lobby/add") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .post(Entity.entity(player, APPLICATION_JSON), LeaderboardEntry.class);
    }

    public Game startMultiplayerGame() {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(serverIP).path("api/game/multiplayer/start") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .get(Game.class);
    }

    public Game startSingleplayer() {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(serverIP).path("api/game/singleplayer/start") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .get(Game.class);
    }
}