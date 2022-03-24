package client.scenes;

import static client.scenes.MainCtrl.ROUND_TIME;

import client.scenes.controllerrequirements.QuestionFrameRequirements;
import client.scenes.framecomponents.EmoteCtrl;
import client.scenes.framecomponents.TimerBarCtrl;
import client.utils.ServerUtils;
import client.utils.TimeUtils;
import commons.LeaderboardEntry;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javax.inject.Inject;

/**
 * Controller for questionFrame scene
 */
public class QuestionFrameCtrl implements Initializable, QuestionFrameRequirements {

    public boolean test = false;

    public static final int LEADERBOARD_SIZE_MAX = 5;

    final MainCtrl mainCtrl;
    private final TimerBarCtrl timerBarCtrl;
    private final EmoteCtrl emoteCtrl;
    TimeUtils timeUtils;
    ServerUtils serverUtils;

    @FXML
    public Rectangle topBar;
    @FXML
    VBox sideLeaderboard;
    @FXML
    BorderPane borderPane;
    @FXML
    private Button trophy;
    @FXML
    private Button back;
    @FXML
    private Button emote;
    @FXML
    private VBox reactionContainer;
    @FXML
    HBox emoticonSelectionField;
    @FXML
    private Button scoreField;
    @FXML
    private Text newPoints;
    @FXML
    private Button halveTime;
    @FXML
    private Button eliminateWrongAnswer;
    @FXML
    private Button doublePoints;
    @FXML
    private Button turnIndicator;
    @FXML
    public VBox helpMenuContainer;
    @FXML
    private Text helpPointsGained;
    @FXML
    private Button helpMenuScore;
    @FXML
    private Button helpMenuQuestionNumber;

    @FXML
    private TableView<LeaderboardEntry> leaderboard;
    @FXML
    private TableColumn<LeaderboardEntry, String> playerColumn;
    @FXML
    private TableColumn<LeaderboardEntry, String> scoreColumn;

    private List<Button> jokers;

    boolean isMultiplayerGame;
    private int gameScore;
    private int questionNumber;
    long lastEscapeKeyPressTime;


    /**
     * Injects mainCtrl, lobbyUtils and mainCtrl, so it's possible to call methods from there
     *
     * @param timeUtils    The instance of TimeUtils
     * @param mainCtrl     The instance of MainCtrl
     * @param timerBarCtrl The instance of TimerBarCtrl
     * @param emoteCtrl    The instance of EmoteCtrl
     */
    @Inject
    public QuestionFrameCtrl(MainCtrl mainCtrl, TimerBarCtrl timerBarCtrl, EmoteCtrl emoteCtrl, TimeUtils timeUtils,
                             ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.timerBarCtrl = timerBarCtrl;
        this.emoteCtrl = emoteCtrl;
        this.timeUtils = timeUtils;
        this.serverUtils = serverUtils;
    }

    /**
     * Initializes this controller
     *
     * @param location  Location of this controller
     * @param resources (Potentially) useful resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        jokers = List.of(halveTime, eliminateWrongAnswer, doublePoints);

        topBar.setManaged(false);

        timerBarCtrl.initialize(topBar, timeUtils);
        emoteCtrl.initialize(reactionContainer, timeUtils);

        playerColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getName()));
        scoreColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().scoreToString()));

        lastEscapeKeyPressTime = 0;
    }

    /**
     * Resets the question frame and initializes settings for a new single-player game
     */
    public void initializeSingleplayerGame() {
        startNewGame(false, null);
    }

    /**
     * Resets the question frame and initializes settings for a new multiplayer game
     *
     * @param players The list of all players involved
     */
    public void initializeMultiplayerGame(List<LeaderboardEntry> players) {
        startNewGame(true, players);
    }

