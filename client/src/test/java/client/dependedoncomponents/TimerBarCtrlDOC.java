package client.dependedoncomponents;

import client.scenes.framecomponents.TimerBarCtrl;

/**
 * For testing TimerBarCtrl
 */
public class TimerBarCtrlDOC extends TimerBarCtrl implements SupportsLogging {

    public TimerBarCtrlDOC() {
        clearLogs();
    }

    @Override
    public void setRemainingTime(double seconds) {
        log(Double.toString(seconds));
    }

    @Override
    public void halveRemainingTime() {
        log("halve");
    }

    @Override
    public void resize(double value) {
        log("resize" + value);
    }
}
