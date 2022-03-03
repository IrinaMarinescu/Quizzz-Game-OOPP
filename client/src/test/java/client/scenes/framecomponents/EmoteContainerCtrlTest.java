package client.scenes.framecomponents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for EmoteContainerCtrl
 */
public class EmoteContainerCtrlTest {

    private EmoteContainerCtrl sut;

    /**
     * Setup for tests
     */
    @BeforeEach
    public void setup() {
        sut = new EmoteContainerCtrl();

        sut.emoticon = new ImageView();
        sut.person = new Text();
    }

    @Test
    public void constructor() {
        assertNotNull(sut);
    }

    @Test
    public void initialize() {
        sut.initialize("James", "none");

        assertEquals("James", sut.person.getText());
    }

    @Test
    public void urlResourceExists() {
        try {
            sut.initialize("James", "client/emoticons/face-angry-solid.png");
        } catch (RuntimeException e) {

            // if the URL is invalid, the exception just a simple RuntimeException
            assertFalse(e instanceof IllegalArgumentException);
        }
    }
}
