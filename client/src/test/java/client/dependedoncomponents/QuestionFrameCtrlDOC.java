package client.dependedoncomponents;

import client.scenes.QuestionFrameCtrl;

/**
 * For testing LongPollingUtils
 */
public class QuestionFrameCtrlDOC extends QuestionFrameCtrl implements SupportsLogging {

    public QuestionFrameCtrlDOC() {
        super(null, null, null, null, null, null);
        clearLogs();
    }

    @Override
    public void displayNewEmoji(String name, String reaction) {
        log(name + " " + reaction);
    }
}
