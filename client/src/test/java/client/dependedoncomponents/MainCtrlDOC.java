package client.dependedoncomponents;

import client.scenes.MainCtrl;
import commons.Lobby;

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
    public void joinLobby() {
        log("joinedLobby");
    }

    @Override
    public void doublePoints() {
        log("doubledPoints");
    }

    @Override
    public void startGame(boolean isMultiplayerGame) {
        log("game");
    }

    @Override
    public String getUsername() {
        return "testName";
    }

    @Override
    public void exitGameChecker(int type) {
        log("exitChecker");
    }

    @Override
    public void showMainFrame() {
        log("mainFrame");
    }

    @Override
    public void disconnect(int type, String buttonID) {
        log("attemptedDisconnect");
    }

    @Override
    public void setLobby(Lobby lobby) {
        log("setLobby");
    }
}
