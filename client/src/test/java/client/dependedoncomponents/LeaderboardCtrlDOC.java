package client.dependedoncomponents;

import client.scenes.LeaderboardCtrl;
import client.scenes.MainCtrl;
import commons.LeaderboardEntry;
import java.util.List;

/**
 * This class is only used for testing purposes. Contains a list aiming to emulate the TableView.
 */
public class LeaderboardCtrlDOC extends LeaderboardCtrl {
    public List<LeaderboardEntry> entries;

    public LeaderboardCtrlDOC(MainCtrl mainCtrl) {
        super(null);
    }


    @Override
    public void initialize(List<LeaderboardEntry> entries, int maxSize, String type) {
        setMaxSize(maxSize);
        setLeaderboardType(type);
        setEntries(entries);
    }

    public void setEntries(List<LeaderboardEntry> entries) {
        this.entries = sortEntries(entries);
    }
}
