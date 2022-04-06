package client.scenes.framecomponents;

import client.scenes.MainCtrl;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javax.inject.Inject;

/**
 * Controller for emotes displayed in right
 */
public class EmoteContainerCtrl {

    private final MainCtrl mainCtrl;

    @FXML
    public ImageView emoticon;
    @FXML
    public Text person;
    @FXML
    private BorderPane featureBackground;

    @Inject
    public EmoteContainerCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    /**
     * Sets fields of this class
     *
     * @param name        Name of person
     * @param pathToImage The path to the image
     */
    public void initialize(String name, String pathToImage, String color) {
        person.setText(name.equals(mainCtrl.getUsername()) ? "You" : name);
        emoticon.setImage(new Image(pathToImage));
        featureBackground.setStyle("-fx-background-color: " + color);
    }
}
