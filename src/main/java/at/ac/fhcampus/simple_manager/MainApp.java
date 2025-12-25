package at.ac.fhcampus.simple_manager;

import at.ac.fhcampus.simple_manager.Models.CollectionEntry;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {

    public static Stage primaryStage; // das jeweilige Fenster!
    public static Stage waringStage;
    private static final ObservableList<CollectionEntry> entries = FXCollections.observableArrayList(); // mainlist foür alle Entries!
    private static CollectionEntry selectedEntry; // für den radiobutton


    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setResizable(false); // fixiertezz größe von der stage bzw fenster, kann dann nicht mehr vom user geändert werden!
        showMainView();
        CollectionEntry.loadTestData(); //testdata :D
        primaryStage.show();

    }

    public static void showMainView() throws Exception{
        FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource("/at/ac/fhcampus/simple_manager/main_view.fxml"));

        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Scene 1");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
    }

    public static void showAddView() throws Exception{

        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/at/ac/fhcampus/simple_manager/addnewentry_view.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Scene 2");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
    }

    public static void showEditEntryView() throws Exception{
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/at/ac/fhcampus/simple_manager/showeditentry_view.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Scene 3");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene(); //hinzugefügt, weil die textbox den screen blöd mitnimmt. Passt die Fenstergröße automatisch nach der Scene an.
        primaryStage.centerOnScreen();


    }


    public static ObservableList<CollectionEntry> getEntries() {
        return entries;
    }

    //Radio-Button part
    public static void setSelectedEntry(CollectionEntry entry) {
        selectedEntry = entry;
    }

    public static CollectionEntry getSelectedEntry() {
        return selectedEntry;
    }



    public static void main(String[] args) {
        launch(args);
    }

}
