package client.dependedoncomponents;

import client.scenes.framecomponents.EmoteCtrl;

/**
 * For testing EmoteCtrl
 */
public class EmoteCtrlDOC extends EmoteCtrl implements SupportsLogging {

    public EmoteCtrlDOC() {
        clearLogs();
    }

    @Override
    public void addReaction(String name, String reaction) {
        log(name + reaction);
    }
}
