package client.utils;

import java.time.Clock;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * This class contains various useful methods for working with time and delays
 * <p>
 * Methods in this class by TimeUtilsDOC to allow for testing of methods involving time
 */
public class TimeUtils {

    /**
     * Runs a method after a provided delay
     *
     * @param method         The method to be run
     * @param delayInSeconds The delay in seconds
     */
    public void runAfterDelay(Runnable method, double delayInSeconds) {
        long delayInMillis = (long) (delayInSeconds * 1000.0);
        CompletableFuture.delayedExecutor(delayInMillis, TimeUnit.MILLISECONDS).execute(method);
    }

    /**
     * Returns the time in milliseconds relative to an arbitrary point in the past
     *
     * @return A representation of time now
     */
    public long now() {
        return Clock.systemDefaultZone().millis();
    }
}
