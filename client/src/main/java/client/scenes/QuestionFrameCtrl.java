package client.scenes;

import client.scenes.frameComponents.EmoteCtrl;
import client.scenes.frameComponents.TimerBarCtrl;
import commons.LeaderboardEntry;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Controller for questionFrame scene
 */
public class QuestionFrameCtrl implements Initializable {

    private static final int LEADERBOARD_SIZE_MAX = 5;

    private final MainCtrl mainCtrl;
    private final TimerBarCtrl timerBarCtrl;
    private final EmoteCtrl emoteCtrl;

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
    @FXML
    private VBox reactionContainer;
    @FXML
    private HBox emoticonSelectionField;
    @FXML
    private Button scoreField;
    @FXML
    private Text newPoints;
    @FXML
    private Button help;
    @FXML
    private Button halveTime;
    @FXML
    private Button eliminateWrongAnswer;
    @FXML
    private Button doublePoints;
    @FXML
    private Button turnIndicator;

    @FXML
    private TableView<LeaderboardEntry> leaderboard;
    @FXML
    private TableColumn<LeaderboardEntry, String> playerColumn;
    @FXML
    private TableColumn<LeaderboardEntry, String> scoreColumn;

    private List<Button> jokers;

    private boolean isMultiplayerGame;
    private int gameScore;
    private int questionNumber;

    /**
     * Injects necessary dependencies
     * @param mainCtrl - The main front-end controller
     */
    @Inject
    public QuestionFrameCtrl(MainCtrl mainCtrl, TimerBarCtrl timerBarCtrl, EmoteCtrl emoteCtrl) {
        this.mainCtrl = mainCtrl;
        this.timerBarCtrl = timerBarCtrl;
        this.emoteCtrl = emoteCtrl;
    }

    /**
     * Initializes this controller
     * @param location - Location of this controller
     * @param resources - (Potentially) useful resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        jokers = List.of(halveTime, eliminateWrongAnswer, doublePoints);

        addPoints(32);

        timerBarCtrl.initialize(timerBar);
        emoteCtrl.initialize(reactionContainer);

        playerColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getName()));
        scoreColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getScoreString()));

        setRemainingTime(10);

        startMultiplayerGame(List.of("Per", "Andrei"));
    }

    /**
     * Initializes settings for a new single-player game
     */
    public void startSingleplayerGame() {
        startNewGame(false, null);
    }

    /**
     * Initializes settings for a new multiplayer game
     * @param names - The names of players involved
     */
    public void startMultiplayerGame(List<String> names) {
        startNewGame(true, names);
    }

    /**
     * Initializes settings for a new game
     * @param isMultiplayerGame - Whether this is a multiplayer game
     * @param playerNames - The names of the players involved
     */
    private void startNewGame(boolean isMultiplayerGame, List<String> playerNames) {
        this.isMultiplayerGame = isMultiplayerGame;
        this.gameScore = 0;
        this.questionNumber = 0;
        incrementQuestionNumber();

        trophy.setManaged(isMultiplayerGame);
        trophy.setVisible(isMultiplayerGame);
        emote.setVisible(isMultiplayerGame);

        for(Button joker : jokers) {
            if(joker.getStyleClass().contains("usedJoker")) {
                joker.getStyleClass().add("clickable");
                joker.getStyleClass().remove("usedJoker");
            }
        }

        if(isMultiplayerGame) {
            List<LeaderboardEntry> players = playerNames
                    .stream()
                    .map(p -> new LeaderboardEntry(p, 0))
                    .collect(Collectors.toList());
            setLeaderboardContents(players);
        }
        else {
            sideLeaderboard.setVisible(false);
            setEmoticonField(false);
            halveTime.getStyleClass().add("usedJoker");
            halveTime.getStyleClass().remove("clickable");
        }
    }

