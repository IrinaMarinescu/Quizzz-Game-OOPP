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
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;

public class MainFrameCtrl implements Initializable {

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
        //colFirstName.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().person.firstName));
        //colLastName.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().person.lastName));
        //colQuote.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().quote));

        this.lastEscapeKeyPressTime = 0;
    }

    public void toggleLeaderboardVisibility() {
    }

    @FXML
    private void toggleHelpMenuVisibility() {
        helpMenuContainer.setVisible(!helpMenuContainer.isVisible());
    }

    public void singleplayer() {
        username.getText();
        serverIP.getText();
    }

    public void mulitplayer() {
        username.getText();
        serverIP.getText();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case H:
                toggleHelpMenuVisibility();
                break;
            case L:
                toggleLeaderboardVisibility();
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