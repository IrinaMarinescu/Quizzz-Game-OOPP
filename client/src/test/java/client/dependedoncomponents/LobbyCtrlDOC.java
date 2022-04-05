package client.dependedoncomponents;


import client.scenes.LobbyCtrl;
import client.scenes.MainCtrl;
import client.utils.GameUtils;
import com.google.inject.Inject;
import commons.Lobby;

/**
 * For testing LobbyCtrl
 */
public class LobbyCtrlDOC extends LobbyCtrl implements SupportsLogging {

    private GameUtilsDOC gameUtils;
    private MainCtrlDOC mainCtrl;

    @Inject
    public LobbyCtrlDOC(GameUtils gameUtils, MainCtrl mainCtrl) {
        super(gameUtils, mainCtrl);
        clearLogs();
    }

    @Override
    public void initializeMultiplayerGame() {
        log("multiplayerGameInitialized");
    }

    @Override
    public void goBack() {
        log("goBack");
    }

    @Override
    public void setLobby(Lobby lobby) {
        log("newLobbySet");
    }
}
