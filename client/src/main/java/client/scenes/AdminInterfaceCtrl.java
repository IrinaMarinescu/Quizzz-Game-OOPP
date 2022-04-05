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
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
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
    @FXML
    private AnchorPane root;

    private final MainCtrl mainCtrl;
    private final ServerUtils serverUtils;

    @Inject
    public AdminInterfaceCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
    }

    /**
     * {@inheritDoc}
     *
     * @param activities the list of activities to populate the table with.
     */
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

    /**
     * {@inheritDoc}
     *
     * @param newActivity the new activity to show on the left hand side of the page.
     */
    public void updateActivityData(Activity newActivity) {
        activityGrid.setVisible(true);
        activityImage.setImage(
            new Image(serverUtils.getServerIP() + "api/activities/image/" + newActivity.id,
                270, 200, false, false)
        );
        activityIdField.setText(newActivity.id);
        activityTitleField.setText(newActivity.title);
        activityConsumptionField.setText(String.valueOf(newActivity.consumptionInWh));
        activitySourceField.setText(newActivity.source);
    }

    /**
     * {@inheritDoc}
     */
    @FXML
    public void showMainFrame() {
        mainCtrl.showMainFrame();
    }

    /**
     * {@inheritDoc}
     */
    @FXML
    public void updateSelectedActivity() {
        Activity currentActivity = activityTable.getSelectionModel().getSelectedItem();
        currentActivity.title = activityTitleField.getText();
        currentActivity.consumptionInWh = Long.parseLong(activityConsumptionField.getText());
        currentActivity.source = activitySourceField.getText();

        serverUtils.sendActivityUpdate(currentActivity);
        activityTable.refresh();
    }

    /**
     * {@inheritDoc}
     */
    @FXML
    public void deleteSelectedActivity() {
        Activity currentActivity = activityTable.getSelectionModel().getSelectedItem();
        serverUtils.deleteActivity(currentActivity);
        activityTable.getItems().remove(currentActivity);
        activityGrid.setVisible(false);
    }

    /**
     * {@inheritDoc}
     */
    @FXML
    public void showAddDialog() {
        Stage dialog = new Stage();
        root.setEffect(new GaussianBlur(15));
        mainCtrl.showAddActivityDialog(dialog);

        root.setEffect(null);
    }

    /**
     * {@inheritDoc}
     *
     * @param activity the activity instance to add to the table
     */
    public void addActivityToTable(Activity activity) {
        activityTable.getItems().add(activity);
    }

    public ObservableList<Activity> getActivities() {
        return activityTable.getItems();
    }

    public void keyPressed(KeyCode e) {
        switch (e) {
            case ESCAPE:
                showMainFrame();
                break;
            default:
                break;
        }
    }
}
