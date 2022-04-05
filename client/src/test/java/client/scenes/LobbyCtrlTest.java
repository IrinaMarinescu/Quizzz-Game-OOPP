package client.scenes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import client.dependedoncomponents.GameUtilsDOC;
import client.dependedoncomponents.LobbyCtrlDOC;
import client.dependedoncomponents.MainCtrlDOC;
import client.dependedoncomponents.SupportsLogging;
import commons.Lobby;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LobbyCtrlTest implements SupportsLogging {

    private GameUtilsDOC gameUtils;
    private MainCtrlDOC mainCtrl;
    private Lobby lobbyTest;

    private LobbyCtrlDOC sut;

    @BeforeEach
    public void setup() {
        gameUtils = new GameUtilsDOC();
        mainCtrl = new MainCtrlDOC();
        lobbyTest = new Lobby(UUID.randomUUID());
        sut = new LobbyCtrlDOC(gameUtils, mainCtrl);
        logs.clear();
    }

    @Test
    public void constructorTest() {
        assertNotNull(sut);
    }

    @Test
    public void initializeMultiplayerGameTest() {
        sut.initializeMultiplayerGame();
        assertEquals("multiplayerGameInitialized", logs.get(0));
    }

    @Test
    public void goBackTest() {
        sut.goBack();
        assertEquals("goBack", logs.get(0));
    }

    @Test
    public void setLobby() {
        sut.setLobby(lobbyTest);
        assertEquals("newLobbySet", logs.get(0));
    }

}
