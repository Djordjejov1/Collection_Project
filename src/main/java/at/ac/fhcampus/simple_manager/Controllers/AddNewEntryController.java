package at.ac.fhcampus.simple_manager.Controllers;

import at.ac.fhcampus.simple_manager.Models.CollectionEntry;
import at.ac.fhcampus.simple_manager.Services.JsonExportService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import at.ac.fhcampus.simple_manager.MainApp;
import at.ac.fhcampus.simple_manager.Models.EntryType;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;



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

    @FXML private Button importButton;

    @FXML
    private TextField importFileField;





    @FXML
    public void initialize() {
        // Dropdown befüllen
        typeComboBox.getItems().setAll(EntryType.BOOK,
                EntryType.CD,
                EntryType.DVD);
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

        // 🔴 DUPLIKAT PRÜFEN
        if (MainApp.entryAlreadyExists(title, author, type,null)) {
            showDuplicateAlert();
            return;
        }

        int newID = MainApp.getEntries().size() + 1;
        CollectionEntry newEntry = new CollectionEntry(newID , title, author, type);
        MainApp.getEntries().add(newEntry); //fügt ganz klassich ein neuen eintrag in die allgemeine liste ein !

        try {
            MainApp.showMainView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleImportJson(ActionEvent event) {
        try { // verhindert programm abzustürzen
            // FileChooser erstellen
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import JSON Entry");

            // Nur JSON-Dateien erlauben bzw nur JSON kann ausgewählt werden
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json")
            );

            //Optional: Startordner setzen (macht direkt dein Ordnerdrauf wo du in der Regel dein
            //                              JSON Datei drinnen hast) in Momos Fall in "Imports"
            File defaultDir = new File("imports");
            if (defaultDir.exists()) {
                fileChooser.setInitialDirectory(defaultDir);
            }

            // Finder öffnen bzw Ordner
            Stage stage = (Stage) importButton.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);

            // null ist, wenn User auf Abbrechen klickt
            if (selectedFile == null) {
                return;
            }

            JsonExportService service = new JsonExportService(); // JSON-Service verwenden
            CollectionEntry importedEntry = service.importEntry(selectedFile.getAbsolutePath());


            if (importedEntry == null) { // Fehlerhafte JSON abfangen
                System.out.println("JSON ungültig oder konnte nicht gelesen werden");
                return;
            }


            // 🔴 DUPLIKAT PRÜFEN (JSON)
            if (MainApp.entryAlreadyExists(importedEntry.getTitle(), importedEntry.getAuthor(), importedEntry.getType(), null)) {   // ← kein Ignore nötig beim Add
                showDuplicateAlert();
                return;
            }

            importFileField.setText(selectedFile.getName()); // Dateiname auf Textfield anzeigen

            // Felder automatisch setzen
            titleField.setText(importedEntry.getTitle());
            authorField.setText(importedEntry.getAuthor());
            typeComboBox.setValue(importedEntry.getType());

        } catch (Exception e) {
            showError("Something Went Wrong", "An unexpected error occurred while importing the file.");
            e.printStackTrace(); // falls irgendwas passiert kann man da sehen was schief
        }                       // gelaufen ist
    }



    private void showDuplicateAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Duplicate Entry");
        alert.setHeaderText("Entry already exists");
        alert.setContentText(
                "This entry is already present in the Collection Manager.\n" +
                        "Please choose a different entry."
        );
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
