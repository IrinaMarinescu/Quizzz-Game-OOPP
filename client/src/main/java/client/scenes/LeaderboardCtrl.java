package client.scenes;

import client.scenes.controllerrequirements.LeaderboardCtrlRequirements;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Pair;
import javax.inject.Inject;

public class LeaderboardCtrl implements LeaderboardCtrlRequirements {

    @FXML
    private TableView<LeaderboardEntry> leaderboard;
    @FXML
    private TableColumn<LeaderboardEntry, String> playerColumn;
    @FXML
    private TableColumn<LeaderboardEntry, String> scoreColumn;
    @FXML
    private Text pageTitle;
    @FXML
    private Button leaderboardTitle;
    @FXML
    private ImageView backBtn;
    @FXML
    private GridPane buttonGrid;

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

    private int type;
    private int maxSize;

    private MainCtrl mainCtrl;

    protected boolean test = false;

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
     * Sets the type of the leaderboard. Also updates the necessary text.
     *
     * @param type can either be "solo", "intermediate" or "final".
     *             If something else is put, the method automatically sets the type to solo as placeholder.
     */
    protected void setLeaderboardType(String type) {
        Pair<String, String> texts;
        if (type.equals("intermediate")) {
            this.type = TYPE_INTERMED;
            texts = new Pair<>("Intermediate Leaderboard", "Scores after 10 rounds - go get 'em!");
            setButtonAndGrid(false, false);
        } else if (type.equals("final")) {
            this.type = TYPE_FINAL;
            texts = new Pair<>("Final Leaderboard", "Final scores");
            setButtonAndGrid(false, true);
        } else {
            this.type = TYPE_SOLO;
            texts = new Pair<>("Global Leaderboard", "All-time top players in singleplayer");
            setButtonAndGrid(true, false);
        }
        formatLabels(texts);
    }

    /**
     * Sets the visibility of the button and the button grid at the bottom of the page.
     *
     * @param buttonVisibility a boolean value, representing whether the back button should be visible or not.
     * @param gridVisibility a boolean value, representing whether the bottom button grid should be visible or not.
     */
    protected void setButtonAndGrid(boolean buttonVisibility, boolean gridVisibility) {
        if (!test) {
            backBtn.setVisible(buttonVisibility);
            buttonGrid.setVisible(gridVisibility);
        }
    }

    /**
     * Sets both the labels for the page and table title to the values needed for the specific leaderboard type.
     *
     * @param texts a Pair of two strings, containing the titles to be set.
     */
    protected void formatLabels(Pair<String, String> texts) {
        if (!test) {
            pageTitle.setText(texts.getKey());
            leaderboardTitle.setText(texts.getValue());
        }
    }

    /**
     * Redirects the user to the main frame.
     */
    public void showMainFrame() {
        mainCtrl.showMainFrame();
    }

    public void playAgain() {
        // TODO: implement logic for starting another game
    }

    /**
     * Returns the type of the leaderboard.
     *
     * @return an integer, one of the following:
     * <ul>
     *      <li><code>LeaderboardCtrl.TYPE_SOLO</code> = 1</li>
     *      <li><code>LeaderboardCtrl.TYPE_INTERMED</code> = 2</li>
     *      <li><code>LeaderboardCtrl.TYPE_FINAL</code> = 3</li>
     * </ul>
     */
    public int getType() {
        return type;
    }

    /**
     * Returns the maximum size of the leaderboard (how many entries to show at max)
     *
     * @return an integer, representing the maximum number of entries that can be shown in the table.
     */
    public int getMaxSize() {
        return maxSize;
    }


    /**
     * Sets the type of the leaderboard.
     *
     * @param type an integer, one of the following:
     *        <ul>
     *            <li><code>LeaderboardCtrl.TYPE_SOLO</code> = 1</li>
     *            <li><code>LeaderboardCtrl.TYPE_INTERMED</code> = 2</li>
     *            <li><code>LeaderboardCtrl.TYPE_FINAL</code> = 3</li>
     *        </ul>
     */
    protected void setType(int type) {
        this.type = type;
    }


    /**
     * Sets the maximum size of the leaderboard
     *
     * @param maxSize an integer, representing the maximum number of entries that can be shown in the table.
     */
    protected void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
