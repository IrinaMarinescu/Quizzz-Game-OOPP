package client.scenes.controllerrequirements;

import commons.LeaderboardEntry;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;

/**
 * This interface lists all functions in QuestionFrameCtrl that should ever be needed by MainCtrl
 */
public interface QuestionFrameRequirements {

    /**
     * Resets the question frame and initializes settings for a new single-player game
     */
    void initializeSingleplayerGame();

    /**
     * Resets the question frame and initializes settings for a new multiplayer game
     *
     * @param players The names of all players involved
     */
    void initializeMultiplayerGame(List<LeaderboardEntry> players);

    /**
     * Sets node containing question at the center of the frame
     *
     * @param questionNode The node to be inserted in the center of the frame
     */
    void setCenterContent(Node questionNode, boolean animate);

    /**
     * Add points to a player's score, as seen in top left
     *
     * @param points The number of points to be added
     */
    void addPoints(int points);

    /**
     * Adds a new reaction sent by another player to the right
     *
     * @param name     The name of the person sending the emote
     * @param reaction A string representing the emoticon (Accepted values: "happy", "sad", "angry", "surprised")
     */
    void displayNewEmoji(String name, String reaction);

    /**
     * Increments the question number displayed in the top right
     */
    void incrementQuestionNumber();

    /**
     * Makes leaderboard entries visible in side leaderboard
     *
     * @param entries The entries to display in the leaderboard
     * @return List of entries after sorting and slicing (Only relevant for testing)
     */
    List<LeaderboardEntry> setLeaderboardContents(List<LeaderboardEntry> entries);

    /**
     * Provides functionality for keybindings to accelerate certain actions
     *
     * @param e Information about a keypress performed by the user
     *          <p>
     *          This should only be called by the MainCtrl showQuestionFrame method
     */
    void keyPressed(KeyCode e);
}
