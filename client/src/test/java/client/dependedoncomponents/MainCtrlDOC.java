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
    public void disconnect() {
        log("disc");
    }
}
