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
package client;

import static com.google.inject.Guice.createInjector;

import client.scenes.InjectedCenterExampleCtrl;
import com.google.inject.Injector;

import client.scenes.MainCtrl;
import client.scenes.QuestionFrameCtrl;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The Main class
 */
public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    /**
     * Hands control over to JavaFX
     * @param args - Arguments for starting the program
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Loads all scenes/nodes, initializes main controller and configures primary (and only) stage
     * @param primaryStage - The stage containing all scenes
     */
    @Override
    public void start(Stage primaryStage) {

        var questionFrame = FXML.load(QuestionFrameCtrl.class, "client/scenes/questionFrame.fxml", "client/css/questionFrame.css");
        var injectedCenterExample = FXML.load(InjectedCenterExampleCtrl.class, "client/scenes/injectedCenterExample.fxml", null);

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, questionFrame, injectedCenterExample);
    }
}