package client.scenes.frameComponents;

import client.Main;
import client.MyFXML;
import javafx.animation.FadeTransition;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Controller for features involving emotes
 */
public class EmoteCtrl {

    private static final int MAX_EMOTES = 5;

    private VBox reactionContainer;
    private final MyFXML loader = Main.getLoader();

    private int visibleEmotes;

    /**
     * Initializes this class
     * @param reactionContainer - The container containing users' emotes
     */
    public void initialize(VBox reactionContainer) {
        this.reactionContainer = reactionContainer;
        this.visibleEmotes = 0;
    }

    /**
     * Shows a user's reaction
     * @param name - The name of the user emoting
     * @param reaction - String representation of their chosen emoticon
     */
    public void addReaction(String name, String reaction) {
        String pathToImage = "client/emoticons/";
        switch(reaction) {
            case "happy":
                pathToImage += "face-grin-squint-solid.png";
                break;
            case "sad":
                pathToImage += "face-sad-cry-solid.png";
                break;
            case "angry":
                pathToImage += "face-angry-solid.png";
                break;
            case "surprised":
                pathToImage += "face-flushed-solid.png";
                break;
        }

        var emoteContainer = loader.load(EmoteContainerCtrl.class, "client/scenes/EmoteContainer.fxml", null);
        emoteContainer.getKey().initialize(name, pathToImage);
        reactionContainer.getChildren().add(0, emoteContainer.getValue());

        if(visibleEmotes == MAX_EMOTES) reactionContainer.getChildren().remove(MAX_EMOTES);
        else visibleEmotes++;

        animate(emoteContainer.getValue(), 5, 3);
    }

    /**
     * Creates a fade-out animation after a provided delay
     * @param container - The container to be animated
     * @param delay - The initial delay before the fade-out starts
     * @param fadeTime - The time it takes for the container to fade out
     */
    private void animate(Parent container, int delay, int fadeTime) {
        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(delay);
                FadeTransition fade = new FadeTransition(new Duration(fadeTime * 1000), container);
                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.play();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("An error with the fade-out animation transition has occured");
            }
        }).start();
    }
}
