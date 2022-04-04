package client.scenes;

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
import javafx.scene.text.Text;
import javax.inject.Inject;

/**
 * The Lobby class
 */
public class LobbyCtrl implements Initializable {

    @FXML
    private TableColumn<String, String> name;
    @FXML
    private TableView<String> table;
    @FXML
    private Text startGameError;

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
        displayStartGameError(false);
    }


    /**
     * Calls startMultiplayerGame() method in mainCtrl
     */
    @FXML
    public void initializeMultiplayerGame() {
        if (lobby.getPlayers().size() > 1) {
            displayStartGameError(false);
            gameUtils.startMultiplayerGame();
        } else {
            displayStartGameError(true);
        }
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

    /**
     * Display the start game error
     *
     * @param show If true displays the error otherwise hides it
     */
    public void displayStartGameError(boolean show) {
        startGameError.setVisible(show);
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