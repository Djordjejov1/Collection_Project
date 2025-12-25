module at.ac.fhcampus.simple_manager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires jdk.compiler;
    requires com.google.gson;

    opens at.ac.fhcampus.simple_manager to javafx.fxml;
    opens at.ac.fhcampus.simple_manager.Controllers to javafx.fxml;


    opens at.ac.fhcampus.simple_manager.Models to com.google.gson;

    exports at.ac.fhcampus.simple_manager;
    exports at.ac.fhcampus.simple_manager.Controllers;


    exports at.ac.fhcampus.simple_manager.Models;
}
