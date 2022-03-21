package client.scenes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import client.dependedoncomponents.LeaderboardCtrlDOC;
import commons.LeaderboardEntry;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * This unit test class tests the proper setting of the leaderboard,
 * asserts its max size, its type and its proper sorting of the entries.
 */
class LeaderboardCtrlTest {
    LeaderboardCtrlDOC leaderboardCtrl;
    List<LeaderboardEntry> placeHolderEntries;

    @BeforeEach
    void setUp() {
        leaderboardCtrl = new LeaderboardCtrlDOC(null);
        placeHolderEntries = List.of(
                new LeaderboardEntry("Someone", 100),
                new LeaderboardEntry("test", 500),
                new LeaderboardEntry("Noobmaster69", 20)
        );
        leaderboardCtrl.initialize(placeHolderEntries, 4, "final");
    }

    @Test
    void testInitialize() {
        assertEquals(leaderboardCtrl.getMaxSize(), 4);
        assertEquals(leaderboardCtrl.getType(), leaderboardCtrl.TYPE_FINAL);
    }

    @Test
    void testEntriesSorted() {
        assertEquals(leaderboardCtrl.entries, placeHolderEntries.stream().sorted().collect(Collectors.toList()));
    }

    @Test
    void testEntryLimit() {
        var moreEntries = List.of(
                new LeaderboardEntry("Someone", 100),
                new LeaderboardEntry("test", 500),
                new LeaderboardEntry("Noobmaster69", 20),
                new LeaderboardEntry("Noobmaster69", 20),
                new LeaderboardEntry("Noobmaster69", 20),
                new LeaderboardEntry("Someone", 100)
        );
        leaderboardCtrl.setEntries(moreEntries);
        assertEquals(leaderboardCtrl.entries.size(), 4);
    }
}