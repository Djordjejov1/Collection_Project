package at.ac.fhcampus.simple_manager.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class WarningController {

    @FXML
    private Label entryInfoLabel;

    private boolean confirmed = false;

    public void setEntryInfo(String info) {
        entryInfoLabel.setText(info);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    @FXML
    private void handleDelete() {
        confirmed = true;
        closeWindow();
    }

    @FXML
    private void handleCancel() {
        confirmed = false;
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) entryInfoLabel.getScene().getWindow();
        stage.close();
    }
}
