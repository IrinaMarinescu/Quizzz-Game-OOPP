package client.scenes.framecomponents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import client.dependedoncomponents.TimeUtilsDOC;
import java.util.List;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for TimerBarCtrlTest
 */
public class TimerBarCtrlTest {

    private TimerBarCtrl sut;
    private TimeUtilsDOC timeUtilsDOC;
    private Rectangle rectangleDOC;

    /**
     * Setup for tests
     */
    @BeforeEach
    public void setup() {
        sut = new TimerBarCtrl();
        timeUtilsDOC = new TimeUtilsDOC(100);
        rectangleDOC = new Rectangle();

        sut.initialize(rectangleDOC, timeUtilsDOC);
        sut.test = true;
    }

    @Test
    public void constructor() {
        assertNotNull(sut);
    }

    @Test
    public void nowOnce() {
        assertTrue(timeUtilsDOC.noLogs());

        assertEquals(1100, sut.now());
        assertSame(1, timeUtilsDOC.countLogs());
        assertEquals("now", timeUtilsDOC.getLog(0));
    }

    @Test
    public void nowMultiple() {
        for (int i = 1; i <= 5; i++) {
            assertEquals(1000 + i * 100, sut.now());
        }

        assertEquals(List.of("now", "now", "now", "now", "now"), timeUtilsDOC.logs);
    }

    @Test
    public void animationPlaying() {
        assertFalse(sut.animationPlaying);
        sut.setRemainingTime(3);
        assertTrue(sut.animationPlaying);
    }

    @Test
    public void initializeDependencies() {
        assertSame(rectangleDOC, sut.timerBar);
        assertSame(timeUtilsDOC, sut.timeUtils);
    }

    @Test
    public void initializeAnimation() {
        assertEquals(-1600.0, sut.animation.getByX());
        assertEquals(rectangleDOC, sut.animation.getNode());
        assertEquals(Duration.ZERO, sut.animation.getDuration());
    }

    @Test
    public void initializeFields() {
        assertEquals(Double.MAX_VALUE, sut.currentAnimationStartTime);
        assertEquals(0.0, sut.currentAnimationLength);
    }

    @Test
    public void setRemainingTimeAnimationDone() {
        assertSame(1, sut.timerBar.getTransforms().size());
        sut.currentAnimationStartTime = 0;

        sut.setRemainingTime(7);
        assertSame(1, sut.timerBar.getTransforms().size());
    }

    @Test
    public void setRemainingTimeAnimationNotDone() {
        assertSame(1, sut.timerBar.getTransforms().size());

        sut.setRemainingTime(7);
        assertSame(1, sut.timerBar.getTransforms().size());
    }

    @Test
    public void updateFields() {
        sut.setRemainingTime(7);

        assertEquals(0.0, sut.totalProgress);
        assertEquals(7000, sut.currentAnimationLength);
    }

    @Test
    public void playAnimationFields() {
        sut.playAnimation();

        assertEquals(1100, sut.currentAnimationStartTime);
    }

    @Test
    public void playAnimationAnimation() {
        sut.currentAnimationLength = 2000;
        sut.playAnimation();

        assertEquals(Duration.millis(2000), sut.animation.getDuration());
    }

    @Test
    public void halveRemainingTimeReturn() {
        sut.currentAnimationStartTime = 0;
        sut.halveRemainingTime();

        assertSame(0, timeUtilsDOC.countLogs());
        assertTrue(sut.timerBar.getStyleClass().isEmpty());
    }

    @Test
    public void halveRemainingTimeNoReturn() {
        sut.currentAnimationLength = 3.4;
        sut.halveRemainingTime();

        assertSame(0, sut.timerBar.getStyleClass().size());
    }

    @Test
    public void styleAddedOnlyOnce() {
        sut.currentAnimationLength = 3.4;
        sut.halveRemainingTime();
        sut.halveRemainingTime();

        assertSame(0, sut.timerBar.getStyleClass().size());
    }

    @Test
    public void halveTimeFieldsInitial() {
        sut.currentAnimationStartTime = 900.0;
        sut.currentAnimationLength = 400.0;
        sut.animationPlaying = true;
        sut.halveRemainingTime();

        assertEquals(List.of("now", "now"), timeUtilsDOC.logs);
        assertEquals(0.5, sut.totalProgress);
        assertEquals(200.0, sut.currentAnimationLength);
    }

    @Test
    public void halveFieldsMultiple() {
        sut.currentAnimationStartTime = 900.0;
        sut.currentAnimationLength = 400.0;
        sut.animationPlaying = true;

        sut.halveRemainingTime();
        sut.halveRemainingTime();
        sut.halveRemainingTime();

        // Each call to halveRemainingTime uses now() twice
        assertSame(6, timeUtilsDOC.countLogs("now"));
        assertEquals(50.0, sut.currentAnimationLength);
    }

    @Test
    public void resize() {
        sut.resize(1000, 10);

        assertSame(1, sut.timerBar.getTransforms().size());
        assertEquals(-1000, sut.animation.getByX());
        assertEquals(-2010, sut.relativePos);
    }
}
