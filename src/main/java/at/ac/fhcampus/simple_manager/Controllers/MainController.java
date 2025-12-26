package at.ac.fhcampus.simple_manager.Controllers;

import at.ac.fhcampus.simple_manager.MainApp;
import at.ac.fhcampus.simple_manager.Models.CollectionEntry;
import at.ac.fhcampus.simple_manager.Models.EntryType;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

public class MainController {

    @FXML private TextField searchField;
    @FXML private ListView<CollectionEntry> entryListView;
    @FXML private Button addEntryButton;
    @FXML private Button editEntryButton;
    @FXML private ComboBox<EntryType> typeFilterBox;


    @FXML
    public void initialize() {

        typeFilterBox.setPromptText("Type");
        typeFilterBox.getItems().addAll(EntryType.values());
        typeFilterBox.setValue(EntryType.ALL);

        // FilteredList: basiert auf der Original-Liste
        FilteredList<CollectionEntry> filteredEntries =
                new FilteredList<>(MainApp.getEntries(), entry -> true);

        // ListView zeigt die gefilterte Liste
        entryListView.setItems(filteredEntries);

        // Live-Filter: sobald man in der Searchbar tippt
        searchField.textProperty().addListener((obs, oldText, newText) -> {
            applyFilter(filteredEntries);
        });

        typeFilterBox.valueProperty().addListener((obs, oldType, newType) -> {
            applyFilter(filteredEntries);
        });

        // Edit Button am Anfang deaktivieren
        editEntryButton.setDisable(true);

        // CellFactory mit RadioButtons bleibt wie gehabt (dein Code)
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

    private void applyFilter(FilteredList<CollectionEntry> filteredEntries) {
        String searchText = searchField.getText();
        EntryType selectedType = typeFilterBox.getValue();
// hi momo war hier
        filteredEntries.setPredicate(entry -> {

            // Text-Filter (Titel ODER Autor) oder Yarrak lol gamer sibi doppelyarrak
            boolean matchesText = true;
            if (searchText != null && !searchText.isBlank()) {
                String search = searchText.toLowerCase().trim();

                matchesText =
                        entry.getTitle().toLowerCase().contains(search)
                                || entry.getAuthor().toLowerCase().contains(search);
            }

            // Type-Filter (ComboBox)
            boolean matchesType = true;
            if (selectedType != null && selectedType != EntryType.ALL) {
                matchesType = entry.getType() == selectedType;
            }

            return matchesText && matchesType;
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
