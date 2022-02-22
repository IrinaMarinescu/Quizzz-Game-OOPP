package client.scenes;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import javax.inject.Inject;
import java.net.URL;
import javafx.util.Duration;
import java.util.ResourceBundle;

/**
 * Controller for questionFrame scene
 */
public class QuestionFrameCtrl implements Initializable {

    private final MainCtrl mainCtrl;

    @FXML
    private VBox sideLeaderboard;
    @FXML
    private Rectangle timerBar;
    @FXML
    private Pane centerContent;

    /**
     * Injects necessary dependencies
     * @param mainCtrl - The main front-end controller
     */
    @Inject
    public QuestionFrameCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializes this controller
     * @param location - Location of this controller
     * @param resources - (Potentially) useful resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setRemainingTime(15);
    }

    /**
     * Sets node (ordinarily) containing question screen at the center of the frame
     * @param questionNode - The node to be inserted in the center of the frame
     *
     * This should only be called by MainCtrl!
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
     * @param seconds - How long the bar should slide
     */
    private void setRemainingTime(double seconds) {
        Duration duration = new Duration(seconds * 1000.0);
        TranslateTransition slide = new TranslateTransition(duration, timerBar);
        slide.setByX(-1600);
        slide.play();
    }

    /**
     * Provides functionality for keybindings to accelerate certain actions
     * @param e - Information about a keypress performed by the user
     */
    public void keyPressed(KeyEvent e) {
        switch(e.getCode()) {
            case L:
                toggleLeaderboardVisibility();
                break;
            default:
                break;
        }
    }
}
