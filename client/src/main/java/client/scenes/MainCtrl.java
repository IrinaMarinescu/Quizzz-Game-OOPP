/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package client.scenes;

import client.scenes.controllerrequirements.MainCtrlRequirements;
import client.scenes.controllerrequirements.QuestionRequirements;
import client.scenes.questioncontrollers.InsteadOfQuestionCtrl;
import client.scenes.questioncontrollers.OpenQuestionCtrl;
import client.scenes.questioncontrollers.QuestionOneImageCtrl;
import client.scenes.questioncontrollers.QuestionThreePicturesCtrl;
import client.scenes.questioncontrollers.QuestionTrueFalseCtrl;
import client.utils.LongPollingUtils;
import client.utils.ServerUtils;
import client.utils.TimeUtils;
import commons.Game;
import commons.LeaderboardEntry;
import commons.Lobby;
import commons.Question;
import java.util.List;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Coordinates actions between different screens
 */
public class MainCtrl implements MainCtrlRequirements {

    public static final int ROUND_TIME = 10;
    public static final int OVERVIEW_TIME = 5;
    public static final int LEADERBOARD_TIME = 10;
    public static final int TOTAL_ROUNDS = 20;

    private LeaderboardEntry player;
    private Game game;
    private boolean isMultiplayerGame;
    private boolean intermediateLeaderboardShown;
    private int pointsGained;
    private long questionStartTime;
    private boolean doublePoints;
    private double questionEndTime;
    private int timeoutRoundCheck;
    private String currentQuestionType;

    private TimeUtils timeUtils;
    private ServerUtils serverUtils;
    private LongPollingUtils longPollingUtils;
    private Lobby lobby;
    private Stage primaryStage;

    private MainFrameCtrl mainFrameCtrl;
    private Scene mainFrame;

    private QuestionFrameCtrl questionFrameCtrl;
    private Scene questionFrame;

    private LeaderboardCtrl leaderboardCtrl;
    private Scene leaderboard;

    private FinalScreenCtrl finalScreenCtrl;
    private Node finalScreen;

    private OpenQuestionCtrl openQuestionCtrl;
    private Node openQuestion;

    private QuestionOneImageCtrl questionOneImageCtrl;
    private Node questionOneImage;

    private InsteadOfQuestionCtrl insteadOfQuestionCtrl;
    private Node insteadOfQuestion;

    private QuestionTrueFalseCtrl questionTrueFalseCtrl;
    private Node questionTrueFalse;

    private QuestionThreePicturesCtrl questionThreePicturesCtrl;
    private Node questionThreePictures;

    QuestionRequirements currentQuestionCtrl = null;

