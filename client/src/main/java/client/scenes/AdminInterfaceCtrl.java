package client.scenes;

import client.scenes.controllerrequirements.AdminInterfaceCtrlRequirements;
import client.utils.ServerUtils;
import commons.Activity;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.inject.Inject;


public class AdminInterfaceCtrl implements AdminInterfaceCtrlRequirements {

    @FXML
    private TableView<Activity> activityTable;
    @FXML
    private TableColumn<Activity, String> activityId;
    @FXML
    private TableColumn<Activity, String> activityTitle;
    @FXML
    private ImageView activityImage;
    @FXML
    private TextField activityIdField;
    @FXML
    private TextField activityTitleField;
    @FXML
    private TextField activityConsumptionField;
    @FXML
    private TextField activitySourceField;
    @FXML
    private GridPane activityGrid;

    private MainCtrl mainCtrl;
    private ServerUtils serverUtils;

    @Inject
    public AdminInterfaceCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
    }

    public void initialize(List<Activity> activities) {
        activityId.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().id));
        activityTitle.setCellValueFactory(e -> new SimpleStringProperty(e.getValue().title));

        activityTable.setRowFactory(v -> {
            TableRow<Activity> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if ((!row.isEmpty())) {
                    updateActivityData(row.getItem());
                }
            });
            return row;
        });

        ObservableList<Activity> data = FXCollections.observableList(activities);
        activityTable.setItems(data);
        activityGrid.setVisible(false);
    }

    public void updateActivityData(Activity newActivity) {
        activityGrid.setVisible(true);
        activityImage.setImage(
            new Image(serverUtils.getServerIP() + "images/" + newActivity.imagePath,
            270, 200, false, false)
        );
        activityIdField.setText(newActivity.id);
        activityTitleField.setText(newActivity.title);
        activityConsumptionField.setText(String.valueOf(newActivity.consumptionInWh));
        activitySourceField.setText(newActivity.source);
    }

    @FXML
    public void showMainFrame() {
        mainCtrl.showMainFrame();
    }

    @FXML
    public void updateSelectedActivity() {
        Activity currentActivity = activityTable.getSelectionModel().getSelectedItem();
        currentActivity.title = activityTitleField.getText();
        currentActivity.consumptionInWh = Long.parseLong(activityConsumptionField.getText());
        currentActivity.source = activitySourceField.getText();

        serverUtils.sendActivityUpdate(currentActivity);
        activityTable.refresh();
    }

    @FXML
    public void deleteSelectedActivity() {
        Activity currentActivity = activityTable.getSelectionModel().getSelectedItem();
        serverUtils.deleteActivity(currentActivity);
        activityTable.getItems().remove(currentActivity);
        activityGrid.setVisible(false);
    }
}
