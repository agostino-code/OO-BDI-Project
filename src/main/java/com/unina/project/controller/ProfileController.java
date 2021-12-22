package com.unina.project.controller;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ProfileController {

    private Scene loginScene;

    public void setLoginScene(Scene scene) {
        loginScene = scene;
    }

    public void openLoginScene(ActionEvent actionEvent) {
        Stage primaryStage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        primaryStage.setScene(loginScene);
    }

}
