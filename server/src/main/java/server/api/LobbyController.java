package server.api;

import commons.Game;
import commons.LeaderboardEntry;
import commons.Lobby;
import commons.Question;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lobby")
public class LobbyController {

    private Lobby lobby;

    public LobbyController() {
        UUID lobbyId = UUID.randomUUID();
        lobby = new Lobby(lobbyId);
    }

    /**
     * Return the current lobby
     *
     * @return the Lobby object
     */
    @GetMapping("")
    public Lobby getLobby() {
        return lobby;
    }

    /**
     * Add specified player to the lobby
     *
     * @param player that has to be added to the lobby
     * @return true if the player was not in the lobby, false otherwise
     */
    @PostMapping("/add-player")
    public Lobby addPlayerToLobby(LeaderboardEntry player) {
        lobby.addPlayer(player);
        return lobby;
    }

    /**
     * Removes specified player from the lobby
     *
     * @param player that has to be removed from the lobby
     * @return true if the player was in the lobby, false otherwise
     */
    @PostMapping("/remove-player")
    public boolean removePlayerFromLobby(LeaderboardEntry player) {
        return lobby.removePlayer(player);
    }

    /**
     * Create new Game object with the id and players from the lobby and resets the lobby
     *
     * @param questions set of questions needed to create a game
     * @return Game object with the id and players from the lobby
     */
    public Game createGame(List<Question> questions) {
        Game newGame = new Game(lobby.getId(), questions, lobby.getPlayers());
        UUID lobbyId = UUID.randomUUID();
        lobby = new Lobby(lobbyId);
        return newGame;
    }

}
