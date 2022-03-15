package client.scenes;

import client.scenes.controllerrequirements.LobbyCtrlRequirements;
import java.net.URL;
import java.util.ResourceBundle;
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

    private final MainCtrl mainCtrl;

    /**
     * Injects mainCtrl, so it's possible to call methods from there
     *
     * @param mainCtrl injects mainCtrl
     */
    @Inject
    public LobbyCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Calls startMultiplayerGame() method in mainCtrl
     */
    @FXML
    public void initializeMultiplayerGame() {
        mainCtrl.startMultiplayerGame();
    }

    /**
     * Lets player leave the lobby by calling the playerLeavesLobby() method in mainCtrl
     */
    @FXML
    public void goBack() {
        mainCtrl.playerLeavesLobby();
    }

    ObservableList<String> list = FXCollections.observableArrayList(
            "Yannick",
            "Per",
            "Irina",
            "Andrei",
            "Mirella",
            "Chris"
    );


    /**
     * Sets up the table for the lobby screen
     *
     * @param url url
     *
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setCellValueFactory(s -> new SimpleStringProperty(s.getValue()));

        table.setItems(list);
    }

    /**
     * Adds a player to the lobby, reloads the table
     *
     * @param name The name of the player to be added
     */
    @Override
    public void addPlayer(String name) {
        list.add(name);
        table.setItems(list);
    }

    /**
     * Removes a player from the lobby, reloads the table
     *
     * @param name The name of the player
     */
    @Override
    public void removePlayer(String name) {
        list.remove(name);
        table.setItems(list);
    }

    /**
     * Removes all players from the lobby, reloads the table
     */
    @Override
    public void clearPlayers() {
        list.clear();
        table.setItems(list);
    }
}