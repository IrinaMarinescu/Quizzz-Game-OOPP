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
    public void halveTime() {
        log("halve");
    }

    @Override
    public void toggleModalVisibility() {
        log("modal");
    }

    @Override
    public void startMultiplayerGame() {
        log("game");
    }
}
