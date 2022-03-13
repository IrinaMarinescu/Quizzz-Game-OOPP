package server.api;

import commons.LeaderboardEntry;
import commons.Lobby;
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

    @GetMapping("/lobby")
    public Lobby getLobby() {
        return lobby;
    }

    @GetMapping("/lobby/reset")
    public void resetLobby() {
        UUID lobbyId = UUID.randomUUID();
        lobby = new Lobby(lobbyId);
    }

    @PostMapping("/lobby/add")
    public Lobby addPlayerToLobby(LeaderboardEntry player) {
        lobby.addPlayer(player);
        return lobby;
    }

    @PostMapping("/lobby/remove")
    public boolean removePlayerFromLobby(LeaderboardEntry player) {
        return lobby.removePlayer(player);
    }
}
