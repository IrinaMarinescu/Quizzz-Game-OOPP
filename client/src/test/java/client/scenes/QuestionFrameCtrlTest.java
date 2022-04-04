package client.scenes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import client.dependedoncomponents.EmoteCtrlDOC;
import client.dependedoncomponents.GameUtilsDOC;
import client.dependedoncomponents.MainCtrlDOC;
import client.dependedoncomponents.TimeUtilsDOC;
import client.dependedoncomponents.TimerBarCtrlDOC;
import commons.LeaderboardEntry;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuestionFrameCtrlTest {

    private QuestionFrameCtrl sut;
    private MainCtrlDOC mainCtrlDOC;
    private TimeUtilsDOC timeUtilsDOC;
    private EmoteCtrlDOC emoteCtrlDOC;
    private TimerBarCtrlDOC timerBarCtrlDOC;
    private GameUtilsDOC gameUtilsDOC;

    /**
     * Setup for tests
     */
    @BeforeEach
    public void setup() {
        timeUtilsDOC = new TimeUtilsDOC(150);
        mainCtrlDOC = new MainCtrlDOC();
        emoteCtrlDOC = new EmoteCtrlDOC();
        timerBarCtrlDOC = new TimerBarCtrlDOC();
        gameUtilsDOC = new GameUtilsDOC();

        sut = new QuestionFrameCtrl(null, timeUtilsDOC, gameUtilsDOC, mainCtrlDOC, timerBarCtrlDOC, emoteCtrlDOC);

        sut.test = true;
        sut.sideLeaderboard = new VBox();
        sut.emoticonSelectionField = new HBox();
        sut.isMultiplayerGame = true;
        sut.helpMenuContainer = new VBox();
        sut.borderPane = new BorderPane();
    }

    @Test
    public void constructor() {
        assertNotNull(sut);
        assertSame(mainCtrlDOC, sut.mainCtrl);
    }

    @Test
    public void setLeaderboardContentsOne() {
        List<LeaderboardEntry> entries = List.of(new LeaderboardEntry("James", 1));
        sut.setLeaderboardContents(entries);

        assertEquals(List.of(new LeaderboardEntry("James", 1)), entries);
    }

    @Test
    public void setLeaderboardContentsSort() {
        LeaderboardEntry someone = new LeaderboardEntry("Someone", 2);
        List<LeaderboardEntry> entries = List.of(new LeaderboardEntry("James", 1), someone);
        entries = sut.setLeaderboardContents(entries);

        assertEquals(entries.get(0), someone);
    }

    @Test
    public void toggleLeaderboardVisibility() {
        assertTrue(sut.sideLeaderboard.isVisible());
        sut.toggleLeaderboardVisibility();
        assertFalse(sut.sideLeaderboard.isVisible());
        sut.toggleLeaderboardVisibility();
        assertTrue(sut.sideLeaderboard.isVisible());
    }

    @Test
    public void toggleHelpMenuVisibility() {
        sut.toggleHelpMenuVisibility();
        sut.toggleHelpMenuVisibility();
        assertTrue(sut.helpMenuContainer.isVisible());
        sut.toggleHelpMenuVisibility();
        assertFalse(sut.helpMenuContainer.isVisible());
    }

    @Test
    public void setEmoticonFieldSingleplayer() {
        sut.isMultiplayerGame = false;
        sut.setEmoticonField(false);
        assertTrue(sut.emoticonSelectionField.isVisible());

        sut.isMultiplayerGame = true;
        sut.setEmoticonField(false);
        assertFalse(sut.emoticonSelectionField.isVisible());
    }

    @Test
    public void toggleEmoticonField() {
        assertTrue(sut.emoticonSelectionField.isVisible());
        sut.toggleEmoticonField();
        assertFalse(sut.emoticonSelectionField.isVisible());
        sut.toggleEmoticonField();
        assertTrue(sut.emoticonSelectionField.isVisible());
    }

    @Test
    public void setEmoticonField() {
        sut.setEmoticonField(true);
        assertTrue(sut.emoticonSelectionField.isVisible());
        sut.setEmoticonField(true);
        assertTrue(sut.emoticonSelectionField.isVisible());
        sut.setEmoticonField(false);
        assertFalse(sut.emoticonSelectionField.isVisible());
        sut.setEmoticonField(true);
        assertTrue(sut.emoticonSelectionField.isVisible());
    }

    @Test
    void addReaction() {
        Rectangle source = new Rectangle();
        source.setId("testId");

        ActionEvent e = new ActionEvent(source, null);
        sut.addReaction(e);

        assertSame(1, gameUtilsDOC.countLogs("EMOJItestNametestId"));
        assertSame(1, gameUtilsDOC.countLogs());
    }

    @Test
    public void displayNewEmoji() {
        sut.displayNewEmoji("hello", "world");
        sut.displayNewEmoji("Lorem", "Ipsum");
        sut.displayNewEmoji("Best", "Test");

        assertEquals(List.of("helloworld", "LoremIpsum", "BestTest"), emoteCtrlDOC.logs);
    }

    @Test
    public void displayNewJoker() {
        sut.displayNewJoker("hello", "world");

        assertEquals(List.of("Jokerhelloworld"), emoteCtrlDOC.logs);
    }

    @Test
    public void setRemainingTime() {
        sut.setRemainingTime(123);
        sut.setRemainingTime(322);
        assertEquals(Double.toString(123), timerBarCtrlDOC.getLog(0));
        assertSame(2, timerBarCtrlDOC.countLogs());
    }

    @Test
    public void halveRemainingTime() {
        sut.halveRemainingTime();
        sut.halveRemainingTime();
        sut.halveRemainingTime();

        assertSame(3, timerBarCtrlDOC.countLogs());
        assertSame(3, timerBarCtrlDOC.countLogs("halve"));
    }

    @Test
    public void resizeTimerBar() {
        assertTrue(timerBarCtrlDOC.noLogs());
        sut.resizeTimerBar(1.6);
        assertEquals("resize" + 1.6, timerBarCtrlDOC.getLog(0));
    }

    @Test
    void noGameDisconnect() {
        mainCtrlDOC.gameOngoing = true;
        sut.disconnect();
        assertEquals("exitChecker", mainCtrlDOC.getLog(0));
    }

    @Test
    void gamePresentDisconnect() {
        mainCtrlDOC.gameOngoing = false;
        sut.disconnect();
        assertEquals("mainFrame", mainCtrlDOC.getLog(0));
    }

    @Test
    public void keyL() {
        assertTrue(sut.sideLeaderboard.isVisible());
        sut.keyPressed(KeyCode.L);
        assertFalse(sut.sideLeaderboard.isVisible());
        sut.keyPressed(KeyCode.L);
        assertTrue(sut.sideLeaderboard.isVisible());
    }

    @Test
    void keyESCAPE() {
        noGameDisconnect();
    }
}