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

public class LobbyCtrl implements Initializable, LobbyCtrlRequirements {

    @FXML
    private TableColumn<String, String> name;
    @FXML
    private TableView<String> table;

    private final MainCtrl mainCtrl;

    @Inject
    public LobbyCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    @FXML
    public void initializeMultiplayerGame() {
        mainCtrl.startMultiplayerGame();
    }

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



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setCellValueFactory(s -> new SimpleStringProperty(s.getValue()));

        table.setItems(list);
    }

    @Override
    public void addPlayer(String name) {
        list.add(name);
        table.setItems(list);
    }

    @Override
    public void removePlayer(String name) {
        list.remove(name);
        table.setItems(list);
    }

    @Override
    public void clearPlayers() {
        list.clear();
        table.setItems(list);
    }
}