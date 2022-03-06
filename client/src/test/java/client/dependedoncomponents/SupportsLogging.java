package client.dependedoncomponents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This interface adds event logging as a way to test
 * <p>
 * The method log should be used by DOCs of the SUT to record an event that occurred
 */
public interface SupportsLogging {
    List<String> logs = new ArrayList<>();

    /**
     * Clears all logs
     */
    default void clearLogs() {
        logs.clear();
    }

    /**
     * Logs a new event
     *
     * @param event A string describing the event that occurred
     */
    default void log(String event) {
        logs.add(event);
    }

    /**
     * @return Whether no events have been logged
     */
    default boolean noLogs() {
        return logs.isEmpty();
    }

    /**
     * Returns the logged event at a particular index
     *
     * @param index The index at which to return the event
     * @return A string representing the event that took place
     */
    default String getLog(int index) {
        return logs.get(index);
    }

    /**
     * @return The number of events that have been logged
     */
    default int countLogs() {
        return logs.size();
    }

    /**
     * @param event The event whose occurrences to count
     * @return The number of occurrences
     */
    default int countLogs(String event) {
        return Collections.frequency(logs, event);
    }
}