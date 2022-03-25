package client.scenes;

import client.scenes.controllerrequirements.LeaderboardCtrlRequirements;
import com.google.inject.Inject;
import commons.LeaderboardEntry;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Pair;

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
    private Button back;
    @FXML
    private GridPane buttonGrid;
    @FXML
    private BarChart<String, Integer> barChart;

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

    protected boolean test = false;

    @Inject
    public LeaderboardCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * {@inheritDoc}
     *
     * @param entries a List of instances of LeaderboardEntry.
     * @param maxSize the maximum size of the leaderboard - how many records to show.
     *                if <code>entries</code> has more elements than that, the function trims automatically.
     * @param type    the type of the leaderboard. can either be "solo", "intermediate", "final".
     */
    @Override
    public void initialize(List<LeaderboardEntry> entries, int maxSize, String type) {
        setMaxSize(maxSize);
        setLeaderboardType(type);

        playerColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().getName()));
        scoreColumn.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().scoreToString()));

        fillLeaderboard(sortEntries(entries));
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
        barChart.getData().clear();
        barChart.setLegendVisible(false);

        for (int i = 1; i <= 3; i++) {
            if (entries.size() >= i) {
                generateSerie(entries.get(i - 1), getMedal(i));
            }
        }

        if (entries.size() >= 3) {
            ObservableList<LeaderboardEntry> data = FXCollections.observableList(
                entries.subList(3, entries.size()).stream().map(
                    p -> p.equals(mainCtrl.getPlayer())
                        ? new LeaderboardEntry("You (" + p.getName() + ")", p.getScore()) :
                        p).collect(Collectors.toList()));
            leaderboard.setItems(data);
        }
    }

    /**
     * Generate a new series on the bar chart with data from a leaderboard entry.
     * Also sets the colour of the bar according to the number of points the player got:
     * <ul>
     *     <li>gold for 1st place</li>
     *     <li>silver for 2nd place</li>
     *     <li>bronze for 3rd place</li>
     * </ul>
     *
     * @param entry a LeaderboardEntry instance, with the player to add on the bar chart.
     * @param colour the colour of the bar
     */
    private void generateSerie(LeaderboardEntry entry, String colour) {
        XYChart.Series<String, Integer> serie = new XYChart.Series<>();
        serie.getData().add(new XYChart.Data<>(entry.getName(), entry.getScore()));

        barChart.getData().add(serie);

        for (XYChart.Data<String, Integer> data : serie.getData())  {
            data.getNode().setStyle("-fx-bar-fill: " + colour + ";");
        }
    }

    /**
     * Returns a colour based on the place the player got.
     *
     * @param place an integer from 1, 2, or 3.
     * @return a HEX colour based on the place in the leaderboard.
     * <ul>
     *     <li>gold for 1st place</li>
     *     <li>silver for 2nd place</li>
     *     <li>bronze for 3rd place</li>
     * </ul>
     */
    protected String getMedal(int place) {
        switch (place) {
            case 1: return "#d4af37";
            case 2: return "#c0c0c0";
            default: return "#cd7f32";
        }
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
            back.setVisible(buttonVisibility);
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
        }
    }

    /**
     * Redirects the player to play a game of the same type again.
     */
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

    /**
     * Redirects the user to the main frame.
     */
    public void backToMainFrame() {
        mainCtrl.showMainFrame();
    }

    public void keyPressed(KeyCode e) {
        switch (e) {
            case ESCAPE:
                if (type == TYPE_SOLO || type == TYPE_FINAL) {
                    backToMainFrame();
                } else {
                    mainCtrl.disconnect();
                }
                break;
            case M:
                if (type == TYPE_FINAL) {
                    // TODO rejoin lobby
                }
                break;
            default:
                break;
        }
    }
}
