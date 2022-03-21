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

import commons.Activity;
import commons.Game;
import commons.LeaderboardEntry;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.glassfish.jersey.client.ClientConfig;

/**
 * This class contains all the utils for communicating with the server
 * (e.g. all the functions the client will use to send/request data from the server)
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
            this.serverIP = parseIP(serverIP);
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
                .target(parseIP(serverIP)).path("api/game/validate") //
                .request(APPLICATION_JSON) //
                .accept(APPLICATION_JSON) //
                .get(String.class).equals("Connected");
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * Lets the user input no IP when trying to use the app.
     * If the "Server IP" field is empty, localhost:8080 will be the default.
     *
     * @param serverIP the IP to check
     * @return either the same IP, if the IP is not empty, or "http://localhost:8080/" otherwise.
     */
    private String parseIP(String serverIP) {
        return (serverIP.length() != 0) ? serverIP : "http://localhost:8080/";
    }

    /**
     * Fetches the first <code>limit</code> entries sorted by score from the leaderboard.
     *
     * @param limit the number of entries to fetch
     * @return a list of leaderboard entries, at most <code>limit</code> in number, sorted by the score.
     */
    public List<LeaderboardEntry> getSoloLeaderboard(int limit) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(serverIP).path("api/leaderboard/" + limit) //
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<>() {
            });
    }


    /**
     * Adds a new entry to the leaderboard after a singleplayer game.
     *
     * @param entry a LeaderboardEntry object, which will contain the name of the player and their score
     * @return the entry that was just added
     */
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

    public void disconnect(UUID gameId, LeaderboardEntry player) {
        // TODO send data to server that the player disconnected
    }


    /**
     * Fetch the list of all the activities stored in the database
     *
     * @return a list of activities, containing all the activities stored in the DB
     */
    public List<Activity> fetchActivities() {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(serverIP).path("api/activities/") //
            .request(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .get(new GenericType<>() {
            });
    }

    /**
     * Updates an activity in the database.
     *
     * @param activity the new activity object
     * @return the same object on success
     */
    public Activity sendActivityUpdate(Activity activity) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(serverIP).path("api/activities/update") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .post(Entity.entity(activity, APPLICATION_JSON), Activity.class);
    }

    /**
     * Deletes an activity from the DB, by ID.
     *
     * @param activity the activity object to delete from the database
     * @return an Activity instance equal to the activity that was just deleted
     */
    public Activity deleteActivity(Activity activity) {
        return ClientBuilder.newClient(new ClientConfig()) //
            .target(serverIP).path("api/activities/del") //
            .request(APPLICATION_JSON) //
            .accept(APPLICATION_JSON) //
            .post(Entity.entity(activity, APPLICATION_JSON), Activity.class);
    }
}