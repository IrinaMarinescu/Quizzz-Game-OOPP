package client.scenes;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class QuestionTrueFalseCtrl implements Initializable {

    private final MainCtrl mainCtrl;

    @FXML
    private Button falseAnswer;

    @FXML
    private ImageView image;

    @FXML
    private Text questionText;

    @FXML
    private Button trueAnswer;

    @Inject
    public QuestionTrueFalseCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
//
//    public void onTrueButtonHover() {
//        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), trueAnswer);
//    }
//        while (trueAnswer.isHover()) {
//
//    }

}