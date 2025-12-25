package at.ac.fhcampus.simple_manager.Controllers;

import at.ac.fhcampus.simple_manager.MainApp;
import at.ac.fhcampus.simple_manager.Models.CollectionEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MainController {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<CollectionEntry> entryListView;

    @FXML
    private Button addEntryButton;

    @FXML
    private Button editEntryButton;

    @FXML
    private void handleAddEntry(ActionEvent event) {
        try {
            MainApp.showAddView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleShowEditEntry(ActionEvent event) {
        try {
            MainApp.showEditEntryView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        // Liste aus MainApp anzeigen
        entryListView.setItems(MainApp.getEntries());
    }
}
