package server.api;

import commons.LeaderboardEntry;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lobby")
public class LobbyController {

    private List<LeaderboardEntry> lobby;

    public LobbyController() {
        lobby = new ArrayList<>();
    }

    @GetMapping("/lobby")
    public List<LeaderboardEntry> getLobby() {
        return lobby;
    }

    @GetMapping("/lobby/reset")
    public void resetLobby() {
        lobby = new ArrayList<>();
    }

    @PostMapping("/lobby/add")
    public boolean addPlayerToLobby(LeaderboardEntry player) {
        if (lobby.contains(player)) {
            return false;
        }
        lobby.add(player);
        return true;
    }
}
