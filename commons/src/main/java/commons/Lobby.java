package commons;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Lobby {

    private UUID id;
    private List<LeaderboardEntry> players;

    /**
     * Empty constructor need to create an instance from JSON file
     */
    public Lobby() {
    }

    /**
     * Constructor (no players, empty arraylist initialized)
     *
     * @param id The id of this lobby
     */
    public Lobby(UUID id) {
        this.id = id;
        this.players = new ArrayList<>();
    }

    /**
     * Constructor
     *
     * @param id      The id of this lobby
     * @param players List of players
     */
    public Lobby(UUID id, List<LeaderboardEntry> players) {
        this.id = id;
        this.players = new ArrayList<>(players);
    }

    /**
     * @return The list containing players
     */
    public List<LeaderboardEntry> getPlayers() {
        return players;
    }

    /**
     * @return id of this lobby
     */
    public UUID getId() {
        return id;
    }

    /**
     * Whether the exact leaderboardEntry provided is in lobby
     *
     * @param player The player (name and score)
     * @return Whether that exact entry is in the lobby
     */
    public boolean isInLobby(LeaderboardEntry player) {
        return players.contains(player);
    }

    /**
     * Adds a player to the lobby
     *
     * @param player The player to add
     * @return Whether that player was added successfully (almost always true)
     */
    public boolean addPlayer(LeaderboardEntry player) {
        return players.add(player);
    }

    /**
     * Removes a player from teh lobby
     *
     * @param player The player to remove
     * @return Whether the player was successfully removed (was in the lobby previously)
     */
    public boolean removePlayer(LeaderboardEntry player) {
        return players.remove(player);
    }

    /**
     * Check if any player in the lobby has given username
     *
     * @param username to check if is in the lobby
     * @return true if any player has given username, false otherwise
     */
    public boolean isUsernameTaken(String username) {
        for (LeaderboardEntry player : players) {
            if (player.getName().equals(username)) {
                return true;
            }
        }
        return false;
    }
}
