package client.scenes;

import client.scenes.frameComponents.TimerBarCtrl;
import com.sun.tools.jconsole.JConsoleContext;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for questionFrame scene
 */
public class QuestionFrameCtrl implements Initializable {

    private final MainCtrl mainCtrl;
    private final TimerBarCtrl timerBarCtrl;

    @FXML
    public Rectangle timerBar;
    @FXML
    private VBox sideLeaderboard;
    @FXML
    private Pane centerContent;
    @FXML
    private ImageView trophy;
    @FXML
    private Button emote;

    private boolean isMultiplayerGame;

    /**
     * Injects necessary dependencies
     * @param mainCtrl - The main front-end controller
     */
    @Inject
    public QuestionFrameCtrl(MainCtrl mainCtrl, TimerBarCtrl timerBarCtrl) {
        this.mainCtrl = mainCtrl;
        this.timerBarCtrl = timerBarCtrl;
    }

    /**
     * Initializes this controller
     * @param location - Location of this controller
     * @param resources - (Potentially) useful resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setMultiplayerFeatures(true);
        timerBarCtrl.initialize(timerBar);

        setRemainingTime(10);
    }

    /**
     * Changes whether leaderboard
     * @param isMultiplayerGame - Whether this is a multiplayer game
     */
    public void setMultiplayerFeatures(boolean isMultiplayerGame) {
        this.isMultiplayerGame = isMultiplayerGame;

        trophy.setManaged(isMultiplayerGame);
        trophy.setVisible(isMultiplayerGame);
        emote.setVisible(isMultiplayerGame);

        if(!isMultiplayerGame) {
            sideLeaderboard.setVisible(false);
        }
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
        if(isMultiplayerGame) sideLeaderboard.setVisible(!sideLeaderboard.isVisible());
    }

    /**
     * Halves the time remaining as shown by the timer bar
     */
    @FXML
    void halveTime() {
        if(!timerBarCtrl.animationDone()) timerBarCtrl.halveTime();
    }

    /**
     * Temporary function to demonstrate working of timer bar
     */
    @FXML
    void demoTimer() {
        setRemainingTime(60);
    }

    /**
     * Makes timerBar slide from full to empty in a provided number of seconds
     * @param seconds - How long the bar should slide
     */
    void setRemainingTime(double seconds) {
        timerBarCtrl.setRemainingTime(seconds);
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
