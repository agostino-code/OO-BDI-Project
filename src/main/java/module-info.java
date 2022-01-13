module com.unina.project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.jfxtras.styles.jmetro;
    requires java.sql;
    requires java.mail;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    // add icon pack modules
    requires org.kordamp.ikonli.win10;

    opens com.unina.project to javafx.fxml;
    exports com.unina.project;
    opens com.unina.project.controller to javafx.fxml;
    exports com.unina.project.controller;
    opens com.unina.project.graphics to javafx.fxml;
    exports com.unina.project.graphics;
    exports com.unina.project.controller.profile;
    opens com.unina.project.controller.profile to javafx.fxml;
}