    /**
     * Initializes settings for a new game
     *
     * @param isMultiplayerGame Whether this is a multiplayer game
     * @param players           The players involved
     */
    private void startNewGame(boolean isMultiplayerGame, List<LeaderboardEntry> players) {
        this.isMultiplayerGame = isMultiplayerGame;
        this.gameScore = 0;
        this.questionNumber = -1;
        this.lastEscapeKeyPressTime = 0;

        incrementQuestionNumber();

        trophy.setManaged(isMultiplayerGame);
        trophy.setVisible(isMultiplayerGame);
        emote.setVisible(isMultiplayerGame);
        back.setManaged(!isMultiplayerGame);
        back.setVisible(!isMultiplayerGame);
        scoreField.setText("0");

        for (Button joker : jokers) {
            if (joker.getStyleClass().contains("usedJoker")) {
                setJokerEnabled(joker, true);
            }
        }

        if (isMultiplayerGame) {
            setLeaderboardContents(players);
        } else {
            sideLeaderboard.setVisible(false);
            setEmoticonField(false);
            setJokerEnabled(halveTime, false);
        }
    }

    /**
     * Sets node containing question at the center of the frame
     *
     * @param node    The node to be inserted in the center of the frame
     * @param animate Whether to play the timer bar animation
     */
    public void setCenterContent(Node node, boolean animate) {
        Platform.runLater(() -> {
            borderPane.setCenter(node);
            if (animate) {
                setRemainingTime(ROUND_TIME);
                mainCtrl.setQuestionTimeouts(ROUND_TIME);
                Platform.runLater(() -> resizeTimerBar(timerBarCtrl.displayWidth, 0));
            }
        });
    }

    /**
     * Add points to a player's score, as seen in top left
     *
     * @param points The number of points to be added
     */
    public void addPoints(int points) {
        newPoints.setText("+" + points);
        newPoints.setVisible(true);

        gameScore += points;
        scoreField.setText(((Integer) gameScore).toString());

        helpPointsGained.setText("+" + points);
        helpMenuScore.setText(((Integer) gameScore).toString());

        timeUtils.runAfterDelay(() -> newPoints.setVisible(false), 3);
    }

    /**
     * Increments the question number displayed in the top right
     */
    public void incrementQuestionNumber() {
        questionNumber++;
        Platform.runLater(() -> {
            turnIndicator.setText(questionNumber + "/20");
            helpMenuQuestionNumber.setText(questionNumber + "/20");
        });
    }

    /**
     * Sets contents of the pop-up leaderboard
     *
     * @param entries A list of LeaderboardEntry objects representing leaderboard fields
     */
    public List<LeaderboardEntry> setLeaderboardContents(List<LeaderboardEntry> entries) {
        entries = entries.stream()
            .sorted()
            .limit(LEADERBOARD_SIZE_MAX)
            .collect(Collectors.toList());

        if (!test) {
            ObservableList<LeaderboardEntry> data = FXCollections.observableList(entries);
            leaderboard.setItems(data);
        }

        return entries;
    }

    /**
     * Toggles visibility of the side leaderboard
     */
    @FXML
    void toggleLeaderboardVisibility() {
        if (isMultiplayerGame) {
            sideLeaderboard.setVisible(!sideLeaderboard.isVisible());
        }
    }

    /**
     * Toggles help menu visibility
     */
    @FXML
    public void toggleHelpMenuVisibility() {
        helpMenuContainer.setVisible(!helpMenuContainer.isVisible());
    }

    /**
     * Toggles emoticon field visibility
     */
    @FXML
    void toggleEmoticonField() {
        setEmoticonField(!emoticonSelectionField.isVisible());
    }

    /**
     * Turns emoticon field on or off
     *
     * @param visible Whether the emoticon field must become visible
     */
    void setEmoticonField(boolean visible) {
        if (!isMultiplayerGame) {
            return;
        }

        emoticonSelectionField.setVisible(visible);
        if (!test) {
            if (visible) {
                emote.getStyleClass().add("jagged");
            } else {
                emote.getStyleClass().remove("jagged");
            }
        }
    }

    /**
     * Method to be run when a user chooses to send an emoticon
     * <p>
     * TODO: make this send a request to the server, delete placeholders
     */
    @FXML
    private void addReaction(ActionEvent e) {
        serverUtils.sendNewEmoji(mainCtrl.getUsername(), ((Node) e.getSource()).getId());
    }

