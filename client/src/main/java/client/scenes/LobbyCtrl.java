package client.scenes;

import client.scenes.controllerrequirements.LobbyCtrlRequirements;
import client.utils.GameUtils;
import commons.LeaderboardEntry;
import commons.Lobby;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javax.inject.Inject;

/**
 * The Lobby class
 */
public class LobbyCtrl implements Initializable, LobbyCtrlRequirements {

    @FXML
    private TableColumn<String, String> name;
    @FXML
    private TableView<String> table;

    private final GameUtils gameUtils;

    private final MainCtrl mainCtrl;

    private Lobby lobby;

    ObservableList<String> list = FXCollections.observableArrayList();

    /**
     * Injects mainCtrl, gameUtils and mainCtrl, so it's possible to call methods from there
     *
     * @param gameUtils The instance of GameUtils
     * @param mainCtrl  The instance of MainCtrl
     */
    @Inject
    public LobbyCtrl(GameUtils gameUtils, MainCtrl mainCtrl) {
        this.gameUtils = gameUtils;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Sets up the table for the lobby screen
     *
     * @param url            url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setCellValueFactory(s -> new SimpleStringProperty(s.getValue()));
        table.setItems(list);
        this.lobby = new Lobby();
    }


    /**
     * Calls startMultiplayerGame() method in mainCtrl
     */
    @FXML
    public void initializeMultiplayerGame() {
        gameUtils.startMultiplayerGame();
    }

    /**
     * Lets player leave the lobby by calling the playerLeavesLobby() method in mainCtrl
     */
    @FXML
    public void goBack() {
        mainCtrl.playerLeavesLobby();
    }

    public Lobby getLobby() {
        return lobby;
    }

    /**
     * Set up the given lobby and update displayed list of players
     *
     * @param lobby The lobby that has to be set
     */
    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
        list = FXCollections.observableArrayList(lobby.getPlayers().stream().map(LeaderboardEntry::getName).collect(
            Collectors.toList()));
        table.setItems(list);
    }

    public void keyPressed(KeyCode e) {
        switch (e) {
            case ESCAPE:
                goBack();
                break;
            case ENTER:
                initializeMultiplayerGame();
                break;
            default:
                break;
        }
    }
}