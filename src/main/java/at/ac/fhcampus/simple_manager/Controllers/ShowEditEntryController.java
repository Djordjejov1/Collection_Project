package at.ac.fhcampus.simple_manager.Controllers;

import at.ac.fhcampus.simple_manager.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ShowEditEntryController {




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
