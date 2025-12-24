module at.ac.fhcampus.simple_manager {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens at.ac.fhcampus.simple_manager to javafx.fxml;
    opens at.ac.fhcampus.simple_manager.Controllers to javafx.fxml;

    exports at.ac.fhcampus.simple_manager;
    exports at.ac.fhcampus.simple_manager.Controllers;
}
