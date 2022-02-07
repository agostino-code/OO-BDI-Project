package com.unina.project;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loginPaneLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent loginPane = loginPaneLoader.load();
        Scene loginScene = new Scene(loginPane, 400, 400);
        JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setScene(loginScene);
            primaryStage.setTitle("FormazioneFacile");
            primaryStage.setScene(loginScene);
            primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Vuoi davvero uscire?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent())
            if (result.get() == ButtonType.OK){
                Platform.exit();
            }
        });
            primaryStage.show();
        }

    public static void main(String[] args) {
        launch();
    }

}
