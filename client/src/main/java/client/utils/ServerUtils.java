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

/**
 * Not relevant for now
 */
public class ServerUtils {

    public static final String applicationJson = "application/json";
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
     * @return true if serverIP is correct, false otherwise
     */
    public boolean setServerIP(String serverIP) {
        if (validateIP(serverIP)) {
            this.serverIP = serverIP;
            return true;
        }
        return false;
    }

    /**
     * Check if the provided server IP is correct
     *
     * @param serverIP IP to validate
     * @return true if serverIP is correct, false otherwise
     */
    public boolean validateIP(String serverIP) {
        // TODO checks if the provided IP is correct
        return true;
    }
}