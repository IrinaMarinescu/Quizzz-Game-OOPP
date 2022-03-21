package client.scenes;

import client.scenes.controllerrequirements.LobbyCtrlRequirements;
import client.utils.GameUtils;
import client.utils.ServerUtils;
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
import javax.inject.Inject;

/**
 * The Lobby class
 */
public class LobbyCtrl implements Initializable, LobbyCtrlRequirements {

    @FXML
    private TableColumn<String, String> name;
    @FXML
    private TableView<String> table;

    private final ServerUtils serverUtils;
    private final GameUtils gameUtils;

    private final MainCtrl mainCtrl;

    ObservableList<String> list = FXCollections.observableArrayList();

    /**
     * Injects mainCtrl, so it's possible to call methods from there
     *
     * @param mainCtrl injects mainCtrl
     */
    @Inject
    public LobbyCtrl(ServerUtils serverUtils, GameUtils gameUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.gameUtils = gameUtils;
        this.mainCtrl = mainCtrl;
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
    }

    /**
     * Set up the given lobby
     *
     * @param lobby The lobby that has to be set
     */
    public void updateLobby(Lobby lobby) {
        list = FXCollections.observableArrayList(lobby.getPlayers().stream().map(player -> player.getName()).collect(
            Collectors.toList()));
        table.setItems(list);
    }
}