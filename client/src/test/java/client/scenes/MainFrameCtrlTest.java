package client.scenes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import client.dependedoncomponents.MainCtrlDOC;
import client.dependedoncomponents.MainFrameCtrlDOC;
import client.dependedoncomponents.SupportsLogging;
import client.utils.LobbyUtils;
import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MainFrameCtrlTest implements SupportsLogging {


    private ServerUtils serverUtils;
    private LobbyUtils lobbyUtils;

    private MainCtrl mainCtrl;
    private MainFrameCtrlDOC sut;

    @BeforeEach
    public void setup() {
        serverUtils = new ServerUtils();
        mainCtrl = new MainCtrlDOC();
        lobbyUtils = new LobbyUtils(serverUtils, mainCtrl);

        sut = new MainFrameCtrlDOC(serverUtils, lobbyUtils, mainCtrl);
        clearLogs();
    }

    @Test
    public void constructorTest() {
        assertNotNull(sut);
    }

    @Test
    public void openLeaderboardTest() {
        sut.openLeaderboard();
        assertEquals("openedLeaderBoard", logs.get(0));
    }

    @Test
    public void startSingleplayerGameTest() {
        sut.startSingleplayerGame();
        assertEquals("singleplayerGameStarted", logs.get(0));
    }

    @Test
    public void joinLobbyTest() {
        sut.joinLobby();
        assertEquals("joinedLobby", logs.get(0));
    }

    @Test
    public void showAdminTest() {
        sut.showAdmin();
        assertEquals("showedAdmin", logs.get(0));
    }

    @Test
    public void displayUsernameErrorTest() {
        sut.displayUsernameError(true, "This username is already taken!");
        assertEquals("displayedUsernameError", logs.get(0));
    }

    @Test
    public void displayServerIPErrorTest() {
        sut.displayServerIPError(true);
        assertEquals("displayedServerIPError", logs.get(0));
    }
}
