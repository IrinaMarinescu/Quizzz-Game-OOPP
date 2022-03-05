package client.scenes;


import client.scenes.controllerrequirements.LobbyCtrlRequirements;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


public class LobbyCtrl implements Initializable, LobbyCtrlRequirements {

    @FXML
    private TableColumn<String, String> name;

    @FXML
    private TableView<String> table;

    @FXML
    void initializeMultiplayerGame(ActionEvent event) {

    }

    @FXML
    public void goBack(ActionEvent actionEvent) {

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
    }

    @Override
    public void removePlayer(String name) {
        list.remove(name);
    }

    @Override
    public void clearPlayers() {
        list.clear();
    }
}