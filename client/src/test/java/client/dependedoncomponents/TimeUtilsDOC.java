package client.dependedoncomponents;

import client.utils.TimeUtils;

/**
 * Extends TimeUtils for the purpose of testing using dependency injection / inversion
 * <p>
 * Unlike TimeUtils, the overloaded methods in this class have predictable behavior
 */
public class TimeUtilsDOC extends TimeUtils implements SupportsLogging {

    private long timeNow;
    private final long interval;

    /**
     * Sets interval representing how much time (supposedly) passes between calls to now
     *
     * @param interval The interval of time that (supposedly) passes
     */
    public TimeUtilsDOC(long interval) {
        clearLogs();
        timeNow = 1000;
        this.interval = interval;
    }

    /**
     * This is supposed to mimic the fact that time has passes from when now was called last
     *
     * @return The (artificially generated) time when this method was called
     */
    @Override
    public long now() {
        log("now");
        timeNow += interval;
        return timeNow;
    }

    /**
     * This runs the provided method without any delay
     *
     * @param method      The method to be run
     * @param unusedDelay A field that is necessary to override runAfterDelay
     */
    @Override
    public void runAfterDelay(Runnable method, double unusedDelay) {
        log("run");
        method.run();
    }
}