    /**
     * Adds a new reaction sent by another player to the right
     *
     * @param name     The name of the person sending the emote
     * @param reaction A string representing the emoticon (Accepted values: "happy", "sad", "angry", "surprised")
     */
    public void displayNewEmoji(String name, String reaction) {
        emoteCtrl.addReaction(name, reaction);
    }

    /**
     * Makes timerBar slide from full to empty in a provided number of seconds
     *
     * @param seconds How long the bar should slide
     */
    public void setRemainingTime(double seconds) {
        timerBarCtrl.setRemainingTime(seconds);
    }

    /**
     * Halves the remaining time as seen on the timer bar
     */
    public void halveRemainingTime() {
        timerBarCtrl.halveRemainingTime();
        serverUtils.halveTime(mainCtrl.getGame().getId());
    }

    /**
     * Changes width of timerBar in response to a change in window's size
     *
     * @param newVal New width of window in px
     * @param change Change of width of window in px
     */
    public void resizeTimerBar(int newVal, int change) {
        timerBarCtrl.resize(newVal, change);
    }

    /**
     * Sets whether a joker is enabled
     *
     * @param joker  The Button referring to the joker to be enabled / disabled
     * @param enable Whether to enable the joker
     */
    private void setJokerEnabled(Button joker, boolean enable) {
        Platform.runLater(() -> {
            if (enable) {
                joker.getStyleClass().remove("usedJoker");
                if (!joker.getStyleClass().contains("usedJoker")) {
                    joker.getStyleClass().add("clickableGreen");
                }
            } else {
                joker.getStyleClass().add("usedJoker");
                joker.getStyleClass().remove("clickableGreen");
            }
        });
    }

    /**
     * Method to run when a user uses a joker
     *
     * @param e Information about the joker used
     */
    @FXML
    private void useJoker(ActionEvent e) {
        Button joker = (Button) e.getSource();
        if (joker.getStyleClass().contains("usedJoker")) {
            return;
        }
        setJokerEnabled(joker, false);

        switch (joker.getId()) {
            case "doublePoints":
                mainCtrl.doublePoints();
                break;
            case "eliminateWrongAnswer":
                mainCtrl.eliminateWrongAnswer();
                break;
            case "halveTime":
                // TODO: fill in this joker's function
                break;
            default:
                System.err.println("Unrecognized joker id in QuestionFrameCtrl");
                break;
        }
    }

    /**
     * Temporarily disabled all jokers
     * Meant to be used during overview phase
     *
     * @param duration How long the jokers should remain disabled for
     */
    public void tempDisableJokers(double duration) {
        for (Button joker : jokers) {
            setJokerEnabled(joker, false);
        }

        timeUtils.runAfterDelay(() -> {
            for (Button joker : jokers) {
                setJokerEnabled(joker, true);
            }
        }, duration);
    }

    /**
     * Sets whether the wrong answer joker is enabled
     *
     * @param enabled Whether the wrong answer joker is enabled
     */
    public void setWrongAnswerJoker(boolean enabled) {
        setJokerEnabled(eliminateWrongAnswer, enabled);
    }

    /**
     * Disconnects from the game
     */
    @FXML
    private void disconnect() {
        mainCtrl.toggleModalVisibility();
    }

    /**
     * Provides functionality for keybindings to accelerate certain actions
     *
     * @param e Information about a keypress performed by the user
     *          <p>
     *          This should only be called when initializing the scene
     *          The keypress is passed to the current question controller if no was found
     */
    public void keyPressed(KeyCode e) {
        switch (e) {
            case H:
                useJoker(new ActionEvent(halveTime, null));
                break;
            case E:
                useJoker(new ActionEvent(eliminateWrongAnswer, null));
                break;
            case D:
                useJoker(new ActionEvent(doublePoints, null));
                break;
            case L:
                toggleLeaderboardVisibility();
                break;
            case ESCAPE:
                disconnect();
                break;
            default:
                mainCtrl.currentQuestionCtrl.keyPressed(e);
                break;
        }
    }
}