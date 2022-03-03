package client.scenes;

import client.scenes.controllerrequirements.LeaderboardCtrlRequirements;
import commons.LeaderboardEntry;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.stream.Collectors;

public class LeaderboardCtrl implements LeaderboardCtrlRequirements {

    @FXML
    private TableView<LeaderboardEntry> leaderboard;
    @FXML
    private TableColumn<LeaderboardEntry, String> playerColumn;
    @FXML
    private TableColumn<LeaderboardEntry, String> scoreColumn;

    /**
     * Field <code>type</code> in <code>LeaderboardCtrl</code> denotes the type of leaderboard to be shown. The controller will handle:
     * <ul>
     *     <li>the solo, all-time leaderboard - <code>type = TYPE_SOLO</code></li>
     *     <li>intermediate leaderboards for multiplayer games - <code>type = TYPE_INTERMED/code></li>
     *     <li>the final leaderboard for multiplayer games - <code>type = TYPE_FINAL</code></li>
     * </ul>
     */
    private final int TYPE_SOLO = 1,
                      TYPE_INTERMED = 2,
                      TYPE_FINAL = 3;

    private int type,
                maxSize;

    @Override
    public void initialize(List<LeaderboardEntry> entries, int maxSize, String type) {
        this.maxSize = maxSize;
        setLeaderboardType(type);

        playerColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getName()));
        scoreColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getScoreString()));

        entries = entries.stream().sorted().limit(this.maxSize).collect(Collectors.toList());
        fillLeaderboard(entries);
    }

    private void fillLeaderboard(List<LeaderboardEntry> entries) {
        ObservableList<LeaderboardEntry> data = FXCollections.observableList(entries);
        leaderboard.setItems(data);
    }

    private void setLeaderboardType(String type) {
        if(type.equals("intermediate")) this.type = TYPE_INTERMED;
        else if(type.equals("final")) this.type = TYPE_FINAL;
        else this.type = TYPE_SOLO;
    }
}
