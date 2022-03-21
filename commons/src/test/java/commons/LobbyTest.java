package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the lobby class
 */
public class LobbyTest {

    private Lobby sut;
    private UUID id;
    private List<LeaderboardEntry> players;

    @BeforeEach
    void setup() {
        id = new UUID(123, 456);
        players = List.of(new LeaderboardEntry("James", 23), new LeaderboardEntry("Test", 5));
        sut = new Lobby(id, players);
    }

    @Test
    void constructor() {
        assertNotNull(sut);
    }

    @Test
    void getId() {
        assertSame(id, sut.getId());
    }

    @Test
    void getPlayers() {
        assertEquals(players, sut.getPlayers());
    }

    @Test
    void addPlayer() {
        sut = new Lobby(id);
        assertEquals(List.of(), sut.getPlayers());
        assertTrue(sut.addPlayer(new LeaderboardEntry("James", 73)));
        assertEquals(List.of(new LeaderboardEntry("James", 73)), sut.getPlayers());
    }

    @Test
    void isInLobby() {
        assertTrue(sut.isInLobby(new LeaderboardEntry("James", 23)));
        assertFalse(sut.isInLobby(new LeaderboardEntry("Random", 23)));
    }

    @Test
    void removePlayer() {
        assertSame(2, sut.getPlayers().size());
        assertFalse(sut.removePlayer(new LeaderboardEntry("What", 44)));
        assertSame(2, sut.getPlayers().size());
        assertTrue(sut.removePlayer(new LeaderboardEntry("James", 23)));
        assertSame(1, sut.getPlayers().size());
    }

    @Test
    void isUserNameTaken() {
        assertTrue(sut.isUsernameTaken("James"));
        assertFalse(sut.isUsernameTaken("Michael"));
    }
}
