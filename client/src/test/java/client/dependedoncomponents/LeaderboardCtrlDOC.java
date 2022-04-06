package client.dependedoncomponents;

import client.scenes.LeaderboardCtrl;
import client.scenes.MainCtrl;
import client.scenes.MainFrameCtrl;
import client.utils.LobbyUtils;
import client.utils.ServerUtils;
import commons.LeaderboardEntry;
import java.util.List;

/**
 * This class is only used for testing purposes. Contains a list aiming to emulate the TableView.
 */
public class LeaderboardCtrlDOC extends LeaderboardCtrl {
    public List<LeaderboardEntry> entries;

    public LeaderboardCtrlDOC(
            MainCtrl mainCtrl, ServerUtils serverUtils,
            LobbyUtils lobbyUtils, MainFrameCtrl mainFrameCtrl) {
        super(mainCtrl, serverUtils, lobbyUtils, mainFrameCtrl);
        test = true;
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
