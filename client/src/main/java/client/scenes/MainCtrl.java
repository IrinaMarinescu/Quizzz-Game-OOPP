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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * Coordinates actions between different screens
 */
public class MainCtrl implements MainCtrlRequirements {

    private Stage primaryStage;

    private QuestionFrameCtrl questionFrameCtrl;
    private Scene questionFrame;

    private InjectedCenterExampleCtrl injectedCenterExampleCtrl;
    private Node injectedCenterNode;

    /**
     * Disconnects the player from an online game
     */
    public void disconnect() {

        // DO USEFUL STUFF HERE
        primaryStage.close();
    }

    /**
     * Initialize this controller using components provided by Main
     *
     * @param primaryStage          The (only) stage containing all scenes
     * @param questionFrame         Controller file and parent node of questionFrame node
     * @param injectedCenterExample Controller file and parent node of (demonstrational) injectedCenterExample node
     */
    public void initialize(Stage primaryStage, Pair<QuestionFrameCtrl, Parent> questionFrame,
                           Pair<InjectedCenterExampleCtrl, Parent> injectedCenterExample) {
        this.primaryStage = primaryStage;

        this.questionFrameCtrl = questionFrame.getKey();
        this.questionFrame = new Scene(questionFrame.getValue());

        this.injectedCenterExampleCtrl = injectedCenterExample.getKey();
        this.injectedCenterNode = injectedCenterExample.getValue();

        primaryStage.setTitle("Quizzzzz!");

        questionFrameCtrl.setCenterContent(injectedCenterNode);
        showQuestionFrame();
        primaryStage.show();
    }

    /**
     * Sets the questionFrame as the visible scene on the stage
     */
    public void showQuestionFrame() {
        primaryStage.setScene(questionFrame);
        questionFrame.setOnKeyPressed(e -> questionFrameCtrl.keyPressed(e));
    }
}