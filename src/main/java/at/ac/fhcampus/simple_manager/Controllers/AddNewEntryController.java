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
import java.util.List;


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
        try {
            // 1. DATEIAUSWAHL-DIALOG
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import JSON (Single or List)");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json")
            );

            // Setzt den Startordner auf /imports, falls vorhanden
            File defaultDir = new File("imports");
            if (defaultDir.exists()) {
                fileChooser.setInitialDirectory(defaultDir);
            }

            // Wir nutzen importButton statt stage direkt
            Stage stage = (Stage) importButton.getScene().getWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile == null) return; // Abbrechen, falls der User das Fenster schließt

            // 2. DATEN LADEN
            // Nutzt die Methode aus JsonExportService.java
            JsonExportService service = new JsonExportService();
            List<CollectionEntry> importedEntries = service.importAny(selectedFile.getAbsolutePath());

            if (importedEntries == null || importedEntries.isEmpty()) {
                showError("Invalid File", "The JSON file is empty or formatted incorrectly.");
                return;
            }

            // 3. LOGIK-ENTSCHEIDUNG: Einzeln oder Liste?

            // FALL A: EINZELNER EINTRAG
            if (importedEntries.size() == 1) {
                CollectionEntry entry = (CollectionEntry) ((List<?>) importedEntries).get(0);

                // Schaut ob die File leer ist
                if (isInvalid(entry)) {
                    showError("Import Denied", "The Entry in the file contains empty fields.");
                    return;
                }
                // Erst prüfen, ob dieser Eintrag schon existiert
                if (MainApp.entryAlreadyExists(entry.getTitle(), entry.getAuthor(), entry.getType(), null)) {
                    showDuplicateAlert();
                } else {
                    // Felder im GUI-Formular befüllen (User muss danach noch auf "Save" klicken)
                    importFileField.setText(selectedFile.getName());
                    titleField.setText(entry.getTitle());
                    authorField.setText(entry.getAuthor());
                    typeComboBox.setValue(entry.getType());
                }
            }
            // FALL B: EINE LISTE (MEHRERE EINTRÄGE)
            else {
                int addedCount = 0;
                int duplicateCount = 0;
                int invalidCount = 0;

                for (CollectionEntry entry : importedEntries) {
                    // Duplikat-Check für jeden einzelnen Eintrag in der Liste
                    // skipt leere Stellen in der Liste
                    if (isInvalid(entry)) {
                        invalidCount++;
                        continue;
                    }
                    if (MainApp.entryAlreadyExists(entry.getTitle(), entry.getAuthor(), entry.getType(), null)) {
                        duplicateCount++;
                    } else {
                        // Falls neu: ID generieren (aktuelle Größe + 1)
                        int nextID = MainApp.getEntries().size() + 1;
                        // Neues Objekt erstellen und zur globalen Liste in MainApp hinzufügen
                        CollectionEntry newEntry = new CollectionEntry(nextID, entry.getTitle(), entry.getAuthor(), entry.getType());
                        MainApp.getEntries().add(newEntry);
                        addedCount++;
                    }
                }

                // 4. FEEDBACK AN DEN USER
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Import Completed");
                info.setHeaderText(addedCount + " Entries Successfully Added");
                info.setContentText("New entries: " + addedCount + "\nSkipped (duplicates): " + duplicateCount);
                info.showAndWait();

                // Falls Einträge importiert wurden, direkt zur Hauptübersicht wechseln
                if (addedCount > 0) {MainApp.showMainView();
                }
            }
        } catch (Exception e) {
            showError("Import Error", "Please check the JSON format of your file.");
            e.printStackTrace();
        }
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

    // Helfer-Methode in dem man Entries nach Inhalt checken kann
    private boolean isInvalid(CollectionEntry entry) {
        return entry.getTitle() == null || entry.getTitle().isBlank() ||
                entry.getAuthor() == null || entry.getAuthor().isBlank() ||
                entry.getType() == null;
    }
}
