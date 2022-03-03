package client.scenes.controllerrequirements;

public interface LobbyCtrlRequirements {

    /**
     * Adds a player to the list of players
     *
     * @param name The name of the player to be added
     */
    void addPlayer(String name);

    /**
     * Removes a player of the provided name from the list of players
     *
     * @param name The name of the player
     */
    void removePlayer(String name);
}
