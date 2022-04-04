package client.scenes;

import client.scenes.controllerrequirements.LeaderboardCtrlRequirements;
import com.google.inject.Inject;
import commons.LeaderboardEntry;
import java.util.List;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;

public class LeaderboardCtrl implements LeaderboardCtrlRequirements {

    @FXML
    private VBox leaderboard;
    @FXML
    private Text pageTitle;
    @FXML
    private Button back;
    @FXML
    private HBox buttonsPart;

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

    public static final String FIRST_PLACE = "firstPlace";
    public static final String SECOND_PLACE = "secondPlace";
    public static final String THIRD_PLACE = "thirdPlace";
    public static final String ODD_PLACE = "lightBlue";
    public static final String EVEN_PLACE = "lightishBlue";


    public static final int minBarWidth = 300;
    public static final int changeBarWidth = 700;


    private final MainCtrl mainCtrl;
    private int type;
    private int maxSize;

    protected Pair<String, String> texts;
    protected boolean backButtonVisible;
    protected boolean buttonGridVisible;
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
     * @param entries the sorted by score entries to be put in the leaderboard
     */
    private void fillLeaderboard(List<LeaderboardEntry> entries) {
        leaderboard.getChildren().clear();
        if (entries.isEmpty()) {
            return;
        }

        int maxScore = entries.get(0).getScore() == 0 ? 1 : entries.get(0).getScore();

        for (int i = 0; i < entries.size(); i++) {
            leaderboard.getChildren()
                .add(generateBar(entries.get(i), (double) entries.get(i).getScore() / maxScore, getColor(i)));
        }
    }

    /**
     * Generate a new bar with appropriate width, color and name of the player
     *
     * @param entry  a LeaderboardEntry instance, with the player to add on the leaderboard
     * @param width  value from 0 to 1, describing how long the bar should be
     * @param colour the colour of the bar
     * @return a Label with properties of the entry and the set colour
     */
    private HBox generateBar(LeaderboardEntry entry, double width, String colour) {
        Text name = new Text(
            entry.getName().equals(mainCtrl.getUsername()) ? "You (" + entry.getName() + ")" : entry.getName());
        name.getStyleClass().add("leaderboardText");
        Text score = new Text(Integer.toString(entry.getScore()));
        score.getStyleClass().add("leaderboardText");
        Region region = new Region();
        HBox row = new HBox();

        HBox.setHgrow(region, Priority.ALWAYS);

        row.getChildren().add(name);
        row.getChildren().add(region);
        row.getChildren().add(score);

        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle("-fx-background-color: -" + colour + ";");
        row.setMinWidth(minBarWidth + changeBarWidth * width);
        row.setPrefWidth(minBarWidth + changeBarWidth * width);
        row.setMaxWidth(minBarWidth + changeBarWidth * width);

        row.getStyleClass().add("leaderboardBox");

        return row;
    }

    /**
     * Returns a colour based on the place the player got.
     *
     * @param place an integer of place the player has
     * @return a HEX colour based on the place in the leaderboard.
     */
    protected String getColor(int place) {
        switch (place) {
            case 0:
                return FIRST_PLACE;
            case 1:
                return SECOND_PLACE;
            case 2:
                return THIRD_PLACE;
            default:
                return place % 2 == 0 ? EVEN_PLACE : ODD_PLACE;
        }
    }

    /**
     * Sets the type of the leaderboard. Also updates the necessary text.
     *
     * @param type can either be "solo", "intermediate" or "final".
     *             If something else is put, the method automatically sets the type to solo as placeholder.
     */
    protected void setLeaderboardType(String type) {
        if (type.equals("intermediate")) {
            setType(TYPE_INTERMED);
            texts = new Pair<>("Intermediate Leaderboard", "Scores after 10 rounds - go get 'em!");
            setButtonAndGrid(false, false);
        } else if (type.equals("final")) {
            setType(TYPE_FINAL);
            this.type = TYPE_FINAL;
            texts = new Pair<>("Final Leaderboard", "Final scores");
            setButtonAndGrid(false, true);
        } else {
            setType(TYPE_SOLO);
            texts = new Pair<>("Global Leaderboard", "All-time top players in singleplayer");
            setButtonAndGrid(true, false);
        }
        formatLabels();
    }

    /**
     * Sets the visibility of the button and the button grid at the bottom of the page.
     *
     * @param buttonVisibility a boolean value, representing whether the back button should be visible or not.
     * @param gridVisibility   a boolean value, representing whether the bottom button grid should be visible or not.
     */
    protected void setButtonAndGrid(boolean buttonVisibility, boolean gridVisibility) {
        backButtonVisible = buttonVisibility;
        buttonGridVisible = gridVisibility;
        if (!test) {
            back.setVisible(buttonVisibility);
            buttonsPart.setVisible(gridVisibility);
        }
    }

    /**
     * Sets both the labels for the page and table title to the values needed for the specific leaderboard type.
     */
    protected void formatLabels() {
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
     *             <ul>
     *                 <li><code>LeaderboardCtrl.TYPE_SOLO</code> = 1</li>
     *                 <li><code>LeaderboardCtrl.TYPE_INTERMED</code> = 2</li>
     *                 <li><code>LeaderboardCtrl.TYPE_FINAL</code> = 3</li>
     *             </ul>
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
                    mainCtrl.exitGameChecker(1);
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
