package client.scenes;

import client.scenes.controllerrequirements.LeaderboardCtrlRequirements;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.LeaderboardEntry;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class LeaderboardCtrl implements LeaderboardCtrlRequirements {

    @FXML
    private Button back;
    @FXML
    private TableView<LeaderboardEntry> leaderboard;
    @FXML
    private TableColumn<LeaderboardEntry, String> playerColumn;
    @FXML
    private TableColumn<LeaderboardEntry, String> scoreColumn;

    /**
     * Field <code>type</code> in <code>LeaderboardCtrl</code>
     * denotes the type of leaderboard to be shown. The controller will handle:
     * <ul>
     *     <li>the solo, all-time leaderboard - <code>type = TYPE_SOLO</code></li>
     *     <li>intermediate leaderboards for multiplayer games - <code>type = TYPE_INTERMED</code></li>
     *     <li>the final leaderboard for multiplayer games - <code>type = TYPE_FINAL</code></li>
     * </ul>
     */

    public static final int TYPE_SOLO = 1;
    public static final int TYPE_INTERMED = 2;
    public static final int TYPE_FINAL = 3;

    private final MainCtrl mainCtrl;
    private int type;
    private int maxSize;

    @Inject
    public LeaderboardCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * {@inheritDoc}
     *
     * @param entries a List of instances of LeaderboardEntry.
     *
     * @param maxSize the maximum size of the leaderboard - how many records to show.
     *                if <code>entries</code> has more elements than that, the function trims automatically.
     *
     * @param type the type of the leaderboard. can either be "solo", "intermediate", "final".
     */
    @Override
    public void initialize(List<LeaderboardEntry> entries, int maxSize, String type) {
        setMaxSize(maxSize);
        setLeaderboardType(type);

        playerColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getName()));
        scoreColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getScoreString()));

        fillLeaderboard(entries);
    }

    /**
     * Helper method. Used to sort and limit the entries given to the controller.
     *
     * @param entries the list of entries to be shown in the leaderboard
     * @return a list, containing the entries sorted by score.
     */
    protected List<LeaderboardEntry> sortEntries(List<LeaderboardEntry> entries) {
        return entries.stream().sorted().limit(this.maxSize).collect(Collectors.toList());
    }

    /**
     * Helper method. Fills the FXML with the entries.
     *
     * @param entries the entries to be put in the leaderboard, in any order. The method will sort the entries by score.
     */
    private void fillLeaderboard(List<LeaderboardEntry> entries) {
        ObservableList<LeaderboardEntry> data = FXCollections.observableList(sortEntries(entries));
        leaderboard.setItems(data);
    }

    /**
     * Sets the type of the leaderboard.
     *
     * @param type can either be "solo", "intermediate" or "final".
     *             If something else is put, the method automatically sets the type to solo as placeholder.
     */
    protected void setLeaderboardType(String type) {
        if (type.equals("intermediate")) {
            this.type = TYPE_INTERMED;
        } else if (type.equals("final")) {
            this.type = TYPE_FINAL;
        } else {
            this.type = TYPE_SOLO;
        }
    }

    public int getType() {
        return type;
    }

    public int getMaxSize() {
        return maxSize;
    }

    protected void setType(int type) {
        this.type = type;
    }

    protected void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void backToMainFrame(){mainCtrl.showMainFrame();}
}
