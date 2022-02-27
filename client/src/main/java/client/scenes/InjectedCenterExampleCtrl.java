package client.scenes;

import javafx.fxml.FXML;
import javax.inject.Inject;

/**
 * Just an example
 */
public class InjectedCenterExampleCtrl {

    private MainCtrl mainCtrl;
    private QuestionFrameCtrl questionFrameCtrl;

    @Inject
    public InjectedCenterExampleCtrl(MainCtrl mainCtrl, QuestionFrameCtrl questionFrameCtrl) {
        this.mainCtrl = mainCtrl;
        this.questionFrameCtrl = questionFrameCtrl;
    }

    @FXML
    private void toggleFromInside() {
        // questionFrameCtrl.toggleLeaderboardVisibility();
    }
}
