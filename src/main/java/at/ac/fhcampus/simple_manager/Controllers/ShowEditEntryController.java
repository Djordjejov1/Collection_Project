package at.ac.fhcampus.simple_manager.Controllers;

import at.ac.fhcampus.simple_manager.MainApp;
import at.ac.fhcampus.simple_manager.Models.CollectionEntry;
import at.ac.fhcampus.simple_manager.Models.EntryType;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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


        //ist ein unnötiger code eigentlich aber dient zur sicherheit!
        if (currentEntry == null){
            titleField.setText("");
            authorField.setText("");
            jsonPreviewArea.setText("No Entry selected.");
        }

        titleField.setText(currentEntry.getTitle());
        authorField.setText(currentEntry.getAuthor());
        typeComboBox.setValue(currentEntry.getType());

        //TODO hier fehlt dann der json part!
        updatePreview();
    }


    private void updatePreview() {
        if (currentEntry == null) return;

        String preview =
                "{\n" +
                        "  \"id\": " + currentEntry.getId() + ",\n" +
                        "  \"title\": \"" + currentEntry.getTitle() + "\",\n" +
                        "  \"author\": \"" + currentEntry.getAuthor() + "\",\n" +
                        "  \"type\": \"" + currentEntry.getType() + "\"\n" +
                        "}";
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
    }
    @FXML
    public void handleDelete(ActionEvent actionEvent) {
    }
    @FXML
    public void handleExportJson(ActionEvent actionEvent) {
        //TODO : @MOMO
    }
}
