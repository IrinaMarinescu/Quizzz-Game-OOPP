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

    public Lobby(UUID id) {
        this.id = id;
        this.players = new ArrayList<>();
    }

    public Lobby(UUID id, List<LeaderboardEntry> players) {
        this.id = id;
        this.players = players;
    }

    public List<LeaderboardEntry> getPlayers() {
        return players;
    }

    public UUID getId() {
        return id;
    }

    public boolean isInLobby(LeaderboardEntry player) {
        return players.contains(player);
    }

    public boolean addPlayer(LeaderboardEntry player) {
        return players.add(player);
    }

    public boolean removePlayer(LeaderboardEntry player) {
        return players.remove(player);
    }

    /**
     * Check if any pllayer in the lobby has given username
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
