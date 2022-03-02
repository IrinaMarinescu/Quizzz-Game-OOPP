package client.scenes.framecomponents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import client.MyFXML;
import client.utils.TimeUtils;
import javafx.animation.FadeTransition;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for EmoteCtrlTest
 */
public class EmoteCtrlTest {

    private EmoteCtrl sut;

    /**
     * Setup for tests
     */
    @BeforeEach
    public void setup() {
        sut = new EmoteCtrl();

        sut.initialize(new VBox(), new TimeUtils());
        sut.test = true;
    }

    @Test
    public void constructor() {
        assertNotNull(sut);
        assertSame(sut.MAX_EMOTES, EmoteCtrl.MAX_EMOTES);
    }

    @Test
    public void loaderRetrieved() {
        assertEquals(MyFXML.class, sut.loader.getClass());
    }

    @Test
    public void initialize() {
        assertNotNull(sut.reactionContainer);
        assertSame(0, sut.visibleEmotes);
        assertTrue(sut.reactionContainer.getChildren().isEmpty());
    }

    @Test
    public void reactionStringToUrl() {
        assertEquals("face-grin-squint-solid.png", sut.reactionImage.get("happy"));
        assertEquals("face-sad-cry-solid.png", sut.reactionImage.get("sad"));
        assertEquals("face-angry-solid.png", sut.reactionImage.get("angry"));
        assertEquals("face-flushed-solid.png", sut.reactionImage.get("surprised"));
    }

    @Test
    public void addOneReaction() {
        sut.addReaction("James", "happy");

        assertSame(1, sut.reactionContainer.getChildren().size());
    }

    @Test
    public void addLimitReaction() {
        for (int i = 0; i < EmoteCtrl.MAX_EMOTES; i++) {
            sut.addReaction("James", "happy");
        }

        assertSame(EmoteCtrl.MAX_EMOTES, sut.reactionContainer.getChildren().size());
    }

    @Test
    public void addExtremeReaction() {
        for (int i = 0; i < EmoteCtrl.MAX_EMOTES + 10; i++) {
            sut.addReaction("James", "happy");
        }

        assertSame(EmoteCtrl.MAX_EMOTES, sut.reactionContainer.getChildren().size());
    }

    @Test
    public void animate() {
        sut.animate(new Circle());

        assertEquals(FadeTransition.class, sut.currentAnimation.getClass());
        assertEquals(Duration.seconds(3), sut.currentAnimation.getDuration());
        assertEquals(1.0, sut.currentAnimation.getFromValue());
        assertEquals(0.0, sut.currentAnimation.getToValue());
    }
}
