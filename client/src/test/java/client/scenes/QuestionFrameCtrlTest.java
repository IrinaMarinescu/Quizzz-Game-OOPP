package client.scenes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import client.dependedoncomponents.questionframe.MainCtrlQuestionFrameDOC;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class QuestionFrameCtrlTest {

    private QuestionFrameCtrl sut;
    private MainCtrlQuestionFrameDOC mainCtrlDOC;

    @BeforeEach
    public void setup() {
        mainCtrlDOC = new MainCtrlQuestionFrameDOC();

        sut = new QuestionFrameCtrl(mainCtrlDOC);

        sut.sideLeaderboard = new VBox();
        sut.timerBar = new Rectangle();
        sut.centerContent = new Pane();
    }

    @Test
    public void constructor() {
        assertNotNull(sut);
    }

    @Test
    public void setCenterContent() {
        assertTrue(sut.centerContent.getChildren().isEmpty());
        sut.setCenterContent(new Circle());
        assertSame(1, sut.centerContent.getChildren().size());
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
    public void setRemainingTime() {
        sut.setRemainingTime(10, false);
        assertNotNull(sut.slide);
        assertEquals(Duration.seconds(10), sut.slide.getDuration());
        assertEquals(-1600, sut.slide.getByX());
    }

    @Test
    public void keyEvent() {
        assertTrue(sut.sideLeaderboard.isVisible());
        sut.keyPressed(KeyCode.L);
        assertFalse(sut.sideLeaderboard.isVisible());
    }

    @Test
    public void disconnect() {
        sut.disconnect();
        assertSame(1, mainCtrlDOC.events.size());
        assertEquals("disconnect", mainCtrlDOC.events.get(0));
    }
}
