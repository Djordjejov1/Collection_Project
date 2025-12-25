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



    private static final ObservableList<CollectionEntry> entries = FXCollections.observableArrayList(); // mainlist foür alle Entries!



    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setWidth(700);
        primaryStage.setHeight(500);
        primaryStage.setResizable(false); // fixiertezz größe, kann dann nicht mehr geändert werden!
        showMainView();
        primaryStage.show();
    }

    public static void showMainView() throws Exception{
        FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource("/at/ac/fhcampus/simple_manager/main_view.fxml")
        );

        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Scene 1");
        primaryStage.setScene(scene);
    }

    public static void showAddView() throws Exception{

        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(
                "/at/ac/fhcampus/simple_manager/AddNewEntry_view.fxml"
        ));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Scene 2");
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static ObservableList<CollectionEntry> getEntries() {
        return entries;
    }

}
