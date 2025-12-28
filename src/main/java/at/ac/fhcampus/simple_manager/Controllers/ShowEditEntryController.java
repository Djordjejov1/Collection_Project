package at.ac.fhcampus.simple_manager.Controllers;

import at.ac.fhcampus.simple_manager.MainApp;
import at.ac.fhcampus.simple_manager.Models.CollectionEntry;
import at.ac.fhcampus.simple_manager.Models.EntryType;
import at.ac.fhcampus.simple_manager.Services.JsonExportService;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.nio.file.Path;

import static at.ac.fhcampus.simple_manager.MainApp.getEntries;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ShowEditEntryController {



    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private ComboBox<EntryType> typeComboBox;
    @FXML
    private Button updateButton;
    @FXML
    private TextArea jsonPreviewArea;


    private CollectionEntry currentEntry;
    private String originalTitle;
    private String originalAuthor;
    private EntryType originalType;
    private final BooleanProperty hasChanges = new SimpleBooleanProperty(false);

    //TODO fixing
    @FXML
    public void initialize() {

        currentEntry = MainApp.getSelectedEntry(); // nimmt den ausgewählten beitrag!

        // Orginalwerte merken
        originalTitle = currentEntry.getTitle();
        originalAuthor = currentEntry.getAuthor();
        originalType = currentEntry.getType();

        // Dropdown befüllen
        typeComboBox.getItems().setAll(EntryType.BOOK,
                EntryType.CD,
                EntryType.DVD);
        typeComboBox.getSelectionModel().selectFirst(); // Default: BOOK

        // Felder setzen
        titleField.setText(currentEntry.getTitle());
        authorField.setText(currentEntry.getAuthor());
        typeComboBox.setValue(currentEntry.getType());

        updatePreview(); // "Json part"


        // Listener für Änderungen
        updateButton.setDisable(true);
        titleField.textProperty().addListener((obs, oldVal, newVal) -> markChanged());
        authorField.textProperty().addListener((obs, oldVal, newVal) -> markChanged());
        typeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> markChanged());

        // Update-Button-Logik
        BooleanBinding invalidInput =
                titleField.textProperty().isEmpty()
                        .or(authorField.textProperty().isEmpty())
                        .or(typeComboBox.valueProperty().isNull());

        updateButton.disableProperty().bind(
                invalidInput.or(hasChanges.not())
        );

        hasChanges.set(false);

    }

    private void markChanged() {
        hasChanges.set(true);
        updatePreview();
    }


    private void updatePreview() {
        if (currentEntry == null) return;

        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        EntryType type = typeComboBox.getValue();

        String preview = """
        {
          "id": %d,
          "title": "%s",
          "author": "%s",
          "type": "%s"
        }
        """.formatted(currentEntry.getId(), title, author, type);

        jsonPreviewArea.setText(preview);
    }



    @FXML
    public void handleBack(ActionEvent actionEvent) {
        try{
            MainApp.showMainView();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleUpdate(ActionEvent actionEvent) {
           if (currentEntry == null) return;

        // Neue Werte aus den Feldern holen
        String newTitle = titleField.getText().trim();
        String newAuthor = authorField.getText().trim();
        EntryType newType = typeComboBox.getValue();


        // DUPLIKAT-PRÜFUNG (IGNORIERT das aktuelle Entry selbst)
        if (MainApp.entryAlreadyExists(newTitle, newAuthor, newType, currentEntry)) {  // ← extrem wichtig!

            showDuplicateAlert();
            return;
        }

        // Daten ins Model schreiben
        currentEntry.setTitle(newTitle);
        currentEntry.setAuthor(newAuthor);
        currentEntry.setType(newType);

           // Zurück zur MainView
           try {
               MainApp.showMainView();
           } catch (Exception e) {
               e.printStackTrace();
           }
    }


    private void showDuplicateAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Duplicate Entry");
        alert.setHeaderText("Entry already exists");
        alert.setContentText(
                "This entry already exists in the Collection Manager.\n\n" +
                        "Please change the title, author or type."
        );
        alert.showAndWait();
    }


    @FXML
    public void handleDelete(ActionEvent actionEvent) {
        if (currentEntry == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/at/ac/fhcampus/simple_manager/warning_UI.fxml"));

            Scene scene = new Scene(loader.load());
            Stage popupStage = new Stage();

            popupStage.setTitle("Confirm Delete");
            popupStage.setScene(scene);
            popupStage.setResizable(false);

            WarningController controller = loader.getController();
            controller.setEntryInfo(currentEntry.getTitle() + " - " + currentEntry.getAuthor());

            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.showAndWait();

            if (controller.isConfirmed()) {
                getEntries().remove(currentEntry);
                MainApp.setSelectedEntry(null);
                MainApp.showMainView();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleExportJson(ActionEvent actionEvent) {
        if (currentEntry == null) return;

        try {
            JsonExportService exporter = new JsonExportService();
            Path exportedFile = exporter.exportEntry(currentEntry);

            System.out.println("Exported to: " + exportedFile.toAbsolutePath());

            MainApp.showMainView();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
