module com.unina.project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;
    requires org.jfxtras.styles.jmetro;
    requires java.sql;

    opens com.unina.project to javafx.fxml;
    exports com.unina.project;
    opens com.unina.project.controller to javafx.fxml;
    exports com.unina.project.controller;
}