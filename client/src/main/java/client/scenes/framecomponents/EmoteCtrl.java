package client.scenes.framecomponents;

import client.Main;
import client.MyFXML;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controller for features involving emotes
 */
public class EmoteCtrl {

    private static final int MAX_EMOTES = 5;
    private Map<String, String> reactionImage;

    private VBox reactionContainer;
    private final MyFXML loader = Main.getLoader();

    private int visibleEmotes;

    /**
     * Initializes this class
     *
     * @param reactionContainer The container containing users' emotes
     */
    public void initialize(VBox reactionContainer) {
        this.reactionContainer = reactionContainer;
        this.visibleEmotes = 0;

        reactionImage = Stream.of(new String[][] {
            {"happy", "face-grin-squint-solid.png"},
            {"sad", "face-sad-cry-solid.png"},
            {"angry", "face-angry-solid.png"},
            {"surprised", "face-flushed-solid.png"}
        }).collect(Collectors.toMap(d -> d[0], d -> d[1]));
    }

    /**
     * Shows a user's reaction
     *
     * @param name     The name of the user emoting
     * @param reaction String representation of their chosen emoticon
     */
    public void addReaction(String name, String reaction) {
        String pathToImage = "client/emoticons/" + reactionImage.get(reaction);

        var emoteContainer = loader.load(EmoteContainerCtrl.class, "client/scenes/EmoteContainer.fxml", null);
        emoteContainer.getKey().initialize(name, pathToImage);
        Platform.runLater(() -> {
            reactionContainer.getChildren().add(0, emoteContainer.getValue());
            if (visibleEmotes == MAX_EMOTES) {
                reactionContainer.getChildren().remove(MAX_EMOTES);
            } else {
                visibleEmotes++;
            }
        });

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                FadeTransition fade = new FadeTransition(new Duration(3000), emoteContainer.getValue());
                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.play();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.err.println("InterruptError at emote fadeAway animation");
            }
        }).start();
    }
}
