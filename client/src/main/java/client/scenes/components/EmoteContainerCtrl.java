package client.scenes.components;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class EmoteContainerCtrl {

  @FXML
  private ImageView emoticon;
  @FXML
  private Text person;

  public void initialize(String name, String pathToImage) {
    person.setText(name);
    emoticon.setImage(new Image(pathToImage));
  }
}
