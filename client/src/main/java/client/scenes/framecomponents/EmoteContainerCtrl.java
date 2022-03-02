package client.scenes.framecomponents;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * Controller for emotes displayed in right
 */
public class EmoteContainerCtrl {

    @FXML
    public ImageView emoticon;
    @FXML
    public Text person;

    /**
     * Sets fields of this class
     *
     * @param name        Name of person
     * @param pathToImage The path to the image
     */
    public void initialize(String name, String pathToImage) {
        person.setText(name);
        if (pathToImage != null) {
            emoticon.setImage(new Image(pathToImage));
        }
    }
}
