package client.scenes.controllerrequirements;

/**
 * These methods must be implemented in MainCtrl for some controllers to work
 */
public interface MainCtrlRequirements {

    /**
     * Is called when a player chooses disconnects from the game
     */
    void disconnect();

    /**
     * Connects to a server
     *
     * @param ip IP address of the server to connect to
     */
    void connectToServer(String ip);

    /**
     *
     */
    void startSingleplayerGame();

    /**
     * Called by LobbyCtrl
     */
    void startMultiplayerGame();

    /**
     * Called by the lobby class
     *
     * @param name The name chosen by the person
     */
    void redirectToLobby(String name);

    /**
     *
     */
    void redirectToSoloLeaderboard();

    /**
     * The score that the user would get if they answer INSTANTLY
     *
     * @param baseScore the score (0 - 100)
     */
    void addPoints(int baseScore);

    void redirectToMainScreen();

    /**
     * Called by the lobby when a player leaves the game
     */
    void playerLeavesLobby();
}
