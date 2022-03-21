package server.api;

import commons.Game;
import commons.LeaderboardEntry;
import commons.Lobby;
import commons.Question;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lobby")
public class LobbyController {

    private Lobby lobby;
    public UUID receivingLobbyId = UUID.randomUUID();


    /**
     * Set up an empty lobby
     */
    public LobbyController() {
        lobby = new Lobby(UUID.randomUUID());
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
    @PostMapping("add")
    public ResponseEntity<Lobby> addPlayerToLobby(@RequestBody LeaderboardEntry player) {
        lobby.addPlayer(player);
        dispatch(lobby.getId());
        return ResponseEntity.ok(this.lobby);
    }

    /**
     * Removes specified player from the lobby
     *
     * @param player that has to be removed from the lobby
     */
    @PostMapping("remove")
    public void removePlayerFromLobby(@RequestBody LeaderboardEntry player) {
        lobby.removePlayer(player);
        dispatch(lobby.getId());
    }

    /**
     * Create new Game object with the id and players from the lobby and resets the lobby
     *
     * @param questions set of questions needed to create a game
     * @return Game object with the id and players from the lobby
     */
    public Game createGame(List<Question> questions) {
        Game newGame = new Game(lobby.getId(), questions, lobby.getPlayers());
        lobby = new Lobby(UUID.randomUUID());
        return newGame;
    }

    /**
     * This is where front-end sends a request which gets stored
     *
     * @param lobbyId The id of the game to which data will be returned to
     * @return A JSON string corresponding to the result
     */
    @GetMapping(path = {"/{lobbyId}"})
    synchronized ResponseEntity<Lobby> receivePoll(@PathVariable UUID lobbyId) {
        try {
            do {
                wait();
            } while (!lobbyId.equals(receivingLobbyId));
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.err.println("Thread that was paused due to long polling on server got interrupted");
        }
        return ResponseEntity.ok(lobby);
    }

    /**
     * This must be called by server-side methods to send data to the client.
     * This generates a JSON Lobby and sends it to all connected players.
     *
     * @param receivingLobbyId The ID of the lobby to which the data must be sent to
     */

    final synchronized void dispatch(UUID receivingLobbyId) {
        this.receivingLobbyId = receivingLobbyId;
        notifyAll();
    }

}
