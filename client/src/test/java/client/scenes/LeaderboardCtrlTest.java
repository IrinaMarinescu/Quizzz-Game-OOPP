package client.scenes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import client.dependedoncomponents.LeaderboardCtrlDOC;
import client.dependedoncomponents.MainCtrlDOC;
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
        leaderboardCtrl = new LeaderboardCtrlDOC(new MainCtrlDOC());
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
        assertEquals(leaderboardCtrl.getType(), LeaderboardCtrl.TYPE_FINAL);
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

    @Test
    void testMedalGold() {
        assertEquals(LeaderboardCtrl.FIRST_PLACE, leaderboardCtrl.getColor(0));
    }

    @Test
    void testMedalSilver() {
        assertEquals(LeaderboardCtrl.SECOND_PLACE, leaderboardCtrl.getColor(1));
    }

    @Test
    void testMedalBronze() {
        assertEquals(LeaderboardCtrl.THIRD_PLACE, leaderboardCtrl.getColor(2));
    }

    @Test
    void testTypeSet() {
        leaderboardCtrl.setLeaderboardType("solo");
        assertEquals("Global Leaderboard", leaderboardCtrl.texts.getKey());
    }

    @Test
    void testShowBack() {
        leaderboardCtrl.setLeaderboardType("solo");
        assertTrue(leaderboardCtrl.backButtonVisible);
        assertFalse(leaderboardCtrl.buttonGridVisible);
    }

    @Test
    void testShowGrid() {
        leaderboardCtrl.setLeaderboardType("final");
        assertFalse(leaderboardCtrl.backButtonVisible);
        assertTrue(leaderboardCtrl.buttonGridVisible);
    }

    @Test
    void testHideAll() {
        leaderboardCtrl.setLeaderboardType("intermediate");
        assertFalse(leaderboardCtrl.backButtonVisible);
        assertFalse(leaderboardCtrl.buttonGridVisible);
    }

    @Test
    void testGetType() {
        leaderboardCtrl.setType(LeaderboardCtrl.TYPE_INTERMED);
        assertEquals(LeaderboardCtrl.TYPE_INTERMED, leaderboardCtrl.getType());
    }
}