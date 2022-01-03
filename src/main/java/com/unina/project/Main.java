package com.unina.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class Main extends Application {
    private static Scene loginScene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loginPaneLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent loginPane = loginPaneLoader.load();
        loginScene = new Scene(loginPane, 300, 400);

        JMetro jMetro = new JMetro(Style.LIGHT);
        jMetro.setScene(loginScene);
            primaryStage.setTitle("FormazioneFacile");
            primaryStage.setScene(loginScene);
            primaryStage.setResizable(false);
            primaryStage.show();
        }
    public static Scene getLoginScene() {
        return loginScene;
    }

    public static void main(String[] args) {
        launch();
    }

}
