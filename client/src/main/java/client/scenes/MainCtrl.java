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
import client.utils.ServerUtils;
import client.utils.TimeUtils;
import commons.Game;
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
    public static final int TOTAL_ROUNDS = 4; // should be 20

    private String username;
    private boolean isMultiplayerGame;
    private Game game;
    private boolean intermediateLeaderboardShown;
    private int pointsGained;
    private long questionStartTime;
    private boolean doublePoints;

    private TimeUtils timeUtils;
    private ServerUtils serverUtils;
    private Stage primaryStage;

    private MainFrameCtrl mainFrameCtrl;
    private Scene mainFrame;

    private QuestionFrameCtrl questionFrameCtrl;
    private Scene questionFrame;

    private LeaderboardCtrl leaderboardCtrl;
    private Scene leaderboard;

    private OpenQuestion openQuestionCtrl;
    private Node openQuestion;

    QuestionRequirements currentQuestionCtrl = null;

    private boolean widthChanged = false;

    /**
     * Initializes this class
     *
     * @param timeUtils        Only instance of TimeUtils class
     * @param serverUtils      Only instance of ServerUtils class
     * @param primaryStage     Only stage
     * @param mainFrame        Welcome screen FXML and controller
     * @param questionFrame    Question Frame screen FXML and controller
     * @param leaderboard      Leaderboard screen FXML and controller
     * @param openQuestion     Open question node FXML and controller
     * @param questionOneImage Question with one image FXML and controller
     */
    public void initialize(TimeUtils timeUtils, ServerUtils serverUtils, Stage primaryStage,
                           Pair<MainFrameCtrl, Parent> mainFrame,
                           Pair<QuestionFrameCtrl, Parent> questionFrame,
                           Pair<LeaderboardCtrl, Parent> leaderboard,
                           Pair<OpenQuestion, Parent> openQuestion,
                           Pair<QuestionOneImage, Parent> questionOneImage) {

        this.timeUtils = timeUtils;
        this.serverUtils = serverUtils;
        this.primaryStage = primaryStage;

        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (widthChanged) {
                questionFrameCtrl.resizeTimerBar(newVal.intValue(), oldVal.intValue() - newVal.intValue());
            }
            widthChanged = true;
        });

        this.openQuestionCtrl = openQuestion.getKey();
        this.openQuestion = openQuestion.getValue();

        this.mainFrameCtrl = mainFrame.getKey();
        this.mainFrame = new Scene(mainFrame.getValue());
        this.mainFrame.setOnKeyPressed(e -> mainFrameCtrl.keyPressed(e));

        this.questionFrameCtrl = questionFrame.getKey();
        this.questionFrame = new Scene(questionFrame.getValue());
        this.questionFrame.setOnKeyPressed(e -> questionFrameCtrl.keyPressed(e.getCode()));

        this.leaderboardCtrl = leaderboard.getKey();
        this.leaderboard = new Scene(leaderboard.getValue());

        primaryStage.setTitle("Quizzzzz!");
        showMainFrame();
        primaryStage.show();
    }

    @Override
    public void startSingleplayerGame() {
        intermediateLeaderboardShown = false;
        isMultiplayerGame = false;
        game = serverUtils.getGame();
        questionFrameCtrl.initializeSingleplayerGame();
        showQuestionFrame();
        nextEvent();
    }

    @Override
    public void startMultiplayerGame() {

    }

    private void nextEvent() {
        // The current event is the intermediate leaderboard
        if (game.getRound() == TOTAL_ROUNDS / 2 && !intermediateLeaderboardShown) {
            intermediateLeaderboardShown = true;
            leaderboardCtrl.initialize(game.getPlayers(), 10, "intermediate");
            primaryStage.setScene(leaderboard);

            timeUtils.runAfterDelay(() -> {
                showQuestionFrame();
                nextEvent();
            }, LEADERBOARD_TIME);
            return;
        }

        // The current event is the final leaderboard; the game is over
        if (game.getRound() == TOTAL_ROUNDS) {
            leaderboardCtrl.initialize(game.getPlayers(), 10, "final");
            primaryStage.setScene(leaderboard);
            return;
        }

        // The current event is a question
        game.incrementRound();
        questionFrameCtrl.incrementQuestionNumber();
        Platform.runLater(() -> questionFrameCtrl.setRemainingTime(ROUND_TIME));
        questionStartTime = timeUtils.now();
        pointsGained = 0;
        doublePoints = false;
        Question currentQuestion = game.getNextQuestion();

        // TODO: change questions types so that they match
        // TODO: implement remaining cases
        switch (currentQuestion.getQuestionType()) {
            case "OPEN_QUESTION":
                currentQuestionCtrl = openQuestionCtrl;
                questionFrameCtrl.setCenterContent(openQuestion);
                break;
            case "TRUE_FALSE":
                questionFrameCtrl.setWrongAnswerJoker(false);
                break;
            default:
                System.err.println("Unrecognized question type in MainCtrl");
                break;
        }
        currentQuestionCtrl.initialize(currentQuestion);

        timeUtils.runAfterDelay(() -> {
            currentQuestionCtrl.revealCorrectAnswer();
            questionFrameCtrl.addPoints(pointsGained);
            serverUtils.sendPointsGained(game.getId(), username, pointsGained);
            if (currentQuestion.getQuestionType().equals("TRUE_FALSE")) {
                questionFrameCtrl.setWrongAnswerJoker(true);
            }

            Platform.runLater(() -> questionFrameCtrl.setRemainingTime(OVERVIEW_TIME));
            questionFrameCtrl.tempDisableJokers(OVERVIEW_TIME);
            timeUtils.runAfterDelay(this::nextEvent, OVERVIEW_TIME);
        }, ROUND_TIME);
    }

    /**
     * To be run when points are gained by the player
     *
     * @param baseScore the score (0 - 100)
     */
    @Override
    public void addPoints(int baseScore) {
        if (baseScore != 0) {
            double progress = ((double) (timeUtils.now() - questionStartTime)) / (ROUND_TIME * 1000.0);
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

    @Override
    public void eliminateWrongAnswer() {
        // currentQuestionCtrl.eliminateWrongAnswer();
    }

    @Override
    public void redirectToSoloLeaderboard() {

    }

    @Override
    public void redirectToMainScreen() {

    }

    @Override
    public void playerLeavesLobby() {

    }

    /**
     * Disconnects the player from an online game
     */
    public void disconnect() {

        // DO USEFUL STUFF HERE
        primaryStage.close();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void showGlobalLeaderboardFrame() {
        leaderboardCtrl.initialize(List.of(), 10, "solo");
        primaryStage.setScene(leaderboard);
    }

    public void showLobbyFrame() {
    }

    public void showMainFrame() {
        primaryStage.setScene(mainFrame);
    }

    public void showQuestionFrame() {
        primaryStage.setScene(questionFrame);
    }
}