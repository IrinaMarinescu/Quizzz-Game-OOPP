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
     *
     */
    void startSingleplayerGame();

    /**
     * Called by LobbyCtrl
     */
    void startMultiplayerGame();

    /**
     * The score that the user would get if they answer INSTANTLY
     *
     * @param baseScore the score (0 - 100)
     */
    void addPoints(int baseScore);

    void showGlobalLeaderboardFrame();

    void doublePoints();

    void eliminateWrongAnswer();

    void halveTime();
}
