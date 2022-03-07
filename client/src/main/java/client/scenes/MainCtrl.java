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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Coordinates actions between different screens
 */
public class MainCtrl implements MainCtrlRequirements {

    private String username;

    private Stage primaryStage;

    private MainFrameCtrl mainFrameCtrl;
    private Scene mainFrame;

    private QuestionFrameCtrl questionFrameCtrl;
    private Scene questionFrame;

    private boolean widthChanged = false;

    /**
     * Initialize this controller using components provided by Main
     *
     * @param primaryStage  The (only) stage containing all scenes
     * @param questionFrame Controller file and parent node of questionFrame node
     */
    public void initialize(Stage primaryStage, Pair<QuestionFrameCtrl, Parent> questionFrame) {
        this.primaryStage = primaryStage;

        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (widthChanged) {
                questionFrameCtrl.resizeTimerBar(newVal.intValue(), oldVal.intValue() - newVal.intValue());
            }
            widthChanged = true;
        });

        this.questionFrameCtrl = questionFrame.getKey();
        this.questionFrame = new Scene(questionFrame.getValue());
        this.questionFrame.setOnKeyPressed(e -> questionFrameCtrl.keyPressed(e.getCode()));

        primaryStage.setTitle("Quizzzzz!");

        showQuestionFrame();
        primaryStage.show();
    }

    /**
     * Disconnects the player from an online game
     */
    public void disconnect() {

        // DO USEFUL STUFF HERE
        primaryStage.close();
    }

    @Override
    public void startSingleplayerGame() {

    }

    @Override
    public void startMultiplayerGame() {

    }

    @Override
    public void redirectToLobby(String name) {

    }

    @Override
    public void redirectToSoloLeaderboard() {

    }

    @Override
    public void addPoints(int baseScore) {

    }

    @Override
    public void redirectToMainScreen() {

    }

    @Override
    public void playerLeavesLobby(String name) {

    }

    @Override
    public void halveRemainingTime() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void showMainFrame() {
        primaryStage.setScene(mainFrame);
        mainFrame.setOnKeyPressed(e -> mainFrameCtrl.keyPressed(e));
    }

    /**
     * Sets the questionFrame as the visible scene on the stage
     */
    public void showQuestionFrame() {
        primaryStage.setScene(questionFrame);
    }

    public void showLobbyFrame() {
    }

    public void showGlobalLeaderboardFrame() {
    }
}