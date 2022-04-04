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
import client.utils.LobbyUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MainFrameCtrl implements Initializable, MainFrameCtrlRequirements {

    private final ServerUtils serverUtils;
    private final LobbyUtils lobbyUtils;

    private final MainCtrl mainCtrl;

    private long lastEscapeKeyPressTime;

    private final File userInfo = new File("user-info/user-info.txt");

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

    /**
     * Injects mainCtrl, lobbyUtils and mainCtrl, so it's possible to call methods from there
     *
     * @param serverUtils The instance of ServerUtils
     * @param lobbyUtils  The instance of LobbyUtils
     * @param mainCtrl    The instance of MainCtrl
     */
    @Inject
    public MainFrameCtrl(ServerUtils serverUtils, LobbyUtils lobbyUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.lobbyUtils = lobbyUtils;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayUsernameError(false, "");
        displayServerIPError(false);
        try {
            Scanner scanner = new Scanner(userInfo);
            scanner.useDelimiter(",");
            while (scanner.hasNext()) {
                serverIP.setText(scanner.next());
                username.setText(scanner.next());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.lastEscapeKeyPressTime = 0;
    }

    /**
     * If the server IP entered by the user is correct redirect to the frame with the global leaderboard
     * otherwise show a server IP error
     */
    @FXML
    public void openLeaderboard() {
        if (serverUtils.validateIP(serverIP.getText())) {
            serverUtils.setServerIP(serverIP.getText());
            mainCtrl.showGlobalLeaderboardFrame();
        } else {
            displayServerIPError(true);
        }
    }

    /**
     * Open or closes the help menu on click on the question mark icon
     */
    @FXML
    private void toggleHelpMenuVisibility() {
        helpMenuContainer.setVisible(!helpMenuContainer.isVisible());
    }

    /**
     * If the server IP entered by the user is correct start a new single player Game, otherwise show a server IP error
     */
    public void startSingleplayerGame() {
        if (serverUtils.validateIP(serverIP.getText())) {
            displayServerIPError(false);
            if (validateUserName()) {
                displayUsernameError(false, "");
                serverUtils.setServerIP(serverIP.getText());
                mainCtrl.setPlayer(username.getText(), 0);
                writeToFile();
                mainCtrl.startGame(false);
            }
        } else {
            displayServerIPError(true);
        }
    }

    /**
     * If the server IP entered by the user is correct and the username chosen by the user is not taken in the Lobby
     * join the Lobby, otherwise show a server IP error
     */
    public void joinLobby() {
        if (serverUtils.validateIP(serverIP.getText()) && lobbyUtils.validateUsername(username.getText())) {
            displayServerIPError(false);
            serverUtils.setServerIP(serverIP.getText());
            if (validateUserName()) {
                displayUsernameError(false, "");
                mainCtrl.setPlayer(username.getText(), 0);
                writeToFile();
                mainCtrl.joinLobby();
            }
        } else if (!lobbyUtils.validateUsername(serverIP.getText())) {
            displayServerIPError(true);
        } else {
            displayUsernameError(true, "The username is already taken. Try again.");
        }
    }

    private void writeToFile() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(userInfo);
            if (serverIP.getText().equals("")) {
                writer.print("http://localhost:8080/");
            } else {
                writer.print(serverIP.getText());
            }
            writer.print("," + username.getText());
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * If the user has entered a correct server IP, they will be redirected to the admin interface.
     * Otherwise, they will be shown an error.
     */
    public void showAdmin() {
        if (serverUtils.validateIP(serverIP.getText())) {
            serverUtils.setServerIP(serverIP.getText());
            mainCtrl.showAdminInterface();
        } else {
            System.out.println("invalid ip!");
        }
    }

    /**
     * Display the username error
     *
     * @param show If true displays the error otherwise hides it
     */
    @Override
    public void displayUsernameError(boolean show, String errorMessage) {
        usernameError.setText(errorMessage);
        usernameError.setVisible(show);
    }

    /**
     * Display the server IP error
     *
     * @param show If true displays the error otherwise hides it
     */
    @Override
    public void displayServerIPError(boolean show) {
        serverIPError.setVisible(show);
    }

    /**
     * Checks whether the username is valid, so that it does not contain a comma and is not empty
     *
     * @return true if the username is valid, false if it is not
     */
    public boolean validateUserName() {
        if (username.getText().equals("")) {
            displayUsernameError(true, "Your name cannot be empty. Try again.");
            return false;
        } else if (username.getText().contains(",")) {
            displayUsernameError(true, "Your name cannot contain a comma. Try again.");
            return false;
        }
        return true;
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
            case L:
                openLeaderboard();
                break;
            case ESCAPE:
                mainCtrl.toggleModalVisibility();
                break;
            case CONTROL:
                showAdmin();
                break;
            case S:
                startSingleplayerGame();
                break;
            case M:
                joinLobby();
                break;
            default:
                break;
        }
    }
}