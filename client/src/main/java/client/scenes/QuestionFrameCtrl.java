package client.scenes;

import static client.scenes.MainCtrl.ROUND_TIME;

import client.scenes.controllerrequirements.QuestionFrameRequirements;
import client.scenes.framecomponents.EmoteCtrl;
import client.scenes.framecomponents.TimerBarCtrl;
import client.utils.GameUtils;
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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javax.inject.Inject;

/**
 * Controller for questionFrame scene
 */
public class QuestionFrameCtrl implements Initializable, QuestionFrameRequirements {

    public boolean test = false;

    final ServerUtils serverUtils;
    TimeUtils timeUtils;
    private final GameUtils gameUtils;
    final MainCtrl mainCtrl;
    private final TimerBarCtrl timerBarCtrl;
    private final EmoteCtrl emoteCtrl;

    @FXML
    public ProgressBar topBar;
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
    @FXML
    private TableColumn<LeaderboardEntry, String> gainColumn;

    private List<Button> jokers;

    boolean isMultiplayerGame;
    private int gameScore;
    int questionNumber;
    private List<LeaderboardEntry> previousEntries;

    /**
     * Injects mainCtrl, lobbyUtils and mainCtrl, so it's possible to call methods from there
     *
     * @param timeUtils    The instance of TimeUtils
     * @param mainCtrl     The instance of MainCtrl
     * @param timerBarCtrl The instance of TimerBarCtrl
     * @param emoteCtrl    The instance of EmoteCtrl
     */
    @Inject
    public QuestionFrameCtrl(ServerUtils serverUtils, TimeUtils timeUtils, GameUtils gameUtils, MainCtrl mainCtrl,
                             TimerBarCtrl timerBarCtrl, EmoteCtrl emoteCtrl) {

        this.serverUtils = serverUtils;
        this.timeUtils = timeUtils;
        this.gameUtils = gameUtils;

        this.mainCtrl = mainCtrl;
        this.timerBarCtrl = timerBarCtrl;
        this.emoteCtrl = emoteCtrl;
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

        timerBarCtrl.initialize(topBar, timeUtils);
        emoteCtrl.initialize(reactionContainer, timeUtils);

        playerColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getName()));
        scoreColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().scoreToString()));
        gainColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().gainToString()));
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
        previousEntries = players;
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

        incrementQuestionNumber();

        trophy.setManaged(isMultiplayerGame);
        trophy.setVisible(isMultiplayerGame);
        emote.setVisible(isMultiplayerGame);
        back.setManaged(!isMultiplayerGame);
        back.setVisible(!isMultiplayerGame);
        scoreField.setText("0");
        helpMenuScore.setText("0");
        helpPointsGained.setText("+0");
        toggleJokerUsability(true, true);
        emoteCtrl.reset();

        if (isMultiplayerGame) {
            setLeaderboardContents(players);
        } else {
            sideLeaderboard.setVisible(false);
            setEmoticonField(false);
            setJokerEnabled(halveTime, false, false);
        }
    }

    /**
     * Sets node containing question at the center of the frame
     *
     * @param node           The node to be inserted in the center of the frame
     * @param isQuestionNode Whether to play the timer bar animation
     */
    public void setCenterContent(Node node, boolean isQuestionNode) {
        Platform.runLater(() -> {
            borderPane.setCenter(node);
            if (isQuestionNode) {
                setRemainingTime(ROUND_TIME - (timeUtils.now() - mainCtrl.questionStartTime) / 1000.0);
                mainCtrl.questionStartTime = timeUtils.now();
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

        if (points != 0) {
            gameUtils.sendFeature("SCORE", mainCtrl.getUsername(), Integer.toString(gameScore));
        }
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
        entries = entries.stream().sorted().collect(Collectors.toList());
        previousEntries = entries;

        if (!test) {
            ObservableList<LeaderboardEntry> data = FXCollections.observableList(entries);
            leaderboard.setItems(data);
            leaderboard.refresh();
        }

        timeUtils.runAfterDelay(() -> {
            if (!test) {
                previousEntries.forEach(LeaderboardEntry::resetGain);
                leaderboard.setItems(FXCollections.observableList(previousEntries));
                leaderboard.refresh();
            }
        }, MainCtrl.OVERVIEW_TIME);
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
     */
    @FXML
    void addReaction(ActionEvent e) {
        gameUtils.sendFeature("EMOJI", mainCtrl.getUsername(), ((Node) e.getSource()).getId());
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
     * Adds a new joker to be displayed
     *
     * @param name     The name of the player sending the joker
     * @param reaction A string representing the reaction
     */
    public void displayNewJoker(String name, String reaction) {
        emoteCtrl.addJoker(name, reaction);
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
    }

    /**
     * Changes width of timerBar in response to a change in window's size
     *
     * @param newVal New width of window in px
     */
    public void resizeTimerBar(double newVal) {
        timerBarCtrl.resize(newVal);
    }

    /**
     * Sets whether a joker is enabled
     *
     * @param joker  The Button referring to the joker to be enabled / disabled
     * @param enable Whether to enable the joker
     */
    private void setJokerEnabled(Button joker, boolean enable, boolean repeat) {
        ObservableList<String> style = joker.getStyleClass();
        Platform.runLater(() -> {
            if (repeat && enable) {
                style.clear();
                style.add("clickableGreen");
                style.add("circle");
            } else if (enable) {
                style.remove("usedJoker");
                if (!style.contains("usedJoker")) {
                    style.add("clickableGreen");
                }
            } else {
                style.add("usedJoker");
                style.remove("clickableGreen");
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
        setJokerEnabled(joker, false, false);

        switch (joker.getId()) {
            case "doublePoints":
                mainCtrl.doublePoints();
                gameUtils.sendFeature("JOKER", mainCtrl.getUsername(), "DOUBLE_POINTS");
                break;
            case "eliminateWrongAnswer":
                mainCtrl.eliminateWrongAnswer();
                gameUtils.sendFeature("JOKER", mainCtrl.getUsername(), "ELIMINATE_ANSWER");
                break;
            case "halveTime":
                gameUtils.sendFeature("JOKER", mainCtrl.getUsername(), "HALVE_TIME");
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
     * @param enabled Whether the jokers are to be made enabled
     */
    public void toggleJokerUsability(boolean enabled, boolean repeat) {
        for (Button joker : jokers) {
            setJokerEnabled(joker, enabled, repeat);
        }
    }

    /**
     * Sets whether the wrong answer joker is enabled
     *
     * @param enabled Whether the wrong answer joker is enabled
     */
    public void setWrongAnswerJoker(boolean enabled) {
        setJokerEnabled(eliminateWrongAnswer, enabled, false);
    }

    /**
     * Disconnects from the game
     */
    @FXML
    void disconnect() {
        if (mainCtrl.gameOngoing) {
            mainCtrl.exitGameChecker(2);
        } else {
            mainCtrl.showMainFrame();
        }
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
                mainCtrl.dispatchKeyPress(e);
                break;
        }
    }
}