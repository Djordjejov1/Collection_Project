package at.ac.fhcampus.simple_manager.Controllers;

import at.ac.fhcampus.simple_manager.Models.CollectionEntry;
import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import at.ac.fhcampus.simple_manager.MainApp;
import at.ac.fhcampus.simple_manager.Models.EntryType;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AddNewEntryController {
    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private ComboBox<EntryType> typeComboBox;

    @FXML
    private Button backButton;

    @FXML
    private Button saveButton;

    @FXML
    public void initialize() {
        // Dropdown befüllen
        typeComboBox.getItems().setAll(EntryType.values());
        typeComboBox.getSelectionModel().selectFirst(); // Default: BOOK

        // Save Button nur aktiv, wenn alle Eingaben gesetzt sind
        BooleanBinding invalidInput = titleField.textProperty().isEmpty()
                .or(authorField.textProperty().isEmpty())
                .or(typeComboBox.valueProperty().isNull());

        saveButton.disableProperty().bind(invalidInput);
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            MainApp.showMainView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        EntryType type = typeComboBox.getValue();


        int newID = MainApp.getEntries().size() + 1;
        CollectionEntry newEntry = new CollectionEntry(newID , title, author, type);
        MainApp.getEntries().add(newEntry); //fügt ganz klassich ein neuen eintrag in die allgemeine liste ein !

        try {
            MainApp.showMainView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
