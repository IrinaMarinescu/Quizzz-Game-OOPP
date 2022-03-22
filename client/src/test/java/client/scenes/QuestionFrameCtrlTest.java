package client.scenes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import client.dependedoncomponents.EmoteCtrlDOC;
import client.dependedoncomponents.MainCtrlDOC;
import client.dependedoncomponents.TimeUtilsDOC;
import client.dependedoncomponents.TimerBarCtrlDOC;
import commons.LeaderboardEntry;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Sadly, fully testing this class has proven impossible.
 * <p>
 * Most methods work heavily with FXML;
 * Button elements (fields) marked with @FXML cannot be assigned new values without causing an error.
 * <p>
 * Using FXMLLoader is not possible because an injector cannot be set up to target QuestionFrameCtrlTest
 * <p>
 * Most methods in this class are very simplistic FXML manipulations, unlikely to be buggy.
 * <p>
 * Most complex logic is extracted into 3 other test classes, which are tested thoroughly:
 * - EmoteContainerCtrlTest
 * - EmoteCtrlTest
 * - TimerBarCtrlTest
 */

class QuestionFrameCtrlTest {

    private QuestionFrameCtrl sut;
    private MainCtrlDOC mainCtrlDOC;
    private TimeUtilsDOC timeUtilsDOC;
    private EmoteCtrlDOC emoteCtrlDOC;
    private TimerBarCtrlDOC timerBarCtrlDOC;

    /**
     * Setup for tests
     */
    @BeforeEach
    public void setup() {
        timeUtilsDOC = new TimeUtilsDOC(150);
        mainCtrlDOC = new MainCtrlDOC();
        emoteCtrlDOC = new EmoteCtrlDOC();
        timerBarCtrlDOC = new TimerBarCtrlDOC();

        sut = new QuestionFrameCtrl(null, timeUtilsDOC, null, mainCtrlDOC, timerBarCtrlDOC, emoteCtrlDOC);

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

    /*
    @Test
    public void setCenterContentOne() {
        assertNull(sut.borderPane.getCenter());
        sut.setCenterContent(new Circle());
        assertEquals(Circle.class, sut.borderPane.getCenter().getClass());
    }

    @Test
    public void setCenterContentMultiple() {
        assertNull(sut.borderPane.getCenter());
        sut.setCenterContent(new Circle());
        sut.setCenterContent(new Circle());
        sut.setCenterContent(new Rectangle());
        assertEquals(Rectangle.class, sut.borderPane.getCenter().getClass());
    }
     */

    @Test
    public void setLeaderboardContentsOne() {
        List<LeaderboardEntry> entries = List.of(new LeaderboardEntry("James", 1));
        sut.setLeaderboardContents(entries);

        assertEquals(List.of(new LeaderboardEntry("James", 1)), entries);
    }

    @Test
    public void setLeaderboardContentsEdge() {
        List<LeaderboardEntry> entries = new ArrayList<>();
        for (int i = 0; i < QuestionFrameCtrl.LEADERBOARD_SIZE_MAX; i++) {
            entries.add(new LeaderboardEntry("James", 1));
        }
        sut.setLeaderboardContents(entries);

        assertSame(QuestionFrameCtrl.LEADERBOARD_SIZE_MAX, entries.size());
    }

    @Test
    public void setLeaderboardContentsDelete() {
        List<LeaderboardEntry> entries = new ArrayList<>();
        for (int i = 0; i < QuestionFrameCtrl.LEADERBOARD_SIZE_MAX + 10; i++) {
            entries.add(new LeaderboardEntry("James", 1));
        }
        entries = sut.setLeaderboardContents(entries);

        assertSame(QuestionFrameCtrl.LEADERBOARD_SIZE_MAX, entries.size());
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

    //    TODO: test sending reactions when serverUtils is in place

    @Test
    public void displayNewEmoji() {
        sut.displayNewEmoji("hello", "world");
        sut.displayNewEmoji("Lorem", "Ipsum");
        sut.displayNewEmoji("Best", "Test");

        assertEquals(List.of("helloworld", "LoremIpsum", "BestTest"), emoteCtrlDOC.logs);
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
    /*
        sut.halveRemainingTime();
        sut.halveRemainingTime();
        sut.halveRemainingTime();

        assertSame(3, timerBarCtrlDOC.countLogs());
        assertSame(3, timerBarCtrlDOC.countLogs("halve"));
    */
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
    public void twoEscapeTrue() {
        sut.keyPressed(KeyCode.ESCAPE);
        sut.keyPressed(KeyCode.ESCAPE);

        assertSame(1, mainCtrlDOC.countLogs("disc"));
    }

    @Test
    public void escapeFalse() {
        sut.timeUtils = new TimeUtilsDOC(201);
        for (int i = 0; i < 10; i++) {
            sut.keyPressed(KeyCode.ESCAPE);
        }

        assertSame(0, mainCtrlDOC.countLogs("disc"));
        assertSame(10, mainCtrlDOC.countLogs("now"));
    }
}