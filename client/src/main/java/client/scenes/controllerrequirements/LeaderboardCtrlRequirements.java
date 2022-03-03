package client.scenes.controllerrequirements;

import commons.LeaderboardEntry;
import java.util.List;

public interface LeaderboardCtrlRequirements {

    void initialize(List<LeaderboardEntry> entries);

    /**
     *
     * @param type Possible values: ("solo", "intermediate", "final")
     */
    void setLeaderboardType(String type);
}
