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
import javafx.scene.input.KeyEvent;
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
        if (validateServerIP(serverIP.getText())) {
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
        if (validateServerIP(serverIP.getText())) {
            mainCtrl.setUsername(username.getText());
            server.setServerIP(serverIP.getText());
            mainCtrl.startSingleplayerGame();
        } else {
            displayServerIPError(true);
        }
    }

    public void startMultiplayerGame() {
        if (validateServerIP(serverIP.getText()) && validateUsername(username.getText())) {
            mainCtrl.setUsername(username.getText());
            server.setServerIP(serverIP.getText());
            mainCtrl.startMultiplayerGame();
        } else if (!validateServerIP(serverIP.getText())) {
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
     * Check if another user in lobby already uses this name
     *
     * @param username username provided by player in the TextField
     * @return true if the username is not used yet, false otherwise
     */
    private boolean validateUsername(String username) {
        // TODO get usernames from server and check if the one provided by the client is in them
        return true;
    }

    /**
     * Check if serverIP is correct
     *
     * @param serverIP server IP provided by player in the TextField
     * @return true if the serverIP is correct, false otherwise
     */
    private boolean validateServerIP(String serverIP) {
        return server.validateIP(serverIP);
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
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