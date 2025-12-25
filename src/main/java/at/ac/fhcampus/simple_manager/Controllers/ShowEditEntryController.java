package at.ac.fhcampus.simple_manager.Controllers;

import at.ac.fhcampus.simple_manager.MainApp;
import at.ac.fhcampus.simple_manager.Models.CollectionEntry;
import at.ac.fhcampus.simple_manager.Models.EntryType;
import at.ac.fhcampus.simple_manager.Services.JsonExportService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.nio.file.Path;

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

    //TODO fixing
    @FXML
    public void initialize() {
        // Dropdown befüllen
        typeComboBox.getItems().setAll(EntryType.values());
        typeComboBox.getSelectionModel().selectFirst(); // Default: BOOK

        currentEntry = MainApp.getSelectedEntry(); // nimmt den ausgewählten beitrag!


        titleField.setText(currentEntry.getTitle());
        authorField.setText(currentEntry.getAuthor());
        typeComboBox.setValue(currentEntry.getType());

        updatePreview(); // "Json part"

        updateButton.setDisable(true);
        titleField.textProperty().addListener((obs, oldVal, newVal) -> markChanged());
        authorField.textProperty().addListener((obs, oldVal, newVal) -> markChanged());
        typeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> markChanged());
    }

    private void markChanged() {
        updatePreview();
        updateButton.setDisable(false);
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

        // Daten ins Model schreiben
        currentEntry.setTitle(titleField.getText().trim());
        currentEntry.setAuthor(authorField.getText().trim());
        currentEntry.setType(typeComboBox.getValue());

        // Zurück zur MainView
        try {
            MainApp.showMainView();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                MainApp.getEntries().remove(currentEntry);
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
