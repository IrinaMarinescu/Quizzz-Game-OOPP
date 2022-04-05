package client.dependedoncomponents;

import client.scenes.MainCtrl;
import client.scenes.MainFrameCtrl;
import client.utils.LobbyUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;

/**
 * For testing MainFrameCtrl
 */
public class MainFrameCtrlDOC extends MainFrameCtrl implements SupportsLogging {

    @Inject
    public MainFrameCtrlDOC(ServerUtils serverUtils, LobbyUtils lobbyUtils, MainCtrl mainCtrl) {
        super(serverUtils, lobbyUtils, mainCtrl);
        clearLogs();
    }

    @Override
    public void openLeaderboard() {
        log("openedLeaderBoard");
    }

    @Override
    public void startSingleplayerGame() {
        log("singleplayerGameStarted");
    }

    @Override
    public void joinLobby() {
        log("joinedLobby");
    }

    @Override
    public void showAdmin() {
        log("showedAdmin");
    }

    @Override
    public void displayUsernameError(boolean show, String errorMessage) {
        log("displayedUsernameError");
    }

    @Override
    public void displayServerIPError(boolean show) {
        log("displayedServerIPError");
    }
}
