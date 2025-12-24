package at.ac.fhcampus.simple_manager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource("/at/ac/fhcampus/simple_manager/main_view.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setTitle("Collection Manager");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
