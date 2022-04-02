package client.scenes.framecomponents;

import client.Main;
import client.MyFXML;
import client.utils.TimeUtils;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * Controller for features involving emotes
 */
public class EmoteCtrl {

    public boolean test = false;

    public static final int MAX_EMOTES = 5;
    public Map<String, String> reactionImage;

    public VBox reactionContainer;
    public final MyFXML loader = Main.getLoader();
    private TimeUtils timeUtils;

    public FadeTransition currentAnimation;
    public int visibleEmotes;

    /**
     * Initializes this class
     *
     * @param reactionContainer The container containing users' emotes
     */
    public void initialize(VBox reactionContainer, TimeUtils timeUtils) {
        this.reactionContainer = reactionContainer;
        this.visibleEmotes = 0;
        this.timeUtils = timeUtils;

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

        if (test) {
            if (reactionContainer.getChildren().size() < MAX_EMOTES) {
                reactionContainer.getChildren().add(0, new Circle());
            }
            return;
        }

        var emoteContainer = loader.load(EmoteContainerCtrl.class, "client/scenes/EmoteContainer.fxml");
        emoteContainer.getKey().initialize(name, pathToImage);

        Platform.runLater(() -> {
            reactionContainer.getChildren().add(0, emoteContainer.getValue());
            if (visibleEmotes == MAX_EMOTES) {
                reactionContainer.getChildren().remove(MAX_EMOTES);
            } else {
                visibleEmotes++;
            }
            timeUtils.runAfterDelay(() -> animate(emoteContainer.getValue()), 5.0);
        });
    }

    public void addJoker(String name, String joker) {
        String pathToImage = "";
        switch (joker) {
            case "HALVE_TIME":
                pathToImage = "client/icons/hourglass-empty-solid.png";
                break;
            case "DOUBLE_POINTS":
                pathToImage = "client/icons/double-points.png";
                break;
            case "ELIMINATE_ANSWER":
                pathToImage = "client/icons/lightbulb-regular.png";
                break;
            default:
        }

        var emoteContainer = loader.load(EmoteContainerCtrl.class, "client/scenes/EmoteContainer.fxml");
        emoteContainer.getKey().initialize(name, pathToImage);

        Platform.runLater(() -> {
            reactionContainer.getChildren().add(0, emoteContainer.getValue());
            if (visibleEmotes == MAX_EMOTES) {
                reactionContainer.getChildren().remove(MAX_EMOTES);
            } else {
                visibleEmotes++;
            }
            timeUtils.runAfterDelay(() -> animate(emoteContainer.getValue()), 5.0);
        });
    }

    /**
     * Animates a provided node with a fade after an initial delay
     *
     * @param emoteField The node to be animated
     */
    void animate(Node emoteField) {
        FadeTransition fade = new FadeTransition(new Duration(3000), emoteField);
        currentAnimation = fade;

        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        if (!test) {
            fade.play();
        }
    }
}
