package client.scenes;

import javafx.fxml.FXML;
import javax.inject.Inject;

public class OpenQuestion {

    private MainCtrl mainCtrl;
    private QuestionFrameCtrl questionFrameCtrl;


    /**
     * Injects necessary dependencies
     * @param mainCtrl - the main front-ent controller
     * @param questionFrameCtrl - the scene into which it has to be injected
     */
    @Inject
    public OpenQuestion(MainCtrl mainCtrl, QuestionFrameCtrl questionFrameCtrl) {
        this.mainCtrl = mainCtrl;
        this.questionFrameCtrl = questionFrameCtrl;
    }
}
