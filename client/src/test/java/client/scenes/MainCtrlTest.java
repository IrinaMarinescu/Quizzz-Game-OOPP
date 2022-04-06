package client.scenes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import client.dependedoncomponents.MainCtrlDOC;
import client.dependedoncomponents.SupportsLogging;
import commons.Game;
import commons.LeaderboardEntry;
import commons.Lobby;
import commons.Question;
import java.util.ArrayList;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MainCtrlTest implements SupportsLogging {

    private boolean gameOngoing = false;
    private int doublePoints;

    private MainCtrl sut;
    private Game gameTest;
    private Lobby lobbyTest;
    private LeaderboardEntry leaderboardEntry;

    @BeforeEach
    public void setup() {
        sut = new MainCtrlDOC();
        gameTest =
            new Game(UUID.randomUUID(), new ArrayList<Question>(), new ArrayList<LeaderboardEntry>());
        lobbyTest = new Lobby(UUID.randomUUID(), new ArrayList<LeaderboardEntry>());
        leaderboardEntry = new LeaderboardEntry("Username", 120);
        doublePoints = 20;
        logs.clear();
    }

    @Test
    public void setPlayerTest() {
        sut.setPlayer("Username", 120);
        assertEquals(leaderboardEntry, sut.getPlayer());
    }

    @Test
    public void setGameTest() {
        sut.setGame(gameTest);
        assertEquals(gameTest, sut.getGame());
    }

    @Test
    public void setLobbyTest() {
        sut.setLobby(lobbyTest);
        assertEquals("setLobby", logs.get(0));
    }

    @Test
    public void startGameTest() {
        sut.startGame(false);
        assertEquals("game", logs.get(0));
    }

    @Test
    public void joinLobbyTest() {
        sut.joinLobby();
        assertEquals("joinedLobby", logs.get(0));
    }

    @Test
    public void doublePointsTest() {
        sut.doublePoints();
        assertEquals("doubledPoints", logs.get(0));
    }

    @Test
    public void exitGameCheckerTest() {
        sut.exitGameChecker(0);
        assertEquals("exitChecker", logs.get(0));
    }

    @Test
    public void disconnectTest() {
        sut.disconnect(0, "yesButton");
        assertEquals("attemptedDisconnect", logs.get(0));
    }

    @Test
    public void toggleModalVisibilityTest() {
        sut.toggleModalVisibility();
        assertEquals("modal", logs.get(0));
    }

    @Test
    public void showMainFrameTest() {
        sut.showMainFrame();
        assertEquals("mainFrame", logs.get(0));
    }
}
