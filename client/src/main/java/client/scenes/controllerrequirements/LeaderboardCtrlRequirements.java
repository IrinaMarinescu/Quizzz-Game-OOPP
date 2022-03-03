package client.scenes.controllerrequirements;

import commons.LeaderboardEntry;

import java.util.List;

public interface LeaderboardCtrlRequirements {

    /**
     * <p>The <code>initialize</code> function will fill the leaderboard table with entries and also set the max size and the type of leaderboard to be shown.</p>
     * <p>Other private, helper methods are implemented in LeaderboardCtrl. There should be no need to call on those.</p>
     * @param entries a List of instances of LeaderboardEntry.
     * @param maxSize the maximum size of the leaderboard - how many records to show. if <code>entries</code> has more elements than that, the function trims automatically.
     * @param type the type of the leaderboard. can either be "solo", "intermediate", "final".
     * @see commons.LeaderboardEntry
     * @see client.scenes.LeaderboardCtrl
     */
    void initialize(List<LeaderboardEntry> entries, int maxSize, String type);
}