    /**
     * Initializes this class
     *
     * @param timeUtils        Only instance of TimeUtils class
     * @param serverUtils      Only instance of ServerUtils class
     * @param longPollingUtils Only instance of LongPollingUtils class
     * @param primaryStage     Only stage
     * @param mainFrame        Welcome screen FXML and controller
     * @param questionFrame    Question Frame screen FXML and controller
     * @param leaderboard      Leaderboard screen FXML and controller
     * @param finalScreen Final screen FXML and controller
     * @param openQuestion     Open question node FXML and controller
     * @param questionOneImage Question with one image FXML and controller
     * @param insteadOfQuestion Instead of question node FXML and controller
     * @param questionTrueFalse True False question node FXML and controller
     * @param questionThreePictures Question with three pictures node FXML and controller
     */
    public void initialize(TimeUtils timeUtils, ServerUtils serverUtils, LongPollingUtils longPollingUtils,
                           Stage primaryStage,
                           Pair<FinalScreenCtrl, Parent> finalScreen,
                           Pair<MainFrameCtrl, Parent> mainFrame,
                           Pair<QuestionFrameCtrl, Parent> questionFrame,
                           Pair<LeaderboardCtrl, Parent> leaderboard,
                           Pair<OpenQuestionCtrl, Parent> openQuestion,
                           Pair<QuestionOneImageCtrl, Parent> questionOneImage,
                           Pair<InsteadOfQuestionCtrl, Parent> insteadOfQuestion,
                           Pair<QuestionTrueFalseCtrl, Parent> questionTrueFalse,
                           Pair<QuestionThreePicturesCtrl, Parent> questionThreePictures) {

        this.timeUtils = timeUtils;
        this.serverUtils = serverUtils;
        this.longPollingUtils = longPollingUtils; // note that long polling is not active by default!
        this.primaryStage = primaryStage;

        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            questionFrameCtrl.resizeTimerBar(newVal.intValue(), oldVal.intValue() - newVal.intValue());
        });
        primaryStage.setOnCloseRequest(e -> disconnect());

        this.finalScreenCtrl = finalScreen.getKey();
        this.finalScreen = finalScreen.getValue();

        this.openQuestionCtrl = openQuestion.getKey();
        this.openQuestion = openQuestion.getValue();

        this.questionOneImageCtrl = questionOneImage.getKey();
        this.questionOneImage = questionOneImage.getValue();

        this.insteadOfQuestionCtrl = insteadOfQuestion.getKey();
        this.insteadOfQuestion = insteadOfQuestion.getValue();

        this.questionTrueFalseCtrl = questionTrueFalse.getKey();
        this.questionTrueFalse = questionTrueFalse.getValue();

        this.questionThreePicturesCtrl = questionThreePictures.getKey();
        this.questionThreePictures = questionThreePictures.getValue();

        this.mainFrameCtrl = mainFrame.getKey();
        this.mainFrame = new Scene(mainFrame.getValue());
        this.mainFrame.setOnKeyPressed(e -> mainFrameCtrl.keyPressed(e.getCode()));

        this.questionFrameCtrl = questionFrame.getKey();
        this.questionFrame = new Scene(questionFrame.getValue());

        this.leaderboardCtrl = leaderboard.getKey();
        this.leaderboard = new Scene(leaderboard.getValue());

        primaryStage.setTitle("Quizzzzz!");
        showMainFrame();

        primaryStage.show();
    }

    public LeaderboardEntry getPlayer() {
        return this.player;
    }

    public void setPlayer(String username, int points) {
        this.player = new LeaderboardEntry(username, points);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public void playerLeavesLobby() {
    }

    /**
     * Starts a singleplayer game
     * <p>
     * To be run when a person chooses solo play in the welcome screen (main frame)
     */
    @Override
    public void startSingleplayerGame() {
        intermediateLeaderboardShown = false;
        isMultiplayerGame = false;
        timeoutRoundCheck = 1;
        game = serverUtils.startSingleplayer();
        questionFrameCtrl.initializeSingleplayerGame();
        showQuestionFrame();
        nextEvent();
    }

    @Override
    public void startMultiplayerGame() {
        // TODO: enable long polling
    }

    /**
     * Executes the next event (question, leaderboard, game over)
     */
    private void nextEvent() {
        game.setPlayers(serverUtils.getUpdatedScores(game.getId()));

        if (isMultiplayerGame) {
            // The current event is the intermediate leaderboard
            if (game.getRound() == TOTAL_ROUNDS / 2 && !intermediateLeaderboardShown) {
                intermediateLeaderboardShown = true;
                showLeaderboard(game.getPlayers(), 10, "intermediate");

                timeUtils.runAfterDelay(() -> {
                    showQuestionFrame();
                    nextEvent();
                }, LEADERBOARD_TIME);
                return;
            }

            // The current event is the final leaderboard; the game is over
            if (game.getRound() == TOTAL_ROUNDS) {
                showLeaderboard(game.getPlayers(), 10, "final");
                return;
            }
            questionFrameCtrl.setLeaderboardContents(game.getPlayers());
        } else if (game.getRound() == TOTAL_ROUNDS) {
            showFinalScreen();
        }

        // The current event is a question
        game.incrementRound();
        questionFrameCtrl.incrementQuestionNumber();
        Platform.runLater(() -> questionFrameCtrl.setRemainingTime(ROUND_TIME));
        questionStartTime = timeUtils.now();
        questionEndTime = questionStartTime + ROUND_TIME * 1000.0;
        pointsGained = 0;
        doublePoints = false;
        Question currentQuestion = game.getNextQuestion();
        currentQuestionType = currentQuestion.getQuestionType();

        switch (currentQuestionType) {
            case "trueFalseQuestion":
                currentQuestionCtrl = questionTrueFalseCtrl;
                questionFrameCtrl.setCenterContent(questionTrueFalse);
                questionFrameCtrl.setWrongAnswerJoker(false);
                break;
            case "openQuestion":
                currentQuestionCtrl = openQuestionCtrl;
                questionFrameCtrl.setCenterContent(openQuestion);
                questionFrameCtrl.setWrongAnswerJoker(false);
                break;
            case "threePicturesQuestion":
                currentQuestionCtrl = questionThreePicturesCtrl;
                questionFrameCtrl.setCenterContent(questionThreePictures);
                break;
            case "oneImageQuestion":
                currentQuestionCtrl = questionOneImageCtrl;
                questionFrameCtrl.setCenterContent(questionOneImage);
                break;
            case "insteadOfQuestion":
                currentQuestionCtrl = insteadOfQuestionCtrl;
                questionFrameCtrl.setCenterContent(insteadOfQuestion);
                break;
            default:
                System.err.println("Unrecognized question type in MainCtrl");
                break;
        }
        currentQuestionCtrl.initialize(currentQuestion);

        setQuestionTimeouts(ROUND_TIME);
    }

    private void showFinalScreen() {
        finalScreenCtrl.setPoints(getPlayer().getScore());
        questionFrameCtrl.setCenterContent(finalScreen);
    }

    /**
     * Initializes timeouts until events that will happen after question and overview
     *
     * @param delay The time until the end of the question
     */
    private void setQuestionTimeouts(double delay) {
        timeUtils.runAfterDelay(() -> {
            if (game.getRound() != timeoutRoundCheck) {
                return;
            }

            timeoutRoundCheck++;
            currentQuestionCtrl.revealCorrectAnswer();
            questionFrameCtrl.addPoints(pointsGained);
            questionFrameCtrl.tempDisableJokers(OVERVIEW_TIME);
            serverUtils.sendPointsGained(game.getId(), player, pointsGained);
            if (currentQuestionType.equals("trueFalseQuestion")) {
                questionFrameCtrl.setWrongAnswerJoker(true);
            }

            Platform.runLater(() -> questionFrameCtrl.setRemainingTime(OVERVIEW_TIME));
            timeUtils.runAfterDelay(this::nextEvent, OVERVIEW_TIME);
            //Platform.runLater(() -> nextEvent());
        }, delay);
    }

    /**
     * To be run when points are gained by the player
     *
     * @param baseScore the score in range [0; 100]
     */
    @Override
    public void addPoints(long baseScore) {
        if (baseScore != 0) {
            double progress = ((double) (timeUtils.now() - questionStartTime)) / (questionEndTime - questionStartTime);
            pointsGained = (int) (50.0 + 0.5 * (1.0 - progress) * (double) baseScore);
            if (doublePoints) {
                pointsGained *= 2;
            }
        }
    }

    /**
     * Doubles points gained on this question
     */
    @Override
    public void doublePoints() {
        pointsGained *= 2;
        doublePoints = true;
    }

    /**
     * Halves remaining time
     */
    @Override
    public void halveTime() {
        double timeUntilRoundEnd = (questionEndTime - timeUtils.now()) / 2.0;
        questionEndTime -= timeUntilRoundEnd;
        questionFrameCtrl.halveRemainingTime();
        setQuestionTimeouts(timeUntilRoundEnd / 1000.0);
    }

    /**
     * Eliminates incorrect answer
     */
    @Override
    public void eliminateWrongAnswer() {
        currentQuestionCtrl.removeIncorrectAnswer();
    }

    /**
     * Disconnects the player from a game
     */
    public void disconnect() {
        // TODO stop long polling
        serverUtils.disconnect(game.getId(), player);
        showMainFrame();
    }

    @Override
    public void showGlobalLeaderboardFrame() {
        int maxSize = 10;
        showLeaderboard(serverUtils.getSoloLeaderboard(maxSize), maxSize, "solo");
    }

    public void showLobbyFrame() {
    }

    /**
     * Shows leaderboard
     *
     * @param players The players in the leaderboard
     * @param maxSize The maximum number of players to be displayed
     * @param type    The type of the leaderboard
     */
    private void showLeaderboard(List<LeaderboardEntry> players, int maxSize, String type) {
        leaderboardCtrl.initialize(players, maxSize, type);
        primaryStage.setScene(leaderboard);
    }

    /**
     * Shows main frame (welcome/splash screen)
     */
    public void showMainFrame() {
        primaryStage.setScene(mainFrame);
    }

    /**
     * Shows question frame
     */
    public void showQuestionFrame() {
        primaryStage.setScene(questionFrame);
        questionFrame.setOnKeyPressed(e -> questionFrameCtrl.keyPressed(e.getCode()));
    }
}