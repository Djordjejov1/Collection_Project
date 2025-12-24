package at.ac.fhcampus.simple_manager.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MainController {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> entryListView;

    @FXML
    private Button addEntryButton;

    @FXML
    private Button editEntryButton;

    @FXML
    private void handleAddEntry(ActionEvent event) {
        System.out.println("Add clicked");
        entryListView.getItems().add("Test Entry");
    }

    @FXML
    private void handleShowEditEntry(ActionEvent event) {
        System.out.println("Show/Edit clicked");
        System.out.println("Selected: " + entryListView.getSelectionModel().getSelectedItem());
    }
}
