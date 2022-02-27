package client.dependedoncomponents.questionframe;

import client.dependedoncomponents.SupportsTesting;
import client.scenes.MainCtrl;

public class MainCtrlQuestionFrameDOC extends MainCtrl implements SupportsTesting {

    @Override
    public void disconnect() {
        events.add("disconnect");
    }
}
