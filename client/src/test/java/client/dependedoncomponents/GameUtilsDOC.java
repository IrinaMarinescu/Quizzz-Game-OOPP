package client.dependedoncomponents;

import client.utils.GameUtils;

public class GameUtilsDOC extends GameUtils implements SupportsLogging {

    /**
     * Injects serverUtils and mainCtrl, so it's possible to call methods from there
     */
    public GameUtilsDOC() {
        super(null, null);
        clearLogs();
    }

    @Override
    public void sendFeature(String type, String name, String value) {
        log(type + name + value);
    }
}
