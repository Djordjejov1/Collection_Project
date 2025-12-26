package at.ac.fhcampus.simple_manager.Controllers;

import at.ac.fhcampus.simple_manager.Models.CollectionEntry;
import at.ac.fhcampus.simple_manager.Services.JsonExportService;
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

    @FXML private Button importButton;


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
            String fileName = importButton.getText().trim();

            if (fileName.isEmpty()) {
                System.out.println("Bitte Dateiname eingeben z.B:: entry_1.json");
                return;
            }

            JsonExportService service = new JsonExportService();
            CollectionEntry importedEntry = service.importEntry(fileName);

            if (importedEntry == null) {
                System.out.println("Datei nicht gefunden oder JSON ungültig!");
                return;
            }


            int newId = MainApp.getEntries().size() + 1;
            CollectionEntry newEntry = new CollectionEntry(
                    newId,
                    importedEntry.getTitle(),
                    importedEntry.getAuthor(),
                    importedEntry.getType()
            );

            MainApp.getEntries().add(newEntry);
            MainApp.showMainView();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
