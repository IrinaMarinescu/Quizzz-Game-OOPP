package client.dependedoncomponents;

import client.scenes.MainCtrl;

/**
 * For testing MainCtrl
 */
public class MainCtrlDOC extends MainCtrl implements SupportsLogging {

    public MainCtrlDOC() {
        clearLogs();
    }

    @Override
    public void halveRemainingTime() {
        log("halve");
    }

    @Override
    public void playerLeavesLobby(String name) {
        log("leave " + name);
    }

    @Override
    public void disconnect() {
        log("disc");
    }

    @Override
    public void startMultiplayerGame() {
        log("game");
    }
}
