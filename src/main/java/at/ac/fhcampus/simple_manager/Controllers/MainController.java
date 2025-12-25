package at.ac.fhcampus.simple_manager.Controllers;

import at.ac.fhcampus.simple_manager.MainApp;
import at.ac.fhcampus.simple_manager.Models.CollectionEntry;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class MainController {

    @FXML private TextField searchField;
    @FXML private ListView<CollectionEntry> entryListView;
    @FXML private Button addEntryButton;
    @FXML private Button editEntryButton;

    @FXML
    public void initialize() {
        entryListView.setItems(MainApp.getEntries());
        editEntryButton.setDisable(true);

        entryListView.setCellFactory(lv -> new ListCell<>() {

            private final RadioButton radioButton = new RadioButton();
            private final Label label = new Label();
            private final HBox row = new HBox(10);

            {
                row.getChildren().addAll(radioButton, label);

                radioButton.setOnAction(e -> {
                    CollectionEntry entry = getItem();
                    if (entry == null) return;

                    if (entry.isSelected()) {
                        deselectAll();
                    } else {
                        selectEntry(entry);
                    }
                });
            }

            @Override
            protected void updateItem(CollectionEntry item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item.toString());
                    radioButton.setSelected(item.isSelected());
                    setGraphic(row);
                }
            }
        });
    }

    private void selectEntry(CollectionEntry selected) {
        for (CollectionEntry entry : MainApp.getEntries()) {
            entry.setSelected(false);
        }
        selected.setSelected(true);

        editEntryButton.setDisable(false);
        MainApp.setSelectedEntry(selected);

        entryListView.refresh();
    }

    private void deselectAll() {
        for (CollectionEntry entry : MainApp.getEntries()) {
            entry.setSelected(false);
        }

        editEntryButton.setDisable(true);
        MainApp.setSelectedEntry(null);

        entryListView.refresh();
    }

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
        CollectionEntry selected = MainApp.getSelectedEntry();

        if (selected == null) {
            System.out.println("Bitte zuerst einen Eintrag auswählen!");
            return;
        }

        try {
            MainApp.showEditEntryView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