    /**
     * Add points to a player's score, as seen in top left
     * @param points - The number of points to be added
     */
    public void addPoints(int points) {
        newPoints.setText("+" + points);
        newPoints.setVisible(true);

        gameScore += points;
        scoreField.setText(((Integer) gameScore).toString());

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                newPoints.setVisible(false);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.err.println("InterruptError at addPoints delay");
            }
        }).start();
    }

    /**
     * Increments the question number displayed in the top right
     */
    public void incrementQuestionNumber() {
        questionNumber++;
        turnIndicator.setText(questionNumber + "/20");
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
    private void toggleLeaderboardVisibility() {
        if(isMultiplayerGame) sideLeaderboard.setVisible(!sideLeaderboard.isVisible());
    }

    /**
     * Sets contents of the pop-up leaderboard
     * @param entries - A list of LeaderboardEntry objects representing leaderboard fields
     */
    public void setLeaderboardContents(List<LeaderboardEntry> entries) {
        entries = entries.stream().sorted().collect(Collectors.toList());
        while(entries.size() > LEADERBOARD_SIZE_MAX) entries.remove(entries.size() - 1);

        ObservableList<LeaderboardEntry> data = FXCollections.observableList(entries);
        leaderboard.setItems(data);
    }

    /**
     * Toggles emoticon field visibility
     */
    @FXML
    private void toggleEmoticonField() {
        setEmoticonField(!emoticonSelectionField.isVisible());
    }

    /**
     * Turns off emoticon field
     */
    @FXML
    private void toggleEmoticonFieldExit() {
        if(emoticonSelectionField.isVisible()) setEmoticonField(false);
    }

    /**
     * Turns emoticon field on or off
     * @param visible - Whether the emoticon field must become visible
     */
    private void setEmoticonField(boolean visible) {
        if(!isMultiplayerGame) return;

        if(!visible) {
            emoticonSelectionField.setVisible(false);
            emote.getStyleClass().remove("jagged");
        }
        else {
            emoticonSelectionField.setVisible(true);
            emote.getStyleClass().add("jagged");
        }
    }

    /**
     * Methods to be run when a user chooses to send an emoticon
     *
     * Normally, these would send a request to the server
     */
    @FXML
    private void addHappyReaction() {
        emoteCtrl.addReaction("Chris", "happy");
    }
    @FXML
    private void addSadReaction() {
        emoteCtrl.addReaction("Per", "sad");
    }
    @FXML
    private void addAngryReaction() {
        emoteCtrl.addReaction("Mirella", "angry");
    }
    @FXML
    private void addSurprisedReaction() {
        emoteCtrl.addReaction("Andrei", "surprised");
    }

    /**
     * Applies settings that disable the use of a joker
     * @param joker - The joker to be disabled
     * @return Whether the joker was already disabled
     */
    private boolean disableJoker(Button joker) {
        if(joker.getStyleClass().contains("usedJoker")) return true;

        joker.getStyleClass().add("usedJoker");
        joker.getStyleClass().remove("clickable");
        return false;
    }

    /**
     * Sends a request to the server that a player used the halveTime joker
     */
    @FXML
    private void halveTime() {
        if(disableJoker(halveTime)) return;

        // ADD USEFUL STUFF HERE
        timerBarCtrl.halveRemainingTime();
    }

    /**
     * Sends a request to the server that a player used the doublePoints joker
     */
    @FXML
    private void doublePoints() {
        if(disableJoker(doublePoints)) return;

        // ADD USEFUL STUFF HERE
        timerBarCtrl.setRemainingTime(20);
    }

    /**
     * Sends a request to the server that a player used the eliminateWrongAnswer joker
     */
    @FXML
    private void eliminateWrongAnswer() {
        if(disableJoker(eliminateWrongAnswer)) return;

        // ADD USEFUL STUFF HERE
        incrementQuestionNumber();
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
            case T:
                halveTime();
                break;
            case E:
                eliminateWrongAnswer();
                break;
            case D:
                doublePoints();
                break;
            case L:
                toggleLeaderboardVisibility();
                break;
            default:
                break;
        }
    }
}
