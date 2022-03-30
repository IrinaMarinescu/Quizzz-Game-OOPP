package client.scenes;

import client.utils.ServerUtils;
import commons.LeaderboardEntry;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javax.inject.Inject;

public class FinalScreenCtrl {

    private MainCtrl mainCtrl;
    private QuestionFrameCtrl questionFrameCtrl;
    private ServerUtils serverUtils;

    @FXML
    Text scoreField;

    /**
     * Injects necessary dependencies
     *
     * @param mainCtrl          the main front-end controller
     * @param questionFrameCtrl the scene into which it has to be injected
     */
    @Inject
    public FinalScreenCtrl(MainCtrl mainCtrl, QuestionFrameCtrl questionFrameCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.questionFrameCtrl = questionFrameCtrl;
        this.serverUtils = serverUtils;
    }

    @FXML
    public void setPoints(int points) {
        serverUtils.addLeaderboardEntry(new LeaderboardEntry(mainCtrl.getUsername(), points));
        scoreField.setText(String.valueOf(points));
    }
}
