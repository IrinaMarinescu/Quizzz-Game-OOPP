package client.scenes;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javax.inject.Inject;

/**
 * Controller for questionFrame scene
 */
public class QuestionFrameCtrl implements Initializable {

    private final MainCtrl mainCtrl;

    @FXML
    public VBox sideLeaderboard;
    @FXML
    public Rectangle timerBar;
    @FXML
    public Pane centerContent;

    public TranslateTransition slide;

    /**
     * Injects necessary dependencies
     *
     * @param mainCtrl - The main front-end controller
     */
    @Inject
    public QuestionFrameCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializes this controller
     *
     * @param location  - Location of this controller
     * @param resources - (Potentially) useful resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setRemainingTime(15, true);
    }

    /**
     * Sets node (ordinarily) containing question screen at the center of the frame
     *
     * @param questionNode - The node to be inserted in the center of the frame
     *                     <p>
     *                     This should only be called by MainCtrl!
     */
    public void setCenterContent(Node questionNode) {
        centerContent.getChildren().clear();
        centerContent.getChildren().add(questionNode);
    }

    /**
     * Toggles visibility of the side leaderboard
     */
    @FXML
    void toggleLeaderboardVisibility() {
        sideLeaderboard.setVisible(!sideLeaderboard.isVisible());
    }

    /**
     * Makes timerBar slide from full to empty in a provided number of seconds
     *
     * @param seconds - How long the bar should slide
     */
    public void setRemainingTime(double seconds, boolean play) {
        Duration duration = new Duration(seconds * 1000.0);
        slide = new TranslateTransition(duration, timerBar);
        slide.setByX(-1600);
        if (play) {
            slide.play();
        }
    }

    /**
     * Provides functionality for keybindings to accelerate certain actions
     *
     * @param e - Information about a keypress performed by the user
     */
    public void keyPressed(KeyCode e) {
        switch (e) {
            case L:
                toggleLeaderboardVisibility();
                break;
            default:
                break;
        }
    }

    public void disconnect() {
        mainCtrl.disconnect();
    }
}
