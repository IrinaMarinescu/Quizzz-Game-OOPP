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

import com.google.common.base.Strings;
import commons.Activity;
import commons.LeaderboardEntry;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.glassfish.jersey.client.ClientConfig;

/**
 * This class contains all the utils for communicating with the server
 * (e.g. all the functions the client will use to send/request data from the server)
 */
public class ServerUtils {

    private static final String CRLF = "\r\n";
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

    /**
     * Adds a new activity, with the corresponding image.
     *
     * @param activity the activity to add to the DB
     * @param image the image to bind to the activity
     * @return a boolean, representing whether the request was successful or not.
     */
    public boolean addActivity(Activity activity, File image) {
        return (httpUpload(activity, image.getName(), getBytesFromFile(image)) == 200);
    }

    /**
     * Turns a file into a byte array.
     *
     * @param file the file to get the byte array from
     * @return an array of bytes, representing the contents of the file.
     */
    protected static byte[] getBytesFromFile(File file) {
        try {
            FileInputStream fl = new FileInputStream(file);
            byte[] arr = new byte[(int) file.length()];
            fl.read(arr);
            fl.close();
            return arr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Adds an activity to the database, with a photo.
     *
     * @param activity the activity to add
     * @param filename the name of the file
     * @param byteStream the photo, as a byte stream
     * @return an integer, representing the response code of the request.
     */
    public int httpUpload(Activity activity, String filename, byte[] byteStream) {
        try {
            HttpURLConnection connection = (HttpURLConnection)
                new URL(serverIP + "api/activities/contribute").openConnection();
            final String boundary = Strings.repeat("-", 15) + Long.toHexString(System.currentTimeMillis());

            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            OutputStream directOutput = connection.getOutputStream();
            PrintWriter body = new PrintWriter(new OutputStreamWriter(directOutput, StandardCharsets.UTF_8), true);

            body.append(CRLF);
            addSimpleFormData("id", activity.id, body, boundary);
            addSimpleFormData("image_path", activity.imagePath, body, boundary);
            addSimpleFormData("title", activity.title, body, boundary);
            addSimpleFormData("consumption_in_wh", Long.toString(activity.consumptionInWh), body, boundary);
            addSimpleFormData("source", activity.source, body, boundary);
            addFileData("file", filename, byteStream, body, directOutput, boundary);

            body.append("--").append(boundary).append("--").append(CRLF);
            body.flush();


            int responseCode = connection.getResponseCode();
            return responseCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Adds key-value pairs to the request.
     *
     * @param paramName the parameter name
     * @param value the parameter value
     * @param body the body of the request
     * @param boundary the string boundary
     */
    private static void addSimpleFormData(String paramName, String value, PrintWriter body, final String boundary) {
        body.append("--").append(boundary).append(CRLF);
        body.append("Content-Disposition: form-data; name=\"" + paramName + "\"").append(CRLF);
        body.append("Content-Type: text/plain; charset=" + "UTF-8").append(CRLF);
        body.append(CRLF);
        body.append(value).append(CRLF);
        body.flush();
    }

    /**
     * Adds a file to the request.
     *
     * @param paramName the parameter name to be taken from the request in the server
     * @param filename the name of the file
     * @param byteStream the byte stream
     * @param body body of the request
     * @param directOutput the direct output stream
     * @param boundary the boundary string
     */
    private static void addFileData(String paramName,
            String filename, byte[] byteStream, PrintWriter body,
            OutputStream directOutput, final String boundary) {
        body.append("--").append(boundary).append(CRLF);
        body.append("Content-Disposition: form-data; name=\"" + paramName
            + "\"; filename=\"" + filename + "\"").append(CRLF);
        body.append("Content-Type: application/octed-stream").append(CRLF);
        body.append("Content-Transfer-Encoding: binary").append(CRLF);
        body.append(CRLF);
        body.flush();
        try {
            directOutput.write(byteStream);
            directOutput.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        body.append(CRLF);
        body.flush();
    }
}