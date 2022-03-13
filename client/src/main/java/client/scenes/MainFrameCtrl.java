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

import client.scenes.controllerrequirements.MainFrameCtrlRequirements;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import java.net.URL;
import java.time.Clock;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;

public class MainFrameCtrl implements Initializable, MainFrameCtrlRequirements {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private long lastEscapeKeyPressTime;

    //@FXML
    //private Button trophy;
    @FXML
    private TextField username;
    @FXML
    private TextField serverIP;
    @FXML
    private VBox helpMenuContainer;
    @FXML
    private Text usernameError;
    @FXML
    private Text serverIPError;

    //@FXML
    //private Text helpPointsGained;
    //@FXML
    //private Button helpMenuScore;
    //@FXML
    //private Button helpMenuQuestionNumber;

    @Inject
    public MainFrameCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayUsernameError(false);
        displayServerIPError(false);
        this.lastEscapeKeyPressTime = 0;
    }

    public void openLeaderboard() {
        if (server.validateIP(serverIP.getText())) {
            server.setServerIP(serverIP.getText());

            mainCtrl.showGlobalLeaderboardFrame();
        } else {
            displayServerIPError(true);
        }
    }

    @FXML
    private void toggleHelpMenuVisibility() {
        helpMenuContainer.setVisible(!helpMenuContainer.isVisible());
    }

    public void startSingleplayerGame() {
        if (server.validateIP(serverIP.getText())) {
            server.setServerIP(serverIP.getText());
            mainCtrl.setPlayer(username.getText(), 0);
            mainCtrl.setGame(server.startSingleplayer());

            mainCtrl.showQuestionFrame();
        } else {
            displayServerIPError(true);
        }
    }

    public void joinLobby() {
        if (server.validateIP(serverIP.getText()) && server.validateUsername(username.getText())) {
            server.setServerIP(serverIP.getText());
            mainCtrl.setPlayer(username.getText(), 0);
            mainCtrl.setLobby(server.joinLobby(mainCtrl.getPlayer()));

            mainCtrl.showLobbyFrame();
        } else if (!server.validateIP(serverIP.getText())) {
            displayServerIPError(true);
        } else {
            displayUsernameError(true);
        }
    }

    @Override
    public void displayUsernameError(boolean show) {
        usernameError.setVisible(show);
    }

    @Override
    public void displayServerIPError(boolean show) {
        serverIPError.setVisible(show);
    }


    /**
     * Provides functionality for keybindings to accelerate certain actions
     *
     * @param e Information about a keypress performed by the user
     *          <p>
     *          This should only be called by the MainCtrl showMainFrame method
     */
    public void keyPressed(KeyCode e) {
        switch (e) {
            case H:
                toggleHelpMenuVisibility();
                break;
            case L:
                openLeaderboard();
                break;
            case ESCAPE:
                if (Clock.systemDefaultZone().millis() - lastEscapeKeyPressTime < 200) {
                    mainCtrl.disconnect();
                }
                lastEscapeKeyPressTime = Clock.systemDefaultZone().millis();
                break;
            default:
                break;
        }
    }
